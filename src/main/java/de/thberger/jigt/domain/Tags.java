package de.thberger.jigt.domain;

import java.util.List;

/**
 * @author thb
 */
public class Tags {

    int size;
    int limit;
    boolean isLastPage;
    List<Tag> values;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public List<Tag> getValues() {
        return values;
    }

    public void setValues(List<Tag> values) {
        this.values = values;
    }



}
