# Playing with Rabbit

### Get the image

```
podman pull rabbitmq:management-alpine
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