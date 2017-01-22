package de.thberger.bitcore.stash.service;

import de.thberger.bitcore.stash.domain.Commit;
import de.thberger.bitcore.stash.domain.Page;
import de.thberger.bitcore.stash.domain.Tag;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author thb
 */
public interface StashAPI {

    @GET("tags/")
    Call<Page<Tag>> getTags();

    @GET("projects/{project}/repos/{repo}/commits/")
    Call<Page<Commit>> getCommits(@Path("project") String project, @Path("repo") String repo, @Query("start") int start);

}
