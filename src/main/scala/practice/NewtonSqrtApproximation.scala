package com.dnikitin
package practice

import scala.annotation.tailrec
import scala.math.abs

object NewtonSqrtApproximation {
  /*
  Main idea is start from basic estimation (for example y = 1)
  and then calculating mean(y, x/y)
  for example x = 2
  Estimation | Quotient     | Mean
  1          | 2 / 1 = 2    |
   */

  def sqrt(x: Double): Double = sqrtIter(1.0, x)


  @tailrec
  private def sqrtIter(estimated: Double, x: Double): Double = {
    if isGoodEnough(estimated, x) then estimated
    else sqrtIter(improveEstimation(estimated, x), x)
  }

  private def isGoodEnough(estimated: Double, real: Double): Boolean =
    abs(estimated * estimated - real) < 0.000001

  private def improveEstimation(oldEstimation: Double, real: Double): Double =
    (oldEstimation + real / oldEstimation) / 2



  def main(args: Array[String]): Unit = {
    println(s"math sqrt: ${math.sqrt(2)}")
    println(s"my sqrt: ${sqrt(2)}")
  }

}
