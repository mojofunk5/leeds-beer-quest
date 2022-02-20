# Leeds Beer Quest

For the the first part of the challenge I've focused on the infrastructure parts required to build, host and run the various parts of the application. 

Initially I've got:

* A barebones application server implemented in Java using Javalin which is a lightweight web framework providing little more than a thin veneer over Jetty
* Standard SQL scripts to create a BEER schema and VENUE table
* A python script to convert the initial CSV dataset into a series of SQL inserts
* Those SQL scripts in a folder which can then be run by a Java tool called flyway which maintains a record of migrations run and will automatically upgrade the database as required
* Two simple integration tests that:
  - validate the application server starts and a simple hello world end point can be invoked and return an OK response
  - validates the flyway migration scripts have been run, the schema is present and the initial dataset has been loaded
* Docker build file for the application server and a docker compose file to run the application server and a mariadb instance

What I've not done:

* Anything on the UI side
* I would probably have something like nginx serving a React web application

## Database and testing

Whilst the deployed application uses MariaDB the integration tests use an inmemory h2 instance.

Ideally I would have used MariaDB4J which is an embeddable version of MariaDB. However it doesn't work on my machine as there are [issues with Apple M1 silicon](https://stackoverflow.com/questions/69896059/is-it-possible-to-get-mariadb4j-to-work-on-an-m1-mac)

h2 is a lightweight Java embeddable database and does offer some MySQL/Maria compatibility and as long as the SQL is compliant the scripts should work on both types. 

There was some fun trying to get it working as flyway would run and then when connecting with JDBC in the integration test it couldn't find the schema. This turned out to be around how the different databases [handle table names and casing](https://stackoverflow.com/questions/14972408/schema-related-problems-with-flyway-spring-and-h2-embedded-database).

The python script to convert the dataset from CSV to SQL inserts is found under the `scripts` directory. It was run like so:

```
$ python3 convert_csv_to_sql_inserts.py -i ../service/docs/original_dataset/leedsbeerquest.csv --output ../service/src/main/resources/db/migration/V2__insert_venue_data.sql
Running with input=[../service/docs/original_dataset/leedsbeerquest.csv] and output=[../service/src/main/resources/db/migration/V2__insert_venue_data.sql]
```

As h2 runs in memory the database is thrown away after each run of the integration test class ensuring that the migration scripts are run and validated each time.

## Running Locally

To run the application it has the following pre-requisites:

* Java 17 installed
* Maven installed (tested on `3.8.4`)
* Docker and docker compose installed

**Note:** python3 would be required if you wished to run the dataset conversion but this isn't necessary as it's already been done.

First build the application from source:

1. `cd service`
1. `mvn clean install`

Once that has built then run docker compose:

1. `cd ../docker-compose`
1. `docker-compose up`

This will start both the database and application containers and once the database is up the application will connect and run the flyway migration scripts (if required) and then Javalin on port `8080`.

To validate it's working then try calling the hello world endpoint in another terminal:

```
$ curl localhost:8080/hello
{"hello":"world"}
```

## Next steps

In the next session to build on this I would flesh out the application part to implement the API probably presenting the same interface as the original. 