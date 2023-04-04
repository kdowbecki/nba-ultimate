package nba.ultimate

/**
 * Fast binomial coefficient for results fitting into [Int].
 *
 * Binomial coefficient grows very fast even for small n. This function only supports results that fit into [Int],
 * avoiding [java.math.BigInteger] math.
 *
 * @see <a href="https://blog.plover.com/math/choose.html">How to calculate binomial coefficients</a>
 */
fun binomialCoefficient(n: Int, k: Int): Int {
  if (k > n) return 0
  var ni = n
  var r = 1
  for (ki in 1..k) {
    r *= ni--
    r /= ki
  }
  return r
}
