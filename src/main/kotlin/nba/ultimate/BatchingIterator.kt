package nba.ultimate

/**
 * Splits results of [Iterator] into batches.
 */
class BatchingIterator<T>(private val iterator: Iterator<T>, private val batchSize: Int) : Iterator<List<T>> {

  override fun hasNext() = iterator.hasNext()

  override fun next(): List<T> {
    val batch = mutableListOf<T>()
    for (i in 1..batchSize) {
      batch.add(iterator.next())
      if (!iterator.hasNext()) {
        break
      }
    }
    return batch
  }

}