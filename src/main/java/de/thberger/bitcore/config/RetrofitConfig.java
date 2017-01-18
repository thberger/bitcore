package de.thberger.bitcore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.*;
import java.security.cert.CertificateException;

/**
 * @author thb
 */
@Configuration
public class RetrofitConfig {

    @Bean
    @Autowired
    public Retrofit retrofit(StashServerConfig serverConfig) {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient client = unsafeOkHttpClient(serverConfig).build();
        String repoRestUrl = buildRepoRestUrl(serverConfig);

        return new Retrofit.Builder()
                .baseUrl(repoRestUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private String buildRepoRestUrl(StashServerConfig serverConfig) {
        return serverConfig.getBaseUrl()
                + "/rest/api/1.0"
                + "/projects/" + serverConfig.getProject()
                + "/repos/" + serverConfig.getRepo() + "/";
    }

    @Bean
    @Autowired
    public OkHttpClient.Builder unsafeOkHttpClient(StashServerConfig stashServerConfig) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = getUnsafeTrustManager();

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier((hostname, session) -> true);

            boolean isAuthenticated = !StringUtils.isEmpty(stashServerConfig.getUsername());
            if (isAuthenticated) {
                builder.addInterceptor(chain -> {
                    Request original = chain.request();
                    String basic = Credentials.basic(stashServerConfig.getUsername(), stashServerConfig.getPassword());
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic);
                    requestBuilder.header("Accept", "application/json");
                    requestBuilder.method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                });
            }
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TrustManager[] getUnsafeTrustManager() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
    }

}
