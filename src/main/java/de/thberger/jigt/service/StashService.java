package de.thberger.jigt.service;

import de.thberger.jigt.domain.Commit;
import de.thberger.jigt.domain.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author thb
 */
@Service
public class StashService {

    private static final Logger LOG = LoggerFactory.getLogger(StashService.class);

    private Retrofit retrofitClient;

    @Autowired
    public void setRetrofitClient(Retrofit retrofit) {
        this.retrofitClient = retrofit;
    }

    @PostConstruct
    public void start() {
        LOG.debug("Using base URL " + retrofitClient.baseUrl());
        StashAPI stashAPI = retrofitClient.create(StashAPI.class);
        Page<Commit> commits = execute( stashAPI.getCommits() );
        commits.getValues().forEach(  c -> LOG.info("{} : {} ", c.getAuthor().getName(), c.getMessage()) );
    }

    private  <T> T execute(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                LOG.error("Failed {} on {}: {}",  call.request().method(), call.request().url(), response.errorBody().string());
                throw new IllegalStateException();
            }
        } catch (IOException e) {
            LOG.error("Failed {} on {}: {}", call.request().method(), call.request().url(), e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }
}
