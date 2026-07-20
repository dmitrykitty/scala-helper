package com.dnikitin
package com.dnikitin.async

import scala.util.{Failure, Success, Try}

object Async3 {

  /*
   * A callback receives the result of an operation.
   *
   * The result can be:
   * - Success(value), when the operation succeeds,
   * - Failure(exception), when the operation fails.
   */
  private type Callback[T] = Try[T] => Unit

  /*
   * Async[T] is a function that accepts a callback.
   *
   * Creating an Async value does not execute the operation.
   * The operation starts only when the Async function is called
   * and a callback is passed to it.
   */
  private type Async[T] = Callback[T] => Unit


  class Device {

    /*
     * getMlt does not return an Int directly.
     * It returns an Async[Int], which is a function waiting for a callback.
     *
     * Execution order:
     * 1. getMlt returns the function below.
     * 2. The caller passes a callback to that function.
     * 3. The function performs the operation.
     * 4. The callback receives Success(2).
     */
    //similar to
    //    def getMlt: Callback[Int] => Unit = {
    //
    //      val returnedFunction: Callback[Int] => Unit =
    //        (callback: Callback[Int]) => {
    //          println("getMlt before callback")
    //          callback(Success(2))
    //        }
    //
    //      returnedFunction
    //    }
    def getMlt: Async[Int] =
      callback => {
        println("getMlt before callback")

        // Simulate a successfully completed operation.
        callback(Success(2))
      }

    def setParamExplicit(value: Int): Async[Unit] = {
      val operation: Async[Unit] =
        (callback: Callback[Unit]) => {
          println("setParam before callback")
          // Notify the caller that the write operation succeeded.
          callback(Success(())) //Callback[Unit] == Try[Unit] => Unit
        }
      operation
    }

    /*
     * setParam returns an Async[Unit].
     *
     * Unit means that the operation does not produce a useful value.
     * Success(()) only informs the caller that the operation finished
     * successfully.
     */
    def setParam(value: Int): Async[Unit] =
      callback => {
        println("setParam before callback")

        // Notify the caller that the write operation succeeded.
        callback(Success(()))
      }


  }


  /*
   * runBusinessLogic also returns an Async value.
   *
   * At this point, the business logic is only described.
   * It starts when the returned function receives finalCallback.
   *
   * Execution order after the operation is started:
   * 1. Run getMlt.
   * 2. Wait for its callback.
   * 3. If getMlt fails, pass the error to finalCallback.
   * 4. If getMlt succeeds, calculate newValue.
   * 5. Run setParam(newValue).
   * 6. Wait for its callback.
   * 7. If setParam fails, pass the error to finalCallback.
   * 8. If setParam succeeds, pass Success(newValue) to finalCallback.
   */
  def runBusinessLogic(device: Device, param: Int): Async[Int] =
    finalCallback => {
      println("runLogic before callback")

      /*
       * device.getMlt returns an Async[Int].
       * The block below is immediately passed as its callback,
       * which starts the getMlt operation.
       */
      device.getMlt { getResult =>
        println("getMlt callback")

        getResult match {
          case Failure(cause) =>
            /*
             * The multiplier could not be obtained,
             * so no further operation can be performed.
             */
            finalCallback(Failure(cause))

          case Success(multiplier) =>
            /*
             * The multiplier is available, so we can calculate
             * the value that should be written to the device.
             */
            val newValue = param * multiplier

            /*
             * setParam returns an Async[Unit].
             * The block below is passed as its callback,
             * which starts the write operation.
             */
            device.setParam(newValue) { setResult =>
              println("setParam callback")

              setResult match {
                case Failure(cause) =>
                  /*
                   * The write operation failed,
                   * so the whole business operation fails.
                   */
                  finalCallback(Failure(cause))

                case Success(_) =>
                  /*
                   * The write operation succeeded.
                   * The Unit value is ignored, and the calculated
                   * newValue is returned through finalCallback.
                   */
                  finalCallback(Success(newValue))
              }
            }
        }
      }
    }


  def main(args: Array[String]): Unit = {
    val device = new Device()

    /*
     * runBusinessLogic returns an Async[Int].
     *
     * This creates the complete operation, but its body has not started yet.
     * No getMlt or setParam code is executed at this point.
     */
    val program: Async[Int] =
      runBusinessLogic(device, 12)

    /*
     * Calling program and passing the final callback starts
     * the entire chain of operations.
     *
     * The execution order is:
     *
     * program
     *   -> getMlt
     *   -> getMlt callback
     *   -> setParam
     *   -> setParam callback
     *   -> final callback below
     */
    program {
      case Failure(_) =>
        println("runLogic failure callback")

      case Success(_) =>
        println("runLogic callback")
    }
    //similar to
    //    program {
    //      result =>
    //        result match {
    //          case Failure(_) => println("runLogic failure callback")
    //          case Success(_) => println("runLogic callback")
    //        }
    //    }
  }
}

