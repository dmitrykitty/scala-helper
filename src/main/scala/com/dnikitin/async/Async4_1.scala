package com.dnikitin
package com.dnikitin.async

object Async4_1 {

  import scala.util.{Failure, Success, Try}

  type Callback[-T] = Try[T] => Unit


  // ======================================================
  // PROPER ASYNC TYPE WITH MAP / FLATMAP
  // ======================================================

  case class AsyncTask[+T](runT: Callback[T] => Unit) {

    //return AsyncTask, so we creating it by passing lambda to constructor { }
    //for example AsynTask[Int] => AsyncTask[String] by passing num => num.toString
    //so it basically returns task which in some time will return S

    //AsyncTask is case class so it has method apply defined
    //and by writing AsyncTask {...} we basically call AsyncTask.apply(...)
    def map[S](f: T => S): AsyncTask[S] = {
      AsyncTask {
        //here is new run: Callback[S] => Unit function
        (callbackForS: Callback[S]) =>
          val callbackForT: Callback[T] = {
            //we can explicitly create tryS
            //val tryS: Try[S] = tryT.map(f)
            //and then call callbackForS(tryS)

            //map is method from Try, not current map. No recursive call!!!!
            (tryT: Try[T]) => callbackForS(tryT.map(f))
          }
          //it's call of run from AsyncTask[T]. It started only after
          this.runT(callbackForT)
      }
    }


    def flatMap[S](f: T => AsyncTask[S]): AsyncTask[S] =
      AsyncTask { (callbackForS: Callback[S]) =>

        val callbackForT: Callback[T] = {
          case Success(t: T) =>
            Try(f(t)) match {
              case Success(taskS: AsyncTask[S]) =>
                taskS.runT(callbackForS)

              case Failure(cause) =>
                callbackForS(Failure(cause))
            }

          case Failure(cause) =>
            callbackForS(Failure(cause))
        }

        runT(callbackForT)
      }
  }


  object AsyncTask {

    def pure[T](value: T): AsyncTask[T] =
      AsyncTask(callback => callback(Success(value)))


    def eval[T](expr: => T): AsyncTask[T] =
      AsyncTask(callback => callback(Try(expr)))


    def failed[T](cause: Throwable): AsyncTask[T] =
      AsyncTask(callback => callback(Failure(cause)))
  }


  trait AsyncTaskDeviceProtocolApi {

    // Blocking API used to communicate with the device.

    def getParam(name: String): AsyncTask[Int]

    def setParam(name: String, value: Int): AsyncTask[Unit]
  }


  object AsyncTaskSleeper {

    def sleep(millis: Long): AsyncTask[Unit] = ???
  }


  // ======================================================
  // VERSION USING FLATMAP AND MAP
  // ======================================================

  def flatMapAsyncTaskBusinessLogic(
                                     diwajs: AsyncTaskDeviceProtocolApi
                                   ): AsyncTask[Int] = {

    val paramName = "counter"

    diwajs.getParam(paramName).flatMap { value =>
      AsyncTaskSleeper.sleep(1000).flatMap { _ =>
        val newValue = value + 1

        diwajs.setParam(paramName, newValue).map { _ =>
          newValue
        }
      }
    }
  }


  // ======================================================
  // VERSION USING FOR-COMPREHENSION
  // ======================================================

  def forComprehensionAsyncTaskBusinessLogic(
                                              diwajs: AsyncTaskDeviceProtocolApi
                                            ): AsyncTask[Int] =
    for {
      paramName <- AsyncTask.pure("counter")
      value <- diwajs.getParam(paramName)
      _ <- AsyncTaskSleeper.sleep(1000)
      newValue = value + 1
      _ <- diwajs.setParam(paramName, newValue)
    } yield newValue

}
