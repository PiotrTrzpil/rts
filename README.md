# rts

A real-time strategy / city-building game prototype written in Java with a Swing UI. Players build a base, train workers and military units, harvest resources, and engage in combat. Supports single-player and LAN multiplayer (client/server over TCP).

## Features

- Tile-based map with terrain, buildings, and units
- Building types: storehouse, woodcutter, lumber mill, stonecutter, barracks, bowmaker, armorer
- Unit types: builder, carrier, warrior, archer
- Item-transport system — carriers haul goods between buildings
- Multiplayer over TCP (server listens on port `6113`)

## Prerequisites

- **JDK 25** — set as the Gradle toolchain. Earlier JDKs work if you lower `languageVersion` in `build.gradle`. Gradle can auto-provision a JDK if none is installed locally.
- (Optional) **[just](https://github.com/casey/just)** for the convenience commands below.

No global Gradle install is needed — the bundled wrapper (`./gradlew`) downloads its own.

## Running

```sh
just run         # or: ./gradlew run
```

A Swing window opens with the main menu (New Game / Join Game).

For LAN multiplayer: one machine hosts via *New Game*; others connect via *Join Game* using the host's IP address. TCP port `6113` must be reachable.

## Other commands

```sh
just build       # compile and assemble
just clean       # remove build outputs
just dist        # produce a standalone launcher script at build/install/rts/bin/rts
```

## License

See `LICENSE`.
