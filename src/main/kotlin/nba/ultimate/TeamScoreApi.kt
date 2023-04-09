package nba.ultimate

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


private val log = KotlinLogging.logger {}


// TODO On Java 11 HttpClient fails somewhere between 100-200 concurrent HTTP/2 requests, I wonder if
//   Apache HttpClient 5 would be more stable?

/**
 * Api for team scores written with default JDK HttpClient.
 */
class TeamScoreApi(private val baseUrl: String) {

  companion object {
    const val scoresPath = "v1/scores"
  }

  private val client = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .build()

  private val bodyToString = HttpResponse.BodyHandlers.ofString()

  fun getTeamScore(team: Team): TeamScore {
    val uri = URI.create("$baseUrl/$scoresPath/${team.id}")
    val request = HttpRequest.newBuilder(uri).build()
    val resp = client.send(request, bodyToString)
    val score = parseScore(resp.body())
    log.info { "Got team $team score $score" }
    return TeamScore(team, score)
  }

  private fun parseScore(body: String): Float {
    // The JSON is in [[{"score":967.29}]] format
    val data = Json.decodeFromString<List<List<Score>>>(body)
    return data[0][0].score
  }

}

@Serializable
private data class Score(val score: Float)
