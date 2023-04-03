package nba.ultimate

import org.apache.commons.math3.util.CombinatoricsUtils

/**
 * Team of players.
 */
class Team(val playerIds: IntArray) {

  val id = playerIds.joinToString("-")

  init {
    check(playerIds.size == Teams.size) { "Expected ${Teams.size} players but ${playerIds.size} given" }
    for (i in 1 until playerIds.size) {
      check(playerIds[i - 1] < playerIds[i]) {
        "Expected unique player in ascending order but ${playerIds.contentToString()} given"
      }
    }
  }

  override fun toString() = id

}


/**
 * Score of a team.
 */
class TeamScore(val team: Team, val score: Float) {

  override fun toString() = "$team $score"

}

/**
 * Team constants and utilities.
 */
object Teams {
  const val size = 5
  val count = binomialCoefficient(Players.count, size)

  fun iterator(): Iterator<Team> = AllPossibleTeamIterator()
}


private class AllPossibleTeamIterator : Iterator<Team> {

  // TODO avoid the skip altogether by generating a combination from a starting point
  private val combinations: MutableIterator<IntArray> =
    CombinatoricsUtils.combinationsIterator(Players.count, Teams.size)

  override fun hasNext() = combinations.hasNext()

  override fun next(): Team {
    val indexes = combinations.next()
    val playerIds = getPlayersIdsByIndex(indexes)
    return Team(playerIds)
  }

  private fun getPlayersIdsByIndex(indexes: IntArray) = IntArray(indexes.size) { i ->
    Players.idsSorted[indexes[i]]
  }

}