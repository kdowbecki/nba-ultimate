package nba.ultimate

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import kotlinx.serialization.SerializationException
import org.junit.jupiter.api.BeforeEach
import java.util.concurrent.ExecutionException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


@WireMockTest(httpsEnabled = true)
class TeamScoreApiTest {

  private val team = Team(intArrayOf(1, 2, 3, 4, 5))

  private lateinit var teamScoreApi: TeamScoresApi

  @BeforeEach
  fun setUp(info: WireMockRuntimeInfo) {
    info.wireMock.resetMappings()
    teamScoreApi = TeamScoresApi("https://localhost:${info.httpsPort}")
  }

  @Test
  fun `should read team score async`() {
    stubTeamScore(team, 900.01f)

    val result = teamScoreApi.asyncTeamScore(team).get()
    assertEquals(900.01f, result.score)
  }

  @Test
  fun `should fail on invalid response`() {
    stubTeamScore(team, "THIS IS NOT VALID JSON")

    val ex = assertFailsWith<ExecutionException> { teamScoreApi.asyncTeamScore(team).get() }
    assertTrue { ex.cause is SerializationException }
  }

  private fun stubTeamScore(team: Team, score: Float) {
    stubTeamScore(
      team,
      """
      [[{"score":${score}}]]
      """.trimIndent()
    )
  }

  private fun stubTeamScore(team: Team, json: String) {
    stubFor(
      get(urlPathEqualTo("/v1/scores/${team.id}"))
        .willReturn(okJson(json))
    )
  }

}