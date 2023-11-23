# Spring caching with Redis proof of concept

Demo application that uses Spring Data Redis and Spring Caching together.

- See [RedisConfig](src/main/java/org/caching/poc/config/RedisConfig.java) for the redis connection configuration.
- See [AppCachingConfig](src/main/java/org/caching/poc/config/AppCachingConfig.java) for the Redis caching configuration.
- See [HouseService](src/main/java/org/caching/poc/service/HouseService.java) for Spring Caching example usage.

High level diagram of the application:

![Architectural diagram](diagram/spring-caching-poc.drawio.png)

# Technologies used

- _Spring MVC_: for the REST API.
- _Spring Data_: JPA for the Postgres connection.
- _Spring Data Redis_: for the Redis connection.
- _Spring Cache_: abstracts away the caching logic.
- Postgres database, for the `postgres-backend`.
- AWS API Gateway, Lambda and SAM for the `api-backend`.
- Redis for caching.
- Docker for local containers.

# Application profiles

- If the `caching` profile is active, the app will use caching. If this profile is not active,
the caching layer is disabled.
- If the `postgres-backend` profile is active, the local PostgreSQL database will be used to store the data.
- If the `api-backend` profile is active, the data will be stored and retrieved by an external API, which is implemented 
as an AWS API gateway + lambda function. **Configuring this requires AWS experience**.

Please note that exactly one of the `postgres-backend` or the `api-backend` profiles must be active.

# Run locally

Create a run configuration that launches `org.caching.poc.CachingApplication` Add all variables in `.env` to
the run configuration, or use an env file reader plugin in IntelliJ.

Chose either the Postgres backend or the AWS API backend: this determines where the application gets the underlying data. 
Unless you have relevant AWS experience, it is highly recommended to choose the Postgres backend, it is much simpler to set up.

## If using the local Postgres backend

Set the `SPRING_PROFILES_ACTIVE` variable to `dev,caching,postgres-backend`.

Set up the required containers:

```bash
docker compose -f docker-compose.yaml up -d
```

This will create Postgres, a Redis and a RedisInsight container. The RedisInsight is a GUI client
available on http://localhost:8001, with the following credentials:

- Username: default
- Password: redispassword

The application is ready to start and connect to the Redis and Postgres containers.

## If using the AWS API backend

**Warning: Do not attempt, unless you know AWS.**

Set the `SPRING_PROFILES_ACTIVE` variable to `dev,caching,api-backend`.

Set up the required containers:

```bash
docker compose -f docker-compose.yaml up -d --build cache
```

This will create a Redis and a RedisInsight container. The RedisInsight is a GUI client
available on http://localhost:8001, with the following credentials:

- Username: default
- Password: redispassword

To set up the external API backend on AWS, see the [relevant README](spring-caching-poc-api-backend/README.md). **This 
requires AWS experience.** After done, modify the `EXTERNAL_API_URL` in the env file to your newly created API Gateway URL.

# Performance testing

For all details about performance testing, please see the [performance testing REDAME](performance_testing/README.md).

# Postman collection

A [postman collection](spring_caching_poc.postman_collection.json) is included that can be used to invoke the API.