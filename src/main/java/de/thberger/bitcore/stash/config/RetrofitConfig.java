package de.thberger.bitcore.stash.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

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
                addAllTrustingSslManager(builder);
            }

            if ( stashServerConfig.isUseProxy() ) {
                addProxyAuthenticator(builder);
            }

            if ( stashServerConfig.isAuthenticated() ) {
                addBasicAuth(stashServerConfig, builder);
            }
            return builder;
        } catch ( Exception e ) {
            throw new IllegalStateException( "Failed to initialize Http client for BitBucket server!", e );
        }
    }

    private void addBasicAuth(StashServerConfig stashServerConfig, OkHttpClient.Builder builder) {
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

    private void addProxyAuthenticator(OkHttpClient.Builder builder) {
        builder.proxy( httpProxy );
        builder.proxyAuthenticator( proxyAuthenticator );
    }

    private void addAllTrustingSslManager(OkHttpClient.Builder builder) throws NoSuchAlgorithmException, KeyManagementException {
        UnsafeSslFactory unsafeSslFactory = new UnsafeSslFactory();
        builder.sslSocketFactory( unsafeSslFactory.createAllTrustingSslFactory(), unsafeSslFactory.getUnsafeTrustManager() );
        builder.hostnameVerifier( ( hostname, session ) -> true );
    }

    @Autowired
    public void setProxyAuthenticator( Authenticator proxyAuthenticator ) {
        this.proxyAuthenticator = proxyAuthenticator;
    }

    @Autowired
    public void setProxy( Proxy proxy ) {
        this.httpProxy = proxy;
    }



}
