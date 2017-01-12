package de.thberger.jigt.service;

import de.thberger.jigt.domain.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;

/**
 * @author thb
 */
@Service
public class StashService implements Callback<Tags> {

    private Retrofit retrofitClient;

    @Autowired
    public void setRetrofitClient(Retrofit retrofit) {
        this.retrofitClient = retrofit;
    }

    @PostConstruct
    public void start() {
        StashAPI stashAPI = retrofitClient.create(StashAPI.class);
        Call<Tags> call = stashAPI.loadTags();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Tags> call, retrofit2.Response<Tags> response) {
        if (response.isSuccessful()) {
            Tags tagList = response.body();
            tagList.getValues().forEach(tag -> System.out.println(tag.getDisplayId()));
        } else {
            System.err.println("Failed to get tags: " + response.message());
        }
    }

    @Override
    public void onFailure(Call<Tags> call, Throwable throwable) {
        throwable.printStackTrace();
    }
}
