package de.thberger.bitcore.stash.service;



import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thberger.bitcore.stash.config.StashServerConfig;
import de.thberger.bitcore.stash.domain.Commit;

@Service
class CommitObserver {

    private final StashServerConfig.Filter filter;

    private static final Logger LOG = LoggerFactory.getLogger(CommitObserver.class);

    @Autowired
    public CommitObserver(StashServerConfig stashServerConfig) {
        this.filter = stashServerConfig.getFilter();
    }

    void process( List<Commit> commits ) {
        commits.stream()
              .filter( matches() )
              .forEach( c -> LOG.info("{} : {} ", c.getAuthor().getName(), c.getMessage()) );
    }

    private Predicate<Commit> matches() {
        return c -> c.getAuthor() != null && c.getAuthor().getName().equals( filter.getAuthor() );
    }

}
