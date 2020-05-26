## Project Name & Pitch

Webb Test app

An application uses GutHub Api in order to search and generate various statistics.
Built with Spring Boot, Thymeleaf, Bootstrap, MongoDB(embedded), Spring WebFlux

## Installation and Setup Instructions

Clone down this repository. You will need JDK 8 and Maven installed on your machine.  
Running the app very first time could take some time to download embedded mongodb
Please use famous repositories, because only they have public contributors. 
As collaborators need authenticated user in order to retrieve.

Installation:

`mvn clean install`  

To Run Test Suite:  

`mvn clean test`  

To Start Server:

`mvn spring-boot:run`  

To Visit App:

`localhost:8080`  

To use REST Api

`localhost:8080/api/v1/`
`localhost:8080/api/v1/analytics`