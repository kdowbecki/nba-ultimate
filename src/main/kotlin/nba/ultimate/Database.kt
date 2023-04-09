package nba.ultimate

import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import kotlin.io.path.notExists


private val log = KotlinLogging.logger {}


abstract class SqlLiteDatabase(databasePath: Path) : AutoCloseable {

  protected val connection: Connection

  private val initPath = Path.of("data", "init.db")

  init {
    initialize(databasePath)
    connection = DriverManager.getConnection("jdbc:sqlite:$databasePath")
    connection.autoCommit = false
    connection.isValid(0)
  }

  private fun initialize(databasePath: Path) {
    if (databasePath.notExists()) Files.copy(initPath, databasePath)
  }

  protected fun safeExecuteBatch(stmt: PreparedStatement, expected: Int) {
    val result = stmt.executeBatch()
    if (result.size != expected) {
      connection.rollback()
      throw IllegalStateException("Expected to insert $expected but inserted ${result.size} records")
    }
    connection.commit()
  }

  override fun close() {
    connection.close()
  }

}


class TeamScoreDao(databasePath: Path) : SqlLiteDatabase(databasePath) {

  fun hasTeamScore(team: Team): Boolean {
    val sql = """
      SELECT count(*) 
      FROM team_score 
      WHERE player_1 = ?
      AND player_2 = ?
      AND player_3 = ?
      AND player_4 = ?
      AND player_5 = ?
      """.trimIndent()
    connection.prepareStatement(sql).use { stmt ->
      var i = 1
      for (playerId in team.playerIds) {
        stmt.setInt(i++, playerId)
      }
      stmt.executeQuery().use { rs ->
        rs.next()
        val hasScore = rs.getInt(1) > 0
        if (hasScore) {
          log.info { "Team $team has score" }
        } else {
          log.info { "Team $team doesn't have score" }
        }
        return hasScore
      }
    }
  }

  fun batchInsertTeamScores(scores: List<TeamScore>) {
    val sql = """
      |INSERT INTO team_score(player_1, player_2, player_3, player_4, player_5, score) 
      |VALUES (?, ?, ?, ?, ?, ?)
      |""".trimMargin()
    connection.prepareStatement(sql).use { stmt ->
      for (score in scores) {
        var i = 1
        for (playerId in score.team.playerIds) {
          stmt.setInt(i++, playerId)
        }
        stmt.setFloat(i, score.score)
        stmt.addBatch()
      }
      safeExecuteBatch(stmt, scores.size)
      log.info { "Inserted ${scores.size} team scores" }
    }
  }

}
