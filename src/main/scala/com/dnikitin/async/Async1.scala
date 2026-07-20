package com.dnikitin
package com.dnikitin.async


// ======================================================
// STAGE 1
// Regular synchronous functions
// ======================================================

object Async1 {
  class Device {
    // This function returns a regular Int.
    // When it is called, the result is returned immediately.
    def getMlt: Int = {
      println("Device: return multiplier")
      2
    }

    // This function does not return a meaningful value.
    // Unit is the Scala equivalent of void in Java.
    def setParam(value: Int): Unit = {
      println(s"Device: set param = $value")
    }
  }

  /*
   * Our business logic.
   *
   * We want to:
   *
   * 1. get the multiplier,
   * 2. multiply the parameter,
   * 3. save the result,
   * 4. return the result.
   */

  def runBusinessLogic(device: Device, param: Int): Int = {
    // Get the multiplier.
    // getMlt immediately returns an Int.
    val multiplier = device.getMlt

    // Calculate the new value.
    val newValue = param * multiplier

    // Save the new value.
    device.setParam(newValue)

    // Return the final result.
    newValue
  }

  def main(args: Array[String]): Unit = {
    val device = new Device()
    val result = runBusinessLogic(device, 12)
    println(result)
  }
}
