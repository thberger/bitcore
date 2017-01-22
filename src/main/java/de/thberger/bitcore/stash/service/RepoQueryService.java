package de.thberger.bitcore.stash.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thberger.bitcore.stash.config.StashServerConfig;
import de.thberger.bitcore.stash.domain.Commit;
import de.thberger.bitcore.stash.domain.Page;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author thb
 */
@Service
public class RepoQueryService implements Callback<Page<Commit>> {

    private static final Logger LOG = LoggerFactory.getLogger( RepoQueryService.class );

    private Retrofit retrofitClient;

    private StashAPI stashAPI;

    private CommitObserver commitObserver;

    private List<StashServerConfig.Project> projects;

    @Autowired
    public void RepoQueryService( StashServerConfig stashServerConfig, CommitObserver commitObserver, Retrofit retrofit ) {
        this.projects = stashServerConfig.getProjects();
        this.retrofitClient = retrofit;
        this.commitObserver = commitObserver;
    }

    @PostConstruct
    public void start() {
        LOG.debug( "Using base URL " + retrofitClient.baseUrl() );
        stashAPI = retrofitClient.create( StashAPI.class );
        for (StashServerConfig.Project project : projects) {
            for (String repository : project.getRepos()) {
                getNextCommitPage( project.getName(), repository, 0 );
            }
        }
    }

    private void getNextCommitPage( String projectName, String repository, int start ) {
        stashAPI.getCommits( projectName, repository, start ).enqueue( this );
    }

    @Override public void onResponse( Call<Page<Commit>> call, Response<Page<Commit>> response ) {
        if ( response.isSuccessful() ) {
            Page<Commit> page = response.body();
            commitObserver.process( page.getValues() );
            if ( !page.isLastPage() ) {
                int newStart = page.getSize() + page.getStart();
                //getNextCommitPage( newStart );
            }
        } else {
            try {
                LOG.error( "{} Request not successful on {}: {}", call.request().method(), call.request().url(), response.errorBody().string() );
            } catch ( IOException e ) {
                LOG.error( "{} Request failed with exception: on {}: {}", call.request().method(), call.request().url(), e.getMessage(), e );
            }
        }
    }

    @Override public void onFailure( Call<Page<Commit>> call, Throwable e ) {
        LOG.error( "{} Request failed on {}: {}", call.request().method(), call.request().url(), e.getMessage(), e );
    }


}
