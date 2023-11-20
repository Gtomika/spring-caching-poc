# Spring caching proof of concept

Demo application that uses a Postgres datastore and a Redis cache in front of it.

# Run locally

Set up the required containers:

```
docker compose -f docker-compose.yaml up -d
```

Add all variables in `.env` to the run configuration or use env file reader plugins in IntelliJ.