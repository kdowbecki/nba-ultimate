package nba.ultimate

import io.mockk.*
import kotlin.test.Test


class TeamBatchProcessorTest {

  private val dao = mockk<TeamScoreDao>()
  private val api = mockk<TeamScoreApi>()

  @Test
  fun `should read from api and save with dao`() {
    val team = mockk<Team>()
    val teamScore = mockk<TeamScore>()

    every { api.getTeamScore(team) } returns teamScore
    every { dao.hasTeamScore(team) } returns false
    justRun { dao.batchInsertTeamScores(listOf(teamScore)) }

    val processor = TeamBatchProcessor(1, 1, dao, api)
    processor.process(listOf(team))

    verify(exactly = 1) { api.getTeamScore(team) }
    verify(exactly = 1) { dao.hasTeamScore(team) }
    verify(exactly = 1) { dao.batchInsertTeamScores(listOf(teamScore)) }
    confirmVerified(dao, api)
  }

}