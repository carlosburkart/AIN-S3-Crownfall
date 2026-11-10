# Crownfall

[![Scala CI](https://github.com/carlosburkart/AIN-S3-Crownfall/actions/workflows/scala.yml/badge.svg)](https://github.com/carlosburkart/AIN-S3-Crownfall/actions/workflows/scala.yml) [![Quality gate status](https://sonarcloud.io/api/project_badges/measure?project=carlosburkart_AIN-S3-Crownfall&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=carlosburkart_AIN-S3-Crownfall) [![Coverage Status](https://coveralls.io/repos/github/carlosburkart/AIN-S3-Crownfall/badge.svg)](https://coveralls.io/github/carlosburkart/AIN-S3-Crownfall) [![Mutation testing badge](https://img.shields.io/endpoint?style=flat&url=https%3A%2F%2Fbadge-api.stryker-mutator.io%2Fgithub.com%2Fcarlosburkart%2FAIN-S3-Crownfall%2Fmain)](https://dashboard.stryker-mutator.io/reports/github.com/carlosburkart/AIN-S3-Crownfall/main)

A turn-based strategy game for two players, written in Scala 3 for the Software Engineering course at HTWG Konstanz.

## Game Idea

Two armies of knights, archers and mages face each other on an 8×8 board. Similar to chess, the players move their troops across the board. Unlike chess, every troop has stats such as hit points, attack, defense, range and movement, and some troops can attack from a distance. The goal is to defeat the enemy army.

## Getting Started

### Requirements

- JDK 21 or newer
- [sbt](https://www.scala-sbt.org/download/)

Scala, all libraries and the exact sbt version are defined in the project and downloaded automatically on the first build.

### Start the Game

```
git clone https://github.com/carlosburkart/AIN-S3-Crownfall.git
cd AIN-S3-Crownfall
sbt run
```

### Download

Every release contains a ready-to-run JAR file. Download it from the [Releases](https://github.com/carlosburkart/AIN-S3-Crownfall/releases) page and start it with

```
java -jar crownfall.jar
```

Only Java 21 or newer is required, no sbt.

## How to Play

### The Board

```
  A B C D E F G H
8 . m . . . . m .
7 k a k . . k a k
6 . k . . . . k .
5 . . . . . . . .
4 . . . . . . . .
3 . K . . . . K .
2 K A K . . K A K
1 . M . . . . M .
```

| Symbol | Troop |
|---|---|
| `K` / `k` | Knight |
| `A` / `a` | Archer |
| `M` / `m` | Mage |
| `.` | empty field |

Upper case letters (blue) belong to side One, lower case letters (red) to side Two.

### Troop Stats

| Troop | HP | Attack | Defense | Range | Movement |
|---|---|---|---|---|---|
| Knight | 10 | 4 | 5 | 1 | 1 |
| Archer | 6 | 6 | 2 | 3 | 1 |
| Mage | 4 | 8 | 1 | 2 | 1 |

### Commands

Fields are given like on a chess board: a letter for the column and a number for the row, e.g. `C2`. Upper and lower case are both accepted.

| Command | Description |
|---|---|
| `move C2 C3` | Moves the troop on C2 to C3 |
| `info C2` | Shows the stats of the troop on C2 |
| `help` | Lists all commands |
| `quit` | Ends the game |

### Movement Rules

A troop can only move if

- the target field is on the board,
- the target field is empty,
- the target is within its movement range (diagonal steps count as one field).

If a move is not allowed, the game shows the reason and you can try again.

## Current State

| Feature | Status |
|---|---|
| Board, troops and starting setup | Done |
| Movement with rule checks | Done |
| Text user interface | Done |
| Combat (melee and ranged) | Planned |
| Turns and win condition | Planned |
| Terrain | Planned |
| Buildings to train and upgrade troops | Planned |
| Graphical user interface with pixel art tiles | Planned |

## Development

### Architecture

Crownfall uses a layered MVC architecture with the observer pattern. The model is immutable. The layer rules are checked automatically by tests.

```
src/main/scala/crownfall/
├── model/        Game data and rules (immutable)
├── controller/   Current game state, commands, notifies the views
├── aview/tui/    Text user interface: rendering, input parsing, game loop
├── util/         Generic building blocks (observer pattern)
└── Main.scala    Creates and connects all components
```

Details: [docs/architecture.md](docs/architecture.md)

### Tests and Quality

```
sbt test
```

Every push and pull request is checked automatically by GitHub Actions:

- **Tests and coverage**, reported to Coveralls
- **Static analysis** with SonarQube Cloud
- **Mutation testing** with Stryker4s (on `main`)

The quality rules and all commands to run the checks locally are described in [docs/quality.md](docs/quality.md).

### Workflow

- `main` only contains released versions. Every release is tagged, e.g. `v0.3.0`.
- `develop` collects finished features.
- New work happens in `feature/*` branches and is merged into `develop` through a pull request.

Version numbers follow [Semantic Versioning](https://semver.org/). Versions below `1.0.0` are still in development.

## Team

- Mamoon Akhter
- Carlos Burkart
