package nba.ultimate

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class TeamTest {

  @Test
  fun `should not create a team of wrong size`() {
    val ex = assertFailsWith<IllegalStateException> {
      Team(intArrayOf(1, 2))
    }
    assertEquals("Expected 5 players but 2 given", ex.message)
  }

  @Test
  fun `should not create a team with duplicated players`() {
    val ex = assertFailsWith<IllegalStateException> {
      Team(intArrayOf(1, 2, 2, 3, 4))
    }
    assertEquals("Expected unique player in ascending order but [1, 2, 2, 3, 4] given", ex.message)
  }

  @Test
  fun `should have a team id`() {
    val team = Team(intArrayOf(1, 2, 3, 4, 5))
    assertEquals("1-2-3-4-5", team.id)
  }

  @Test
  fun `should use team id for toString`() {
    val team = Team(intArrayOf(1, 2, 3, 4, 5))
    assertEquals(team.id, team.toString())
  }

}


class TeamsTest {

  @Test
  fun `should iterate all possible teams`() {
    var total = 0
    Teams.iterator().forEachRemaining { total++ }
    assertEquals(Teams.count, total)
    println("TOTAL $total")
  }

}