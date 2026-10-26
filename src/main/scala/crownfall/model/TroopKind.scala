package crownfall.model

enum TroopKind(val maxHp: Int, val attack: Int, val defense: Int, val range: Int, val movement: Int):
  case Knight extends TroopKind(10, 4, 5, 1, 1)
  case Archer extends TroopKind(6, 6, 2, 3, 1)
  case Mage extends TroopKind(4, 8, 1, 2, 1)