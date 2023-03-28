package nba.ultimate

import java.math.BigDecimal
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

// TODO This class can be converted from Java's CompletableFuture to Kotlin's coroutines.

// TODO On Java 11 HttpClient fails somewhere between 100-200 concurrent HTTP/2 requests, I wonder if
//   Apache HttpClient 5 would be more stable

/**
 * Api for team scores written with default JDK HttpClient.
 */
class TeamScoresApi(private val baseUrl: String) {

  companion object {
    const val scoresPath = "v1/scores"
  }

  private val client = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .build()

  private val bodyToString = HttpResponse.BodyHandlers.ofString()

  fun asyncTeamScore(team: Team): CompletableFuture<TeamScore> {
    val uri = URI.create("$baseUrl/$scoresPath/${team.id}")

    val request = HttpRequest.newBuilder()
      .uri(uri)
      .build()

    return client.sendAsync(request, bodyToString)
      .thenApply { parseScore(it.body()) }
      .thenApply { TeamScore(team, it) }
  }

  private fun parseScore(body: String): BigDecimal {
    try {
      // TODO what's the best way to map JSON to an object in Kotlin without external library?
      return body
        .filter { it.isDigit() || it == '.' || it == ',' }
        .toBigDecimal()
    } catch (ex: NumberFormatException) {
      throw ApiException("Unexpected score response body: $body", ex)
    }
  }

}


class ApiException(message: String, cause: Throwable) : RuntimeException(message, cause)
