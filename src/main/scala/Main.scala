@main def hello(): Unit =
  val grid =
    """
      |   A B C D E F G H
      |  . . . . . . . . .
      |1 . . . . . . . . .
      |2 . . . . . . . . .
      |3 . . . . . . . . .
      |4 . . . . . . . . .
      |5 . . . . . . . . .
      |6 . . . . . . . . .
      |7 . . . . . . . . .
      |8 . . . . . . . . .""".stripMargin

  println("Willkommen im Spiel")
  println(grid)