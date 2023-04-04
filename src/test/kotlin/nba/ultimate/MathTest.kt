package nba.ultimate

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals


class MathTest {

  @TestFactory
  fun `should calculate binomial coefficient`() = listOf(
    Binom(1, 2, 0),
    Binom(2, 1, 2),
    Binom(4, 2, 6),
    Binom(22, 5, 26_334)
  ).map {
    dynamicTest("${it.n} over ${it.k} should be ${it.value}") {
      assertEquals(it.value, binomialCoefficient(it.n, it.k))
    }
  }

}

private data class Binom(val n: Int, val k: Int, val value: Int)
