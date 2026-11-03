# Crownfall

A turn-based strategy game for two players, written in Scala 3 for the Software Engineering course.

## Game Idea

Two armies of knights, archers and mages face each other on an 8×8 board. Similar to chess, the players move their troops across the board. Unlike chess, every troop has stats such as hit points, attack, defense, range and movement, and some troops can attack from a distance. The goal is to defeat the enemy army.


## Start the Game

```
sbt run
```

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

## Run the Tests

```
sbt test
```

With test coverage:

```
sbt clean coverage test coverageReport
```

The report can be found at `target/scala-<version>/scoverage-report/index.html`.

## Project Structure

```
src/main/scala/crownfall/
├── model/        Game logic: board, positions, troops, rules
└── aview/tui/    Text user interface: rendering, input parsing, game loop
```

## Team

- Mamoon Akhter
- Carlos Burkart