package crownfall.model

case class Troop(id: Int, owner: Side, kind: TroopKind, hp: Int):
  require(hp >= 0, "Current hit points cannot be negative")
  require(hp <= kind.maxHp, "Current hit points must not exceed maximum hit points.")

  def takeDamage(damage: Int): Troop =
    require(damage >= 0, "Damage cannot be negative")
    copy(hp = math.max(hp - damage, 0))

  def isDefeated: Boolean =
    hp == 0
    
object Troop:
  def create(id: Int, owner: Side, kind: TroopKind): Troop =
    Troop(id, owner, kind, kind.maxHp)