package nba.ultimate

/**
 * Player constants and utilities.
 */
object Players {

  const val count = 76

  val namesById: Map<Int, String> = PlayersCsvReader("/players.csv").readPlayerNamesById()

  val idsSorted: List<Int> = namesById.keys.sorted()

  init {
    check(namesById.size == count) { "Expected $count players but ${namesById.size} read" }
  }

}
