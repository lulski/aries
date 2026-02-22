# Playing with Rabbit

### Get the image

```
podman pull rabbitmq:management
```


### Run it

```
podman run --detach --hostname my-rabbit --name some-rabbit \
    --env RABBITMQ_DEFAULT_USER=kocu \
    --env RABBITMQ_DEFAULT_PASS=kocu \
    --publish 15672:15672 \
    --publish 5672:5672 \
     rabbitmq:management
```

Backend configuration
---------------------

The backend reads RabbitMQ connection settings from environment variables which map to Spring Boot properties:

- `RABBITMQ_HOST` -> `spring.rabbitmq.host` (default: `localhost`)
- `RABBITMQ_PORT` -> `spring.rabbitmq.port` (default: `5672`)
- `RABBITMQ_DEFAULT_USER` -> `spring.rabbitmq.username` (default: `guest`)
- `RABBITMQ_DEFAULT_PASS` -> `spring.rabbitmq.password` (default: `guest`)

Backend listens to queue: `test-queue`

Example with custom credentials:

```
podman run --detach --hostname my-rabbit --name some-rabbit \
    --env RABBITMQ_DEFAULT_USER=kocu \
    --env RABBITMQ_DEFAULT_PASS=kocu \
    --env RABBITMQ_HOST=localhost \
    --env RABBITMQ_PORT=5672 \
    --publish 15672:15672 \
    --publish 5672:5672 \
    rabbitmq:management
```

