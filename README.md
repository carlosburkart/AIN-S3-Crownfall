# Crownfall

[![Scala CI](https://github.com/carlosburkart/AIN-S3-Crownfall/actions/workflows/scala.yml/badge.svg)](https://github.com/carlosburkart/AIN-S3-Crownfall/actions/workflows/scala.yml)
[![Quality gate status](https://sonarcloud.io/api/project_badges/measure?project=carlosburkart_AIN-S3-Crownfall&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=carlosburkart_AIN-S3-Crownfall)
[![Coverage Status](https://coveralls.io/repos/github/carlosburkart/AIN-S3-Crownfall/badge.svg)](https://coveralls.io/github/carlosburkart/AIN-S3-Crownfall)
[![Mutation testing badge](https://img.shields.io/endpoint?style=flat&url=https%3A%2F%2Fbadge-api.stryker-mutator.io%2Fgithub.com%2Fcarlosburkart%2FAIN-S3-Crownfall%2Fmain)](https://dashboard.stryker-mutator.io/reports/github.com/carlosburkart/AIN-S3-Crownfall/main)

A turn-based strategy game for two players, written in Scala 3 for the Software Engineering course at HTWG Konstanz.

## Game Idea

Two armies of knights, archers and mages face each other on an 8×8 board. Similar to chess, the players move their troops across the board.

Unlike chess, every troop has stats such as hit points, attack, defense, range and movement, and some troops can attack from a distance.

The goal is to defeat the enemy army.

## Getting Started

### Requirements

To build the project from source, you need:

- JDK 21 or newer
- [sbt](https://www.scala-sbt.org/download/)

Scala, all libraries and the exact sbt version are defined in the project and downloaded automatically on the first build.

### Start the Game

Clone the repository and run Crownfall:

```bash
git clone https://github.com/carlosburkart/AIN-S3-Crownfall.git
cd AIN-S3-Crownfall
sbt run
```

### Download and Run

Crownfall can also be started without installing sbt.

Download `crownfall.jar` from the [GitHub Releases](https://github.com/carlosburkart/AIN-S3-Crownfall/releases) page.

**Requirements:**
- Java 21 or newer

Start the game with:

```bash
java -jar crownfall.jar
```

The JAR contains the application and its required Scala dependencies.

## How to Play

### The Board

```text
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
| `.` | Empty field |

Uppercase letters (blue) belong to side One, lowercase letters (red) to side Two.

### Troop Stats

| Troop | HP | Attack | Defense | Range | Movement |
|---|---|---|---|---|---|
| Knight | 10 | 4 | 5 | 1 | 1 |
| Archer | 6 | 6 | 2 | 3 | 1 |
| Mage | 4 | 8 | 1 | 2 | 1 |

### Commands

Fields are given like on a chess board: a letter for the column and a number for the row, e.g. `C2`.

Uppercase and lowercase letters are both accepted.

| Command | Description |
|---|---|
| `move C2 C3` | Moves the troop on C2 to C3 |
| `info C2` | Shows the stats of the troop on C2 |
| `help` | Lists all commands |
| `quit` | Ends the game |

### Movement Rules

A troop can only move if:

- The target field is on the board.
- The target field is empty.
- The target is within its movement range (diagonal steps count as one field).

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

Crownfall uses a layered MVC architecture with the observer pattern.

The model is immutable, and the layer rules are checked automatically by tests.

```text
src/main/scala/crownfall/
├── model/        Game data and rules (immutable)
├── controller/   Current game state, commands, notifies the views
├── aview/tui/    Text user interface: rendering, input parsing, game loop
├── util/         Generic building blocks (observer pattern)
└── Main.scala    Creates and connects all components
```

More information: [Architecture Documentation](docs/architecture.md)

### Tests and Quality

Run the tests locally:

```bash
sbt test
```

GitHub Actions automatically checks the project on pushes and pull requests.

The CI pipeline includes:

- **Cross-platform testing:** Tests run on Ubuntu and Windows.
- **Code coverage:** Coverage is reported to Coveralls.
- **Static analysis:** Code quality is checked using SonarQube Cloud.
- **Mutation testing:** Stryker4s is used on `main`.

SonarQube analysis and Coveralls reporting are executed only on Ubuntu to avoid duplicate reports.

More information: [Quality Documentation](docs/quality.md)

### Build Executable JAR

Crownfall uses `sbt-assembly` to create an executable JAR containing the application and its required dependencies.

Build the JAR locally:

```bash
sbt clean assembly
```

The generated file is located at:

```text
target/scala-3.9.0/crownfall.jar
```

Run it using:

```bash
java -jar target/scala-3.9.0/crownfall.jar
```

### Continuous Integration

The GitHub Actions workflow is defined in:

```text
.github/workflows/scala.yml
```

The build matrix runs tests on:

- `ubuntu-latest`
- `windows-latest`

Both operating systems are tested independently.

SonarQube and Coveralls run only on Ubuntu.

### Continuous Delivery

The release workflow is defined in:

```text
.github/workflows/release.yml
```

Whenever a version tag matching `v*` is pushed, GitHub Actions automatically:

1. Checks out the repository.
2. Sets up Java 21 and sbt.
3. Runs the automated tests.
4. Builds the executable `crownfall.jar` using `sbt-assembly`.
5. Creates a GitHub Release and uploads the JAR file.

For example, a version tag could be:

```text
v0.3.0
```

Release artifacts are available on the [GitHub Releases](https://github.com/carlosburkart/AIN-S3-Crownfall/releases) page after a successful release workflow.

### Git Workflow

The project follows a branch-based development workflow:

- `main` contains released versions.
- `develop` collects completed features.
- `feature/*` branches are used for development.
- Feature branches are merged into `develop` through pull requests.

Version numbers follow [Semantic Versioning](https://semver.org/).

Versions below `1.0.0` are still in development.

## Team

- Mamoon Akhter
- Carlos Burkart