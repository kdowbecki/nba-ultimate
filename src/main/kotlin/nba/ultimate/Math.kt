package nba.ultimate

/**
 * Binomial coefficients n over k
 *
 * @see <a href="https://blog.plover.com/math/choose.html">How to calculate binomial coefficients</a>
 */
fun binomialCoefficient(n: Int, k: Int): Int {
  if (k > n) return 0
  var ni = n
  var r = 1
  for (d in 1..k) {
    r *= ni--
    r /= d
  }
  return r
}