package de.thberger.bitcore.service;

import de.thberger.bitcore.domain.Commit;
import de.thberger.bitcore.domain.Page;
import de.thberger.bitcore.domain.Tag;
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
