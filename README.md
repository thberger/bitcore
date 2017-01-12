# JIGTR - JIRA Issues by Git Tag Reporter

Simple tool to fetch all Git commits that are associated with a certain Git tag 
and try to extract JIRA issues from the commit messages. Works with Stash and Bitbucket.

The project is based on Spring Boot 1.4.x and Retrofit 2.0.

## Build

    mvn package
    
## Configure
    
Edit application.yml and enter the coordinates of your Stash/Bitbucket repo.

Example:

    stashserver:
      base-url: https://src.openvz.org
      project: OVZ
      repo: openvz-docs
      username:
      password:
      
## Run

    mvn spring-boot:run

Currently, only the tags are listed. Fetching of commits and JIRA issues is in progress.