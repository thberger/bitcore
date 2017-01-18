# BitCoRe - BitBucket Contribution Reporter

This tool tells you what you committed during the day into your BitBucket/Stash repositories.
 This may come handy in the next daily stand-up meeting.

## How does it work?

The tool fetches all of *your* Git commits spread over multiple repositories. It looks
up the JIRA summaries if issues are referenced in the commit messages.

## What do I need?

- Java Runtime installed
- Access to a Stash/BitBucket server
- Access to a JIRA server (optionally)

## What technologies are used?

The project is based on Spring Boot 1.4.x and Retrofit 2.0.

## Build

Build it with Maven:

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

Authentication is enabled when values are given for username/password, otherwise not.
      
## Run

    mvn spring-boot:run

Currently, only the tags are listed. Fetching of commits and JIRA issues is in progress.