package nba.ultimate

import io.mockk.*
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class AsyncTeamBatchProcessorTest {

  private val dao = mockk<TeamScoreDao>()
  private val api = mockk<TeamScoreApi>()

  @Test
  fun `should async read from api and sync save with dao`() {
    val team = mockk<Team>()
    val teamScore = mockk<TeamScore>()

    every { api.getTeamScore(team) } returns teamScore
    every { dao.hasTeamScore(team) } returns false
    justRun { dao.batchInsertTeamScores(listOf(teamScore)) }

    val asyncProcessor = AsyncTeamBatchProcessor(1, 1, dao, api)
    asyncProcessor.process(listOf(team))

    verify(exactly = 1) { api.getTeamScore(team) }
    verify(exactly = 1) { dao.hasTeamScore(team) }
    verify(exactly = 1) { dao.batchInsertTeamScores(listOf(teamScore)) }
    confirmVerified(dao, api)
  }

  @Test
  fun `should fail batch if one record throws exception`() {
    val team = mockk<Team>()
    val teamScore = mockk<TeamScore>()
    val teamScoreEx = RuntimeException("Can't get team score")

    every { api.getTeamScore(team) } returns teamScore andThenThrows teamScoreEx
    every { dao.hasTeamScore(team) } returns false

    val asyncProcessor = AsyncTeamBatchProcessor(1, 1, dao, api)

    val ex = assertFailsWith<RuntimeException> { asyncProcessor.process(listOf(team, team)) }
    assertSame(teamScoreEx, ex)

    coVerify(atLeast = 1, atMost = 2) { dao.hasTeamScore(team) }
    coVerify(atLeast = 1, atMost = 2) { api.getTeamScore(team) }
    confirmVerified(dao, api)
  }

}