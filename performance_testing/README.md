# Performance testing

Apache JMeter is used to create and execute performance test plans. To be able to edit and run them, JMeter must be installed 
and the `bin` directory must be added to the `PATH`. On windows, `jmeter.bat` can be used, on UNIX it is `jmeter`.

JMeter can execute the defined plan concurrently using threads, and it can repeat it as many times as desired.

Disabling and enabling the `caching` Spring profile can be used to performance test the caching and non-caching versions 
of the application, and compare the results.

## Plan: get houses load

This plan simulates multiple users who get all houses using `GET /api/v1/houses`.

Execute the plan and save JTL result (output JTL file can be changed):

```bash
jmeter.bat -n -t get_houses_load.jmx -l results/get_houses_load_with_cache.jtl
```

Convert the JTL result into HTML report (output directory can be changed):

```bash
jmeter.bat -g results/get_houses_load_with_cache.jtl -o reports/get_houses_load_with_cache/
```

### Results: external API on AWS, local Redis

The application run using `dev` and `api-backend` profiles. The exernal API called by the application was hosted 
in the **AWS Tokyo region**, while the app, and the Redis were in Hungary.

Caching is expected to be useful here, as only one resource is accessed many times, and the non-caches data is 
geographically far from the app.

#### Results with caching

Active profiles: `dev`, `caching` and `api-backend`.

- Average response time: 30 ms
- Maximum response time: 2670 ms
- 90th percentile: 9 ms
- 95th percentile: 11 ms
- Transactions per second: 90

> If Xth percentile is Y ms, that means that X percent of the requests were faster than Y ms.

#### Results without caching

Active profiles: `dev` and `api-backend`.

- Average response time: 469 ms
- Maximum response time: 1896 ms
- 90th percentile: 963 ms
- 95th percentile: 967 ms
- Transactions per second: 6

#### Summary

Because only a single resource was requested, and due to the high geographical distances, the caching resulted in a 
significant performance increase.

## Plan: house flow

This plan consists of the following steps:

- `POST /api/v1/houses`: to create a house and extract the ID into a `${HouseID}` variable.
- `GET /api/v1/houses/${HouseId}`: get the newly created house by ID.
- `DELETE /api/v1/houses/${HouseId}`: remove the house.

Execute the plan and save JTL result (output JTL file can be changed):

```bash
jmeter.bat -n -t house_flow.jmx -l results/house_flow_with_cache.jtl
```

Convert the JTL result into HTML report (output directory can be changed):

```bash
jmeter.bat -g results/house_flow_with_cache.jtl -o reports/house_flow_with_cache/
```

### Results: local Postgres, local Redis

Both the Postgres DB and the Redis cache are running locally.

Steps executed with 5 parallel threads ("users"), each performing the steps 100 times. Since the steps define 3 HTTP requests, 
there are 5 users, and each will repeat 100 times, this means that **a total of 3 * 5 * 100 = 1500 requests will be sent to the application**.

#### Results with caching

Active profiles: `dev`, `caching` and `postgres-backend`.

- Average response time: 14 ms
- Maximum response time: 118 ms
- 90th percentile: 28 ms
- 95th percentile: 34 ms

> If Xth percentile is Y ms, that means that X percent of the requests were faster than Y ms.

#### Results without caching

Active profiles: `dev` and `postgres-backend`.

- Average response time: 14 ms
- Maximum response time: 700 ms
- 90th percentile: 18 ms
- 95th percentile: 23 ms

#### Summary

Interestingly, **the application is more performant without caching**, in this scenario. Possible explanation:

- Local Postgres is fast enough to handle this load, and the caching layer is just unnecessary overhead.

ChatGPT-4 gave the following explanations:

> When the cache and the database system are hosted locally on the same machine, they are competing for the same system resources (CPU, memory, disk I/O, network I/O, etc.). When you request data from the cache and that data does not exist (a cache miss), the application has to get the data from the database and then write that data into the cache. This additional operation of writing into the cache consumes resources and may slow down the overall responses, especially if the machine resources are scarce.
> 
> However, when the cache is turned off, all data is read directly from the database, eliminating the need for the overhead of cache operations. If the application's data set is small enough and the queries are efficient, your database might be able to return the data just as quickly, or even quicker, than it could through the cache.

**TODO**: how will the results differ if both Postgres and Redis are hosted externally, for example on AWS?
