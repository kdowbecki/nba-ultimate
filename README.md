# NBA Ultimate with Kotlin

In the 2021-22 season the NBA announced its
[75th Anniversary Team](https://en.wikipedia.org/wiki/NBA_75th_Anniversary_Team).
They also
created [NBA Ultimate fantasy draft website](https://web.archive.org/web/20220221122703/https://www.nba.com/scout/ultimate/)
allowing to select a five player team. Because there are 76 players available there are
$\binom{76}{5} = 18,474,840$ possible teams.

The algorithm deciding how a team is scored was never publicized. People had to try out different teams to see what
happens.

Around the same time I was learning Kotlin. As a practical exercise I wrote a small command line application
that checks public team score API using HTTP/2 and saves results into a SQLite database.

For example a team:

```
Jason Kidd + Steve Nash + Kareem Abdul-Jabbar + Elgin Baylor + Wilt Chamberlain = 994.15
```

can be improved by swapping Elgin Baylor with Paul Arizin:

```
Jason Kidd + Steve Nash + Kareem Abdul-Jabbar + Paul Arizin + Wilt Chamberlain = 994.31
```

# What this project is

A simple application to learn Kotlin with few basic libraries.

It allowed to experiment with Kotlin code and discover new concepts like coroutines. It was fun to see how
commonly used Java libraries like SLF4j or Wiremock fit into Kotlin.

# What this project isn't

A perfect example on how to write and test Kotlin code.

It was never a goal to apply design patterns, the approach was [KISS](https://en.wikipedia.org/wiki/KISS_principle).

It was never a goal to write full unit tests, for example database layer is not tested.

