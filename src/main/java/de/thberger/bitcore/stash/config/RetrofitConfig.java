package de.thberger.bitcore.stash.config;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author thb
 */
@Configuration
public class RetrofitConfig {

    private Authenticator proxyAuthenticator;

    private Proxy httpProxy;

    @Bean
    @Autowired
    public Retrofit retrofit( StashServerConfig serverConfig ) {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient client = stashHttpClient( serverConfig ).build();
        String repoRestUrl = restBaseUrl( serverConfig );

        return new Retrofit.Builder()
              .baseUrl( repoRestUrl )
              .client( client )
              .addConverterFactory( GsonConverterFactory.create( gson ) )
              .build();
    }

    private String restBaseUrl( StashServerConfig serverConfig ) {
        return serverConfig.getBaseUrl() + "/rest/api/1.0/";
    }

    @Bean
    public OkHttpClient.Builder stashHttpClient( StashServerConfig stashServerConfig) {
        try {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            if ( stashServerConfig.isIgnoreSslCerts() ) {
                X509TrustManager unsafeTrustManager = getUnsafeTrustManager();
                final TrustManager[] trustAllCerts = new TrustManager[] { unsafeTrustManager };
                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance( "SSL" );
                sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                builder.sslSocketFactory( sslSocketFactory, unsafeTrustManager );
                builder.hostnameVerifier( ( hostname, session ) -> true );
            }

            if ( stashServerConfig.isUseProxy() ) {
                builder.proxy( httpProxy );
                builder.proxyAuthenticator( proxyAuthenticator );
            }

            boolean isAuthenticated = !StringUtils.isEmpty( stashServerConfig.getUsername() );
            if ( isAuthenticated ) {
                builder.addInterceptor( chain -> {
                    Request original = chain.request();
                    String basic = Credentials.basic( stashServerConfig.getUsername(), stashServerConfig.getPassword() );
                    Request.Builder requestBuilder = original.newBuilder()
                          .header( "Authorization", basic );
                    requestBuilder.header( "Accept", "application/json" );
                    requestBuilder.method( original.method(), original.body() );
                    Request request = requestBuilder.build();
                    return chain.proceed( request );
                } );
            }
            return builder;
        } catch ( Exception e ) {
            throw new IllegalStateException( "Failed to initialize Http client for BitBucket server!", e );
        }
    }

    @Autowired
    public void setProxyAuthenticator( Authenticator proxyAuthenticator ) {
        this.proxyAuthenticator = proxyAuthenticator;
    }

    @Autowired
    public void setProxy( Proxy proxy ) {
        this.httpProxy = proxy;
    }

    private X509TrustManager getUnsafeTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted( java.security.cert.X509Certificate[] chain, String authType ) throws CertificateException {
                // trust all certs
            }

            @Override
            public void checkServerTrusted( java.security.cert.X509Certificate[] chain, String authType ) throws CertificateException {
                // trust all servers
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
        };
    }

}
