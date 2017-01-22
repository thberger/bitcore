package de.thberger.bitcore.stash.config;

import java.util.Base64;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author thb
 */
@Configuration
@ConfigurationProperties(prefix = "stashserver")
@Getter @Setter
public class StashServerConfig {

    private String baseUrl;

    private List<Project> projects;

    private String repo;

    private String username;

    private String password;

    private Filter filter;

    private boolean useProxy;

    private boolean ignoreSslCerts;

    public void setPassword( String base64Password ) {
        this.password = new String( Base64.getDecoder().decode( base64Password ) );
    }

    @Data
    public static class Project {
        String name;

        List<String> repos;
    }

    @Data public static class Filter {

        String author;
    }
}
