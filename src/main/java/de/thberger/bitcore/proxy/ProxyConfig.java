package de.thberger.bitcore.proxy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "proxy")
@Getter @Setter
public class ProxyConfig {

    String hostname;

    int port;

    String username;

    String password;

}
