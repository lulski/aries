# Project Aries

play with reactive programming model.

- springboot, backend
- mongodb, db
- react, frontend

---

### Backend tasks:

- [x] setup mongodb locally, do CRUD and see how it behaves - [Reactive MongoDB](https://www.baeldung.com/spring-data-mongodb-reactive)
- [] setup `user` endpoints
  - create [x]
  - delete [x]
  - list [x]
  - update [ ]
- [ ] setup springsecurity config
- [ ] setup `post` endpoints
  - [ ] create
  - [ ] delete
  - [ ] list
  - [ ] update

### Error handling to-do:

- [x] handle GET /api/v1/user/belmont error when query produces non-unique result

  ```
  org.springframework.dao.IncorrectResultSizeDataAccessException: Query { "$java" : Query: { "username" : "awake"}, Fields: {}, Sort: {} } returned non unique result
    org.springframework.data.mongodb.core.ReactiveFindOperationSupport$ReactiveFindSupport.lambda$one$2(ReactiveFindOperationSupport.java:130) ~[spring-data-mongodb-4.4.0.jar:4.4.0]
    Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException:
  ```

- this error is fixed by using findTopByUsername()

---

### Database tasks:

- [ ] setup `users` collection with:

  - [x] schema validation.

    ```
    use 'aries'

    db.collection `users` schema validation
    {
      $jsonSchema: {
        required: [
          'username',
          'firstName',
          'email'
        ],
        properties: {
          username: {
            bsonType: 'string',
            minLength: 3,
            description: 'Must be a non-empty string and is required'
          },
          firstName: {
            bsonType: 'string',
            minLength: 1,
            description: 'Must be a non-empty string and is required'
          },
          email: {
            bsonType: 'string',
            pattern: '.+@.+\\..+',
            description: 'Must be a valid email'
          }
        }
      }
    }
    ```

  - [ ] Collection index.
  - [ ] unique fields (username, email).