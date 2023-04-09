# NBA Ultimate with Kotlin

In the 2021-22 season the NBA announced its
[75th Anniversary Team](https://en.wikipedia.org/wiki/NBA_75th_Anniversary_Team). They also created a now defunct
[NBA Ultimate fantasy draft website](https://web.archive.org/web/20220221122703/https://www.nba.com/scout/ultimate/)
allowing to select a five player team.

Because there are 76 available players there are $\binom{76}{5} = 18,474,840$ possible teams.

The algorithm deciding how a team is assigned a score was never publicized. People had to try out different
teams to see what happens. For example a team:

```
Jason Kidd + Steve Nash + Kareem Abdul-Jabbar + Elgin Baylor + Wilt Chamberlain = 994.15
```

can be improved by swapping [Elgin Baylor](https://en.wikipedia.org/wiki/Elgin_Baylor)
with [Paul Arizin](https://en.wikipedia.org/wiki/Paul_Arizin):

```
Jason Kidd + Steve Nash + Kareem Abdul-Jabbar + Paul Arizin + Wilt Chamberlain = 994.31
```

Around the same time I was learning Kotlin. As a practical exercise I wrote a small command
line application that checks public team score API, using HTTP/2, and saves results into a SQLite database. This
allows to compare the team score faster.

## What this project is

A simple application to learn Kotlin with few basic libraries.

It allowed to experiment with Kotlin ecosystem and discover new concepts like coroutines. It was fun to see how
common Java libraries like SLF4j or Wiremock fit into Kotlin.

## What this project isn't

A perfect example how to write and test Kotlin code.

It was never a goal to write full unit tests, for example database layer is not tested.

It was never a goal to apply design patterns, the code follows
[KISS principle](https://en.wikipedia.org/wiki/KISS_principle).
