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

private val totalBatches = ceil(Teams.count.toFloat() / batchSize).toInt()
private val teamScoresApi = TeamScoresApi(baseUrl)

fun main() {
  TeamScoreDao(databasePath).use { teamScoreDao ->
    val batchIterator = BatchingIterator(Teams.iterator(), batchSize)
    batchIterator.skipBatches(batchNumResume)
    var batch = batchNumResume
    batchIterator.forEach { teams ->
      // TODO this where we want to start coroutines, to async check the team before making an HTTP call
      val teamsWithNoScore = teams.filter { teamScoreDao.hasNoTeamScore(it) }
      if (teamsWithNoScore.isEmpty()) {
        log(batch, "Empty batch")
      } else {
        log(batch, "Starting batch")
        process(teams, teamScoreDao)
        Thread.sleep(batchSleepMillis)
      }

      batch++
    }
  }
}

private fun process(teams: List<Team>, dao: TeamScoreDao) {
  val futures = teams
    .map { teamScoresApi.asyncTeamScore(it) }
    .toTypedArray()
  CompletableFuture.allOf(*futures).get(batchTimeoutMillis, TimeUnit.MILLISECONDS)
  val teamScores = futures.map { it.get() }
  dao.batchInsertTeamScores(teamScores)
}

private fun log(batch: Int, message: String) {
  log.info {
    val progress = 100f * batch / totalBatches
    "[${"%.2f%%".format(progress)}] [$batch] $message"
  }
}

