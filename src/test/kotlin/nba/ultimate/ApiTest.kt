package nba.ultimate

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

const val httpsPort = 8443;

@WireMockTest(httpsEnabled = true, httpsPort = httpsPort)
class ApiTest {

  @Test
  fun `should read team score async`() {
    stubFor(
      get(urlPathEqualTo("/v1/scores/1-2-3-4-5"))
        .willReturn(
          okJson(
            """
            [[{"score":967.29}]]
            """.trimIndent()
          )
        )
    )

    val baseUrl = "https://localhost:${httpsPort}"
    val api = TeamScoresApi(baseUrl)

    val future = api.asyncTeamScore(Team(intArrayOf(1, 2, 3, 4, 5)))
    val result = future.get()
    assertEquals(BigDecimal("967.29"), result.score)
  }

}