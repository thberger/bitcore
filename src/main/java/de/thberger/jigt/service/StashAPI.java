package de.thberger.jigt.service;

import de.thberger.jigt.domain.Tags;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author thb
 */
public interface StashAPI {

    @GET("tags/")
    Call<Tags> loadTags();


}
