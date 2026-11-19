package crownfall.model

trait DamageStrategy:
  def damage(attacker: Troop, defender: Troop): Int

object TrueDamage extends DamageStrategy:
  def damage(attacker: Troop, defender: Troop): Int =
    attacker.kind.attack

object RatioDamage extends DamageStrategy:
  def damage(attacker: Troop, defender: Troop): Int =
    attacker.kind.attack * 5 / (5 + defender.kind.defense)