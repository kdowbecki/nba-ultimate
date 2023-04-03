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

private val teamScoresApi = TeamScoresApi(baseUrl)

fun main() {
  val totalBatches = ceil(Teams.count.toDouble() / batchSize).toInt()

  TeamScoreDao(databasePath).use { teamScoreDao ->
    val batchIterator = BatchingIterator(Teams.iterator(), batchSize)
    batchIterator.skipBatches(batchNumResume)
    var batch = batchNumResume
    batchIterator.forEach { teams ->
      // TODO this where we want to start coroutines, to async check the team before making an HTTP call
      val teamsWithNoScore = teams.filter { teamScoreDao.hasNoTeamScore(it) }
      if (teamsWithNoScore.isEmpty()) {
        logProgress(totalBatches, batch, "Empty batch $batch")
      } else {
        logProgress(totalBatches, batch, "Starting batch $batch")

        process(teams, teamScoreDao)

        Thread.sleep(batchSleepMillis)
      }
      batch++
    }
  }
}


private fun process(teams: List<Team>, teamScoreDao: TeamScoreDao) {
  val futures = teams
    .map { teamScoresApi.asyncTeamScore(it) }
    .toTypedArray()
  CompletableFuture.allOf(*futures).get(batchTimeoutMillis, TimeUnit.MILLISECONDS)
  val teamScores = futures.map { it.get() }
  teamScoreDao.batchInsertTeamScores(teamScores)
}

private fun logProgress(total: Int, current: Int, message: String) {
  log.info { "[${"%.2f%%".format(current.toDouble() * 100 / total.toDouble())}] $message" }
}

