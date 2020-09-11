# ElytraRace

This is a minigame that utilizes Minecraft's "Elytra". This is the 1.0 release.

# Video
[![Watch the video](https://imgur.com/rFxqFLo.png)](https://www.youtube.com/watch?v=dzWbd6iux1w)



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