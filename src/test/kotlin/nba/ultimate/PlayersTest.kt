package nba.ultimate

import kotlin.test.Test
import kotlin.test.assertEquals


class PlayersTest {

  @Test
  fun `should read Tim Duncan`() {
    assertEquals("Tim Duncan", Players.namesById[1495])
  }

  @Test
  fun `should have as many names as ids`() {
    assertEquals(Players.namesById.size, Players.idsSorted.size)
  }

}