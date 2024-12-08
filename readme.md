# Aries project mongodb setup 


use 'aries'

db.collection `users` schema validation
```
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