# Installatiehandleiding
## Benodigdheden
- Java 11
- Werkende MySQL 8 server
- IntelliJ IDEA Ultimate 2021.3.3
- Git v2.30.1

## Installatie
### 1. Clone
Via de Terminal clone de git repository van de repository door middel van `git clone https://github.com/lfaoanl/backend-rooster-api.git`. Dit zal een nieuwe folder aanmaken op de locatie met de broncode erin.
Import project in IntelliJ
In IntelliJ, klik boven aan op “File” en daarna “Open…”. Navigeer naar de nieuw aangemaakte folder. IntelliJ zal je vragen of je het project vertrouwd. Klik op “Trust”. Onderin zul je een laadbalk zien van Maven die het project aan het importeren. Ga verder naar de volgende stap wanneer dat gebeurd is.
### 2. Update application.properties
In het bestand “application.properties” staat de connectie naar de database gedefinieerd. Update de volgende properties zodat een connectie met jouw MySQL database gemaakt kan worden.
```properties
spring.datasource.url=jdbc:mysql://{DB_SERVER}:{DB_PORT}/{DB_NAME}
spring.datasource.username={DB_USER}
spring.datasource.password={DB_PASSWD}
```
### 3. Eerste test
In IntelliJ, navigeer naar het bestand “NoviBackendRoostersApplication”. Open het bestand. Aan de linker kant in de editor bevind zich een groene pijl. Klik hier op. IntelliJ zal automatisch de Run properties instellen om gemakkelijk het project te kunnen testen. De server zal opstarten op localhost:8080. Als de server klaar is met opstarten kun je doorgaan naar de volgende stap.

### 4. Test gebruikers toevoegen
Login op je database en run de volgende queries.
```sql
insert into users values 
    (1, "admin@novi.nl", "NOVI", "$2a$10$0hKSwDuME9k3VPzwFL9pUu/ADrrwlFRA2lNvO/hAYIoLTsy9jlTHO", "ADMIN"),
    (2, "user@novi.nl", "NOVI", "$2a$10$0hKSwDuME9k3VPzwFL9pUu/ADrrwlFRA2lNvO/hAYIoLTsy9jlTHO", "USER");
update hibernate_sequence set next_val = next_val + 2;
```
Deze queries voegen de eerste gebruikers toe. Je kan een access token opvragen door met deze gebruiker in te loggen op de `/auth`endpoint met de volgende request body:
```json
{
  "email": "test@novi.nl",
  "password": "secret"
}
```

## REST-endpoints

### Auth
#### Auth Login 
POST `http://localhost:8080/auth`  

##### Request body
```json
{
"email": "test@novi.nl",
"password": "secret"
}
```
##### Response
```json
{
"token_type": "Bearer",
"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5vdmkubmwiLCJleHAiOjE2NTIyODQ0ODQsImlhdCI6MTY1MTQyMDQ4NH0.LAeQ-gLTRkxPsdqfGLkvvWu2wZB6VjR3I0iDjTmNTxY",
"expires_at": 1652284484825
}
```

### Users
#### List Users
GET http://localhost:8080/users

#### Show User
http://localhost:8080/users/17
Make things easier for your teammates with a complete request description.
Authorization
Bearer Token
Token
{{TOKEN}}
POST
#### Create User
http://localhost:8080/users
Make things easier for your teammates with a complete request description.
Authorization
Bearer Token
Token
{{ADMIN_TOKEN}}
Bodyraw (json)
json
{
"name": "Moniek",
"email": "moniek@gmail.nl",
"password": "secret",
"role": "USER"
}
POST
Update User
http://localhost:8080/users/17
Make things easier for your teammates with a complete request description.
Authorization
Bearer Token
Token
{{ADMIN_TOKEN}}
Bodyraw (json)
json
{
"name": "Moniek",
"email": "moniek@gmail.com",
"role": "USER"
}
DEL
Delete User
http://localhost:8080/users/3
Make things easier for your teammates with a complete request description.
GET
Show User Schedules
http://localhost:8080/users/16/schedules
Make things easier for your teammates with a complete request description.
Authorization
Bearer Token
Token
{{ADMIN_TOKEN}}
GET
Me
http://localhost:8080/users/me
Make things easier for your teammates with a complete request description.
Authorization
Bearer Token
Token
{{TOKEN}}
POST
Me Update
http://localhost:8080/users/me
Make things easier for your teammates with a complete request description.
Authorization
Bearer Token
Token
{{ADMIN_TOKEN}}
Bodyraw (json)
json
{
"email": "peter@gmail.com",
"password": "secret",
"name": "peter"
}
