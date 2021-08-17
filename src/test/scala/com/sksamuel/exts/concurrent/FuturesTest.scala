package com.sksamuel.exts.concurrent


import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FuturesTest extends AnyWordSpec with Matchers {

  import scala.concurrent.ExecutionContext.Implicits.global

  "Futures.firstThrowableOf" should {
    "return error from collection of futures" in {
      val f = Future.successful(1)
      val g = Future.failed(new RuntimeException("boom"))
      val h = Future.successful(2)
      Await.result(Futures.firstThrowableOf(Seq(f, g, h)), 1.minute).get.getMessage shouldBe "boom"
    }
  }
}
