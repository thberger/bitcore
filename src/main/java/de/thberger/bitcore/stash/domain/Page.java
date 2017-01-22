package de.thberger.bitcore.stash.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author thb
 */
@Getter @Setter
public class Page<T> {

    int start;

    int size;

    int limit;

    boolean isLastPage;

    List<T> values;

}
