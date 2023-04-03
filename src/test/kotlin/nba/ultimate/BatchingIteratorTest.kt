package nba.ultimate

import kotlin.test.Test
import kotlin.test.assertContentEquals


class BatchingIteratorTest {

  @Test
  fun `should batch iteration`() {
    val numbers = 1..7
    val it = BatchingIterator(numbers.iterator(), 3)
    assertContentEquals(1..3, it.next())
    assertContentEquals(4..6, it.next())
    assertContentEquals(7..7, it.next())
  }

  @Test
  fun `should skip batches`() {
    val numbers = 1..12
    val it = BatchingIterator(numbers.iterator(), 3)
    it.skipBatches(3)
    assertContentEquals(10..12, it.next())
  }

}