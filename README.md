# ElytraRace

This is a mini-game that utilizes Minecraft's "Elytra". This is the 1.0 release.

# Motivation
This project was created during the beginning of the COVID-19 pandemic as a way for me to 
explore object-oriented design practices as well as apply linear algebra concepts to a 
area that interests me. This was also a way to interact with friends when social distancing
was (and still is as of the time of writing this) extremely important.

# What is ElytraRace?
ElytraRace is a mini-game that can be used in lobbies and/or small servers. 
Players spawn at the beginning of a map onto a platform. When the player jumps off of the platform, they 
equip the elytra (a cape for flying). The player then follows a path of particles through each 
ring until they reach the end platform. If the player touches the ground, they return
to the starting platform. The goal is to get the fastest time from the start platform to the end
platform, while flying through each ring. Players can compete for positions on the leaderboard. 


# Video
[![Watch the video](https://imgur.com/rFxqFLo.png)](https://www.youtube.com/watch?v=dzWbd6iux1w)

## Technologies Used / Experimented with
- MySQL 
- Java 
- MongoDB
- Maven

## Concepts / Patterns
- visitor pattern
- factory pattern
- builder pattern
- threading
- linear algebra / rotation matricies

## Get Started

```bash
git clone git@github.com:kyleposluns/elytrarace.git
cd elytrarace
mvn install
mvn package
```

```bash
mysql -u user -p < elytraraceschema.sql
mysql -u user -p < flores.sql
mysql -u user -p < temple.sql
```

Move the jar into the plugins folder of your minecraft server, start your server and enjoy