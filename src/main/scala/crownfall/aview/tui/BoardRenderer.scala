package crownfall.aview.tui

import crownfall.model.*

object BoardRenderer:
  def symbol(troop: Troop): String =
    val letter = troop.kind match
      case TroopKind.Knight => "K"
      case TroopKind.Archer => "A"
      case TroopKind.Mage   => "M"
    if troop.owner == Side.One then letter else letter.toLowerCase

  def coloredSymbol(troop: Troop): String =
    val color = if troop.owner == Side.One then Console.BLUE else Console.RED
    color + symbol(troop) + Console.RESET

  def troopInfo(troop: Troop): String =
    val kind = troop.kind
    val owner = troop.owner
    val hp = troop.hp
    val maxHp = troop.kind.maxHp
    val atk = troop.kind.attack
    val df = troop.kind.defense
    val rng = troop.kind.range
    val mov = troop.kind.movement
    s"$kind (Side $owner) | HP $hp/$maxHp | ATK $atk | DEF $df | RNG $rng | MOV $mov"
    
  def legend: String =
    TroopKind.values
      .map(kind => s"${symbol(Troop.create(0, Side.One, kind))} = $kind")
      .mkString(", ")
      
  def stateInfo(state: GameState): String = state match
    case Turn(side) => s"Side $side, it's your turn."
    case GameOver(winner) => s"Side $winner wins the game!"

  def errorMessage(error: GameError): String = error match
    case GameError.NoTroopThere  => "No troop available on this field."
    case GameError.OutOfBounds   => "You can't move troops outside the board."
    case GameError.FieldOccupied => "Another troop occupies this field."
    case GameError.OutOfRange    => "The target field is out of range."
    case GameError.NoTarget      => "There is no troop to attack on this field."
    case GameError.OwnTroop      => "You can't attack your own troops."
    case GameError.NotYourTroop  => "This troop belongs to the other player."
    case GameError.GameIsOver    => "The game is already over."