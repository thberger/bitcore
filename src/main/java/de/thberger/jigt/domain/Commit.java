package de.thberger.jigt.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author thb
 */
@Getter @Setter
public class Commit {

    String id;

    String displayId;

    String message;

    Author author;

    String authorTimestamp;

    @Getter @Setter
    public static class Author {
        String name;
        String emailAddress;
    }
}
