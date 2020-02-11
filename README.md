# microservice-crud
Microservice for user crud operations

User service swagger url : http://localhost:9090/test/swagger-ui.html

Client micro service swagger url : http://localhost:9091/testclient/swagger-ui.html

Sample create user : 

{
  "accounts": [
    {
      "accountBalance": 120
    },
{
      "accountBalance": 80
    }
  ],
  "address": "string",
  "emailId": "test@test.com",
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string"
}


To see code coverage pls do mvn test

The jacoco report available at target/site/jacoco/index.html of the both artifacts.

The existing overage is below :
      user-crud project - 96%
      client microservice - 86%

