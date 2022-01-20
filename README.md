# PMDMMO

This project is inspired by the [Pokémon Mystery Dungeon] series. Our goal is
to create a multiplayer dungeon exploring game heavily inspired by the series.

This project is currently on indefinite hold due to the state of fangaming on nintendo's licences.

If you are interested by joining the development of this project, feel free to contact us via GitHub.

Discord: https://discord.gg/KegXRG8

## Usage

To give build and run:

```sh
./gradlew :PMDMMOc:run # client
./gradlew :PMDMMO-DataEditor:run # data editor
```

Tested using Gradle 5.2.1. Most default commands (eg test, jar, etc) should
work. Older versions supporting Kotlin DSL may still work but nothing is
guaranteed.

Server builds in dev using IDE specific project files only (Netbeans). Dev databases are to be made accessible by the developper andby default use the Derby integrated environement in Netbeans. It should be noted that database layout doesn't need creation, as the server dynamically creates tables upon first connection to an enmpty database.
Release builds are to be made with javac on the root folder and hosted as a war file on a glassfish server (or any other able java webservice provider, such as redhat solutions, but the legacy PTR used glassfish).

[Pokémon Mystery Dungeon]: https://en.wikipedia.org/wiki/Pok%C3%A9mon_Mystery_Dungeon
