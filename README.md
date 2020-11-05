# minesweeper-api
Minesweeper API

### Prerequisites

In order to run the microservice, you have to have installed Java JDK 11 in your local machine.
This project uses maven wrapper, so you don't need to have maven installed.
Also, you need a PostgreSQL database installed in your machine.

### Installing and running

Checkout the project from GitHub and go to the root folder.
 
To compile and package the application
```
> mvnw clean package
```
To run the application with spring-boot maven plugin to have to run
```
> mvnw spring-boot:run
```
or you can run it via the java -jar command
```
> java -jar .\target\minesweeper-api-0.0.1-SNAPSHOT.jar
```

Now, the application is running on http://locahost:8080/minesweeper.

### Database

This application uses a PostgreSQL database and run migrations using liquibase.


### Testing
To execute the application tests using maven, you have to run
```
> mvnw clean test
```
Also, in the folder postman in the project root, there is a Postman collection to test te API.

### Running Docker container
You can create a docker image and run the app in a docker container. For this you need Docker installed in your machine. 


Open a terminal and go to app root folder and build the application .jar file using maven wrapper.
```
> mvnw clean package
```
Build a docker image from the Dockerfile.
```
> docker build -t minesweeper-api .
> docker run --rm -ti -d -p 8080:8080 --name minesweeper minesweeper-api
```
Now, the application is running on http://locahost:8080/minesweeper.

If you want to access to the container you have to run
```
> docker attach minesweeper
```
If you want to remove the container, you have to run
```
> docker kill minesweeper
```

### Development consideration and improvements.
This is a Proof of concept project, so It can have some things to be improved.

I tested the application with some units tests and manually, using Postman. 
There is a lot of more tests that can be implemented. 
Of course, there are a lot of validation that will be useful to implement, mainly in the input of the endpoints.

There are some fields in the responses that can be hidden in order to avoid showing data, but I think those are good just
for the PoC. 

The API implemented an in-memory map in order to store the current games. It can be improved using a cache like 
Memcached or a NoSQL database like Redis.

Also, for this project I implemented an Hibernate User Type in order to be able to work with Postgres JSON type. It can 
be improved using a document oriented No-SQL database like MongoDB to store the cell maps.

Regarding the UI (web or mobile) the API can implement the OAuth2 protocol in order to secure the resources. 

### Playing
To "play" the game you need a user. So, the first thing to do is create a new one. 
(The database generation will create one too).

So, you have two user-related endpoints

```
> GET http://localhost:8080/minesweeper/users/
``` 
to list the users created and 
```
> POST http://localhost:8080/minesweeper/users/
Request Body: 
{ 
    "name":"{PLAYER_NAME}" 
}
``` 
to create a new one.

Then you can start creating games and start playing.

```
> GET http://localhost:8080/minesweeper/games?userId={USER_ID}
``` 
Will retrieve all the games (current and saved) for a given user.
To create a new one you have to use the endpoint:
```
> POST http://localhost:8080/minesweeper/games
Request body:
{
    "name": GAME_NAME,
    "difficulty": BEGINNER | INTERMEDIATE | ADVANCED | CUSTOM,
    "user": {
        "id": USER_ID
    }
}
``` 
This endpoint will create a new game for the given user and assign it as current game. 
It will not store it in the database.

If you want to save the current game in the database, you have to invoke:
```
> PUT http://localhost:8080/minesweeper/games/current?userId={USER_ID}
``` 
If you want to load a saved game you have to invoke: 
```
> GET http://localhost:8080/minesweeper/games/{GAME_NAME}?userId={USER_ID}
```
(If you load a game, you will lose the current game)

Finally, when you have your game ready, you can start making cell action ("playing") 
using this endpoint:

```
> POST http://localhost:8080/minesweeper/games/cells?userId=1
Request body:
{
    "row":{ROW},
    "column":{COLUMN},
    "action": REVEAL | RED_FLAG | QUESTION_MARK
}
```





