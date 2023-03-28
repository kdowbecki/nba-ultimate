package nba.ultimate

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class PlayersCsvReaderTest {

  @Test
  fun `should read csv`() {
    val small = PlayersCsvReader("/small.csv").readPlayerNamesById()
    assertContains(small, 1, "Joe Bloggs")
    assertContains(small, 2, "Jane Bloggs")
  }

  @Test
  fun `should not open missing resource`() {
    val ex = assertFailsWith<IllegalArgumentException> {
      PlayersCsvReader("missing.csv").readPlayerNamesById()
    }
    assertEquals("Failed to open missing.csv for reading", ex.message)
  }


}