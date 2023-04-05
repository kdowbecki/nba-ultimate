package nba.ultimate

import java.nio.file.Path
import kotlin.math.ceil

private val databasePath = Path.of("data", "data.db")

const val baseUrl = "https://us-central1-nba-75-prod.cloudfunctions.net"

const val batchSize = 10
const val batchCurrent = 9_000
const val batchSleepMillis = 10L
private val batchTotal = ceil(Teams.count.toFloat() / batchSize).toInt()


fun main() {
  val teamScoreApi = TeamScoreApi(baseUrl)

  val batchIterator = BatchingIterator(Teams.iterator(), batchSize)
  batchIterator.skipBatches(batchCurrent)

  TeamScoreDao(databasePath).use { teamScoreDao ->
    val processor = TeamBatchProcessor(batchCurrent, batchTotal, teamScoreDao, teamScoreApi)

    batchIterator.forEach { teams ->
      processor.process(teams)
      Thread.sleep(batchSleepMillis) // TODO Rate limiting should be inside TeamBatchProcessor
    }
  }
}



