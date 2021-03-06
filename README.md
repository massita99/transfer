[![Build Status](https://travis-ci.org/massita99/transfer.svg?branch=master)](https://travis-ci.org/massita99/transfer)
[![codecov](https://codecov.io/gh/massita99/transfer/branch/master/graph/badge.svg)](https://codecov.io/gh/massita99/transfer)
# Transfer
RESTful API for money transfer between accounts

### Quick start

`./gradlew run`

The command run endpoint on localhost on port 8080.
All API documentation hosted by Swagger-UI on http://localhost:8080/swagger/views/swagger-ui/index.html
For test reason one can use test account with id = 'TEST'

`./gradlew test`

the command run all tests

### Used Technologies
* https://micronaut.io - stand-alone App container
* https://swagger.io/ - API design tool
* https://www.jooq.org - SQL DSL and transaction manager
* https://flywaydb.org - database migration tool
* https://www.h2database.com - in-memory database
* http://modelmapper.org - object mapper
* https://junit.org/junit5/ - main test framework
* https://joel-costigliola.github.io/assertj/ - assertion DSL
* https://github.com/awaitility/awaitility - for asynchronous testing
* https://gradle.org - build automation tool