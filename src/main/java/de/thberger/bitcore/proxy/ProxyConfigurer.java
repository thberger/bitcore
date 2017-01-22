package de.thberger.bitcore.proxy;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.Authenticator;
import okhttp3.Credentials;

@Configuration
public class ProxyConfigurer {

    @Bean
    public Proxy httpProxy(ProxyConfig proxyConfig) {
        if (proxyConfig.getHostname() != null) {
            return new Proxy( Proxy.Type.HTTP, new InetSocketAddress(proxyConfig.getHostname(), proxyConfig.getPort()) );
        } else {
            return null;
        }
    }

    @Bean
    @Autowired
    public Authenticator proxyAuthenticator(ProxyConfig proxyConfig) {
        return ( route, response ) -> {
            String credential = Credentials.basic(proxyConfig.getUsername(), proxyConfig.getPassword());
            return response.request().newBuilder()
                  .header("Proxy-Authorization", credential)
                  .build();
        };
    }


}
