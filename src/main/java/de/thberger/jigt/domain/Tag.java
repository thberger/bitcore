package de.thberger.jigt.domain;

/**
 * @author thb
 */
public class Tag {

    String id;

    String displayId;

    String latestChangeSet;

    String hash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public String getLatestChangeSet() {
        return latestChangeSet;
    }

    public void setLatestChangeSet(String latestChangeSet) {
        this.latestChangeSet = latestChangeSet;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
