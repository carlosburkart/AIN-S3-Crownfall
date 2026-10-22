package crownfall.model

class PositionSuite extends munit.FunSuite:

  test("Zwei Positionen mit gleichen Werten sind gleich") {
    val pos1 = Position(2, 3)
    val pos2 = Position(2, 3)
    assertEquals(pos1, pos2)
  }

  test("Abstand zu sich selbst ist 0") {
    val pos = Position(2, 3)
    val dis = pos.distanceTo(pos)
    assertEquals(dis, 0)
  }

  test("Distanz-Kontrollrechnungen") {
    val pos1 = Position(0, 3)
    val pos2 = Position(3, 5)
    val pos3 = Position(1, 7)
    val dis12 = pos1.distanceTo(pos2)
    val dis13 = pos1.distanceTo(pos3)
    assertEquals(dis12, 3)
    assertEquals(dis13, 4)
  }

  test("Abstand ist in beide Richtungen gleich") {
    val posA = Position(2, 3)
    val posB = Position(4, 6)
    val disAB = posA.distanceTo(posB)
    val disBA = posB.distanceTo(posA)
    assertEquals(disAB, disBA)
  }

  test("Genau 8 Nachbarn") {
    val pos = Position(1, 1)
    val neigh = pos.neighbors
    assertEquals(neigh.size, 8)
  }

  test("Position ist selbst nicht unter den Nachbarn") {
    val pos = Position(1, 1)
    val neigh = pos.neighbors
    assert(!neigh.contains(pos))
  }

  test("Nachbarn von (1, 1) sind genau die erwarteten") {
    val pos = Position(1, 1)
    val neigh = pos.neighbors
    val neighSet = Set(
      Position(0, 0), Position(0, 1), Position(0, 2),
      Position(1, 0),                 Position(1, 2),
      Position(2, 0), Position(2, 1), Position(2, 2)
    )
    assertEquals(neigh.toSet, neighSet)
  }