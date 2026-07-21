package com.dnikitin
package com.dnikitin.async

object Async3_1 {

  import scala.util.{Failure, Success, Try}

  //function taking Success(T) or Failure(exception)
  type Callback[-T] = Try[T] => Unit

  //type Async[T] = (Try[T] => Unit) => Unit
  //it's function taking callback(another function) and => Unit
  type Async[+T] = Callback[T] => Unit

  trait AsyncDeviceProtocolApi {

    // Blocking API used to communicate with the device.

    //val getParamOperation: Async[Int] = getParam("counter") - getParamOperation - function taking callback
    //getParamOperation(getParamCallback)
    def getParam(name: String): Async[Int]

    def setParam(name: String, value: Int): Async[Unit]
  }

  class Device extends AsyncDeviceProtocolApi {
    private var counter: Int = 10

    override def getParam(name: String): Async[Int] =
      callback => {
        println("Before getParam callback call")
        callback(Success(counter))
      }

    override def setParam(name: String, value: Int): Async[Unit] =
      callback => {
        println("Before setParam callbackCall")
        counter = value
        callback(Success(()))
      }
  }

  object AsyncSleeper {

    def sleep(millis: Long): Async[Unit] =
      callback => {
        /*
         * This version blocks the current thread.
         * It is sufficient for understanding the callback flow,
         * but a production implementation should use a scheduler.
         */
        Try(Thread.sleep(millis)) match {
          case Success(_) =>
            callback(Success(()))

          case Failure(error) =>
            callback(Failure(error))
        }
      }
  }

  def asyncBusinessLogic(device: AsyncDeviceProtocolApi): Async[Int] = {
    //everything below is lambda of type Callback[Int] => Unit x ->
    callback => {
      val paramName = "counter"
      println("Before asyncBusinessLogic callback call")

      //important case: getParam(name) -> Async[Int] { Param to async function }
      //so first braces () are for getParam, {} are for returning function
      device.getParam(paramName) {
        //next import part: instead of case => we can write explicit lambda function
        //        (getParamResult: Try[Int]) => {
        //          getParamResult match {
        //            case Failure(cause) => callback(Failure(cause))
        //            case Success(value) => AsyncSleeper.sleep(1000)
        case Failure(cause) =>
          //result of getParam is Failure(cause) and now we can run callback passed by user
          callback(Failure(cause))

        case Success(value) =>
          AsyncSleeper.sleep(1000) {
            case Failure(cause) =>
              callback(Failure(cause))

            case Success(_) =>
              val newValue = value + 1

              device.setParam(paramName, newValue) {
                case Failure(cause) =>
                  callback(Failure(cause))

                case Success(_) =>
                  callback(Success(newValue))
              }
          }
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val device = new Device()
    //now program is only lambda Callback[Int] => Unit waiting for the passing param
    //we also can do it implicitly asyncBusinessLogic(device) { pass callback here }
    val program: Async[Int] = asyncBusinessLogic(device)
    program {
      case Success(value) => println("Final callback received value = " + value)
      case Failure(e) => println("Final callback received exception = " + e.getMessage)
    }
  }
}
