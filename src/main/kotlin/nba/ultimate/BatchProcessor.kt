package nba.ultimate

import mu.KotlinLogging

private val log = KotlinLogging.logger {}


abstract class BatchProcessor<T, V> {

  open fun process(batch: List<T>) {
    val transformed = transform(batch)
    save(transformed)
  }

  abstract fun transform(batch: List<T>): List<V>

  abstract fun save(batch: List<V>)

}


abstract class ProgressLoggingBatchProcessor<T, V>(
  private var current: Int,
  private val total: Int
) : BatchProcessor<T, V>() {

  override fun process(batch: List<T>) {
    logProgress { "Staring batch" }
    logProgress { "Transforming batch of ${batch.size} size" }
    val transformed = transform(batch)
    logProgress { "Saving batch of ${transformed.size} size" }
    save(transformed)
    logProgress { "Finished batch" }
    current++
  }

  protected fun logProgress(message: () -> Any?) {
    // TODO Is message.invoke() the right pattern to avoid String creation when INFO level is disabled?
    log.info { "[${progress()}] [$current] ${message.invoke().toString()}" }
  }

  private fun progress(): String {
    val progress = 100f * current / total
    return "%.2f%%".format(progress)
  }

}
