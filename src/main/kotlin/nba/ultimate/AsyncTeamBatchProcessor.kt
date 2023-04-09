package nba.ultimate

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking


class AsyncTeamBatchProcessor(
  current: Int, total: Int,
  private val dao: TeamScoreDao, private val api: TeamScoreApi
) :
  ProgressLoggingBatchProcessor<Team, TeamScore>(current, total) {

  override fun transform(batch: List<Team>): List<TeamScore> = runBlocking(IO) {
    batch.map { team ->
      async {
        if (!dao.hasTeamScore(team)) {
          api.getTeamScore(team)
        } else {
          null
        }
      }
    }
      // TODO await will re-throw the Deferred exception in main thread
      .awaitAll()
      .filterNotNull()
  }

  override fun save(batch: List<TeamScore>) {
    dao.batchInsertTeamScores(batch)
  }

}