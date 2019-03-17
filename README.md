# PMDMMO

This project is inspired by the [Pokémon Mystery Dungeon] series. Our goal is
to create a multiplayer dungeon exploring game heavily inspired by the series.

As this is still a new project, there is yet to be a demo or an alpha. More
information will be available very soon.

If you are interested by joining the development of this project, feel free to
contact us via GitHub.

Discord: https://discord.gg/KegXRG8

## Usage

To give build and run:

```sh
./gradlew :PMDMMOc:run # client
./gradlew :PMDMMO-DataEditor:run # data editor
```

(gradle, gradle wrapper, you know the business)

Tested using Gradle 5.2.1. Most default commands (eg test, jar, etc) should
work. Older versions supporting Kotlin DSL may still work but nothing is
guaranteed.

The server does not currently have a gradle build script. We are working on
that. Hopefully.

[Pokémon Mystery Dungeon]: https://en.wikipedia.org/wiki/Pok%C3%A9mon_Mystery_Dungeon
