package com.sksamuel.exts

import scala.concurrent.duration._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PrettyTimeTest extends AnyFunSuite with Matchers {

  test("pretty time hour minute second combinations") {
    PrettyTime(70.seconds) shouldBe "1m:10s"
    PrettyTime(170.seconds) shouldBe "2m:50s"
    PrettyTime(10.seconds) shouldBe "0m:10s"
    PrettyTime(1.seconds) shouldBe "0m:01s"
    PrettyTime(2.seconds) shouldBe "0m:02s"
    PrettyTime(2.hours + 2.minutes + 2.seconds) shouldBe "2h:02m:02s"
    PrettyTime(1.hour) shouldBe "1h:00m:00s"
    PrettyTime(60.seconds) shouldBe "1m:00s"
    PrettyTime(59.seconds) shouldBe "0m:59s"
    PrettyTime(180.seconds) shouldBe "3m:00s"
    PrettyTime(1.hour + 1.minute + 1.second) shouldBe "1h:01m:01s"
    PrettyTime(-1.seconds) shouldBe "-0m:01s"
    PrettyTime(-2.seconds) shouldBe "-0m:02s"
    PrettyTime(-59.seconds) shouldBe "-0m:59s"
    PrettyTime(-60.seconds) shouldBe "-1m:00s"
    PrettyTime(-61.seconds) shouldBe "-1m:01s"

  }
}
