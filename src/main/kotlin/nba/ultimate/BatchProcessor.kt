package nba.ultimate

import mu.KotlinLogging

private val log = KotlinLogging.logger {}


abstract class BatchProcessor<T, V> {

  open fun process(batch: List<T>) {
    val result = transform(batch)
    if (result.isNotEmpty()) {
      save(result)
    }
  }

  abstract fun transform(batch: List<T>): List<V>

  abstract fun save(batch: List<V>)

}


abstract class ProgressLoggingBatchProcessor<T, V>(
  private var current: Int,
  private val total: Int
) : BatchProcessor<T, V>() {

  override fun process(batch: List<T>) {
    if (batch.isEmpty()) {
      logProgress { "Empty batch" }
      return
    }

    logProgress { "Transforming batch of ${batch.size} size" }
    val result = transform(batch)
    if (result.isEmpty()) {
      logProgress { "Empty results" }
      return
    }

    logProgress { "Saving results of ${result.size} size" }
    save(result)
    logProgress { "Saved results" }

    recordProgress()
  }

  protected fun logProgress(message: () -> Any?) {
    // TODO Is message.invoke() the right pattern to avoid String creation when INFO level is disabled?
    log.info { "[${progress()}] [$current] ${message.invoke().toString()}" }
  }

  private fun progress(): String {
    val progress = 100f * current / total
    return "%.2f%%".format(progress)
  }

  private fun recordProgress() {
    current++
  }

}
