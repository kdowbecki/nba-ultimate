package nba.ultimate


class TeamBatchProcessor(
  current: Int, total: Int,
  private val dao: TeamScoreDao, private val api: TeamScoreApi
) :
  ProgressLoggingBatchProcessor<Team, TeamScore>(current, total) {

  override fun transform(batch: List<Team>): List<TeamScore> {
    return batch
      .filter { team -> !dao.hasTeamScore(team) }
      .map { team -> api.getTeamScore(team) }
      .onEach { teamScore -> logProgress { "Team ${teamScore.team} scored ${teamScore.score}" } }
  }

  override fun save(batch: List<TeamScore>) {
    dao.batchInsertTeamScores(batch)
  }

}