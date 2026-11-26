package crownfall.model

enum GameError:
  case NoTroopThere, OutOfBounds, FieldOccupied, OutOfRange   // move
  case NoTarget, OwnTroop                                     // attack
  case NotYourTroop, GameIsOver                               // turns
  case NothingToUndo, NothingToRedo                           // undo
  case InvalidDamage                                          // damage strategy