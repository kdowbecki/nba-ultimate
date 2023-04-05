package nba.ultimate

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement


abstract class SqlLiteDatabase(databasePath: String) : AutoCloseable {

  protected val connection: Connection

  init {
    connection = DriverManager.getConnection("jdbc:sqlite:${databasePath}")
    connection.autoCommit = false
    connection.isValid(0)
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


class TeamScoreDao(databasePath: String) : SqlLiteDatabase(databasePath) {

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
        return rs.getInt(1) > 0
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
    }
  }

}
