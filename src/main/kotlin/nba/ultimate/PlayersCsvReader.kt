package nba.ultimate

/**
 * Players reader for a two column CVS file.
 */
class PlayersCsvReader(private val classpathResource: String) {

  fun readPlayerNamesById(): Map<Int, String> {
    // TODO how is null getResourceAsStream() returned? Can I avoid requireNotNull with !!
    this::class.java.getResourceAsStream(classpathResource).use { input ->
      requireNotNull(input) { "Failed to open $classpathResource for reading" }

      return input.bufferedReader()
        .lineSequence()
        .map(this::parseLine)
        .toMap()
    }
  }

  private fun parseLine(line: String): Pair<Int, String> {
    val comma = line.indexOf(",")
    check(comma > 0) { "Failed to read player: $line" }

    val id = line.substring(0, comma).toInt()
    val name = line.substring(comma + 1)
    return Pair(id, name)
  }

}
