package de.thberger.jigt.service;

import de.thberger.jigt.domain.Commit;
import de.thberger.jigt.domain.Page;
import de.thberger.jigt.domain.Tag;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author thb
 */
public interface StashAPI {

    @GET("tags/")
    Call<Page<Tag>> getTags();

    @GET("commits/")
    Call<Page<Commit>> getCommits();

}
