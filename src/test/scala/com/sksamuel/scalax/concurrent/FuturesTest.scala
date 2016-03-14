package com.sksamuel.scalax.concurrent

import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class FuturesTest extends WordSpec with Matchers {

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
