# Project Aries
play with reactive programming model.
* springboot, backend
* mongodb, db
* react, frontend

---
### backend todo:
* setup mongodb locally, do CRUD and see how it behaves - [Reactive MongoDB](https://www.baeldung.com/spring-data-mongodb-reactive)
  * handle error when query produces non-unique result
    ``` 
    org.springframework.dao.IncorrectResultSizeDataAccessException: Query { "$java" : Query: { "username" : "awake"}, Fields: {}, Sort: {} } returned non unique result
      org.springframework.data.mongodb.core.ReactiveFindOperationSupport$ReactiveFindSupport.lambda$one$2(ReactiveFindOperationSupport.java:130) ~[spring-data-mongodb-4.4.0.jar:4.4.0]
      Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException: 
    ```
* setup springsecurity config


---
### database todo:
* setup `users` collection with: 
  * schema validation.
  * index.
  * unique fields (username, email).