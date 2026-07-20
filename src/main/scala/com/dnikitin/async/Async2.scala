package com.dnikitin
package com.dnikitin.async

object Async2 {
  class Device {

    /*
     * The function does not return the result directly.
     * Instead, it passes the result to a callback.
     */
    def getMlt(callback: Int => Unit): Unit = {
      println("getMLT before callback")

      // The operation has finished, so we pass the result to the callback.
      callback(2)
    }

    /*
     * setParam performs the operation and informs the callback
     * when it has finished.
     */
    def setParam(value: Int)(callback: Unit => Unit): Unit = {
      println("setMLT before callback")

      callback(())
    }
  }

  /*
   * The final result is passed to finalCallback
   * instead of being returned directly.
   */
  def runBusinessLogic(device: Device, param: Int)(finalCallback: Int => Unit): Unit = {

    println("runLogic before callback")

    device.getMlt { multiplier =>
      // Runs when getMlt provides its result.
      println("getMLT callback")

      val newValue = param * multiplier

      device.setParam(newValue) { _ =>
        // Runs when setParam finishes.
        println("setMLT callback")

        finalCallback(newValue)
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val device = new Device()

    runBusinessLogic(device, 12) { result =>
      println("runLogic callback")
    }
  }
}

