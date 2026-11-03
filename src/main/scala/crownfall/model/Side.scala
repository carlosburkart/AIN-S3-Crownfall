package crownfall.model

enum Side:
  case One, Two
  def opponent: Side = this match
    case One => Two
    case Two => One
