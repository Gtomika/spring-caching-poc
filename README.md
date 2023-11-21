# Spring caching with Redis proof of concept

Demo application that uses Spring Data Redis and Spring Caching together.

- See [RedisConfig](src/main/java/org/caching/poc/config/RedisConfig.java) for the redis connection configuration.
- See [AppCachingConfig](src/main/java/org/caching/poc/config/AppCachingConfig.java) for the Redis caching configuration.
- See [HouseService](src/main/java/org/caching/poc/service/HouseService.java) for Spring Caching example usage.

# Run locally

Set up the required containers:

```
docker compose -f docker-compose.yaml up -d
```

This will create Postgres and a Redis container.

Create a run configuration that launches `org.caching.poc.CachingApplication` Add all variables in `.env` to
the run configuration, or use an env file reader plugins in IntelliJ.