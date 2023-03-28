package nba.ultimate

import mu.KotlinLogging
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

const val databasePath = "./data/data.db"
const val baseUrl = "https://us-central1-nba-75-prod.cloudfunctions.net"
const val batchSize = 10
const val batchNumResume = 9_000
const val batchSleepMillis = 30_000L
const val batchTimeoutMillis = 300_000L

private val log = KotlinLogging.logger {}


fun main() {
  val teamScoresApi = TeamScoresApi(baseUrl)

  val totalBatches = ceil(Teams.count.toDouble() / batchSize).toInt()

  TeamScoreDao(databasePath).use { teamScoreDao ->
    var batchNum = 0
    val batchIterator = BatchingIterator(Teams.iterator(), batchSize)
    batchIterator.forEachRemaining { teams ->
      // resume from specific point
      if (batchNum++ < batchNumResume) {
        return@forEachRemaining
      }

      // ignore already processed teams within batch
      val teamsWithNoScore = teams.filter { teamScoreDao.hasNoTeamScore(it) }

      if (teamsWithNoScore.isEmpty()) {
        logProgress(totalBatches, batchNum, "Empty batch $batchNum")
      } else {
        logProgress(totalBatches, batchNum, "Starting batch $batchNum")

        val futures = teamsWithNoScore
          .map { teamScoresApi.asyncTeamScore(it) }
          .toTypedArray()
        CompletableFuture.allOf(*futures).get(batchTimeoutMillis, TimeUnit.MILLISECONDS)
        teamScoreDao.batchInsertTeamScores(futures.map { it.get() })

        Thread.sleep(batchSleepMillis)
      }
    }
  }
}

private fun logProgress(total: Int, current: Int, message: String) {
  log.info { "[${"%.2f%%".format(current.toDouble() * 100 / total.toDouble())}] $message" }
}

