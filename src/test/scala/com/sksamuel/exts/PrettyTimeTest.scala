package com.sksamuel.exts

import org.scalatest.{FunSuite, Matchers}
import scala.concurrent.duration._

class PrettyTimeTest extends FunSuite with Matchers {

  test("pretty time hour minute second combinations") {
    PrettyTime(70.seconds) shouldBe "1m:10s"
    PrettyTime(170.seconds) shouldBe "2m:50s"
    PrettyTime(10.seconds) shouldBe "0m:10s"
    PrettyTime(1.seconds) shouldBe "0m:01s"
    PrettyTime(2.seconds) shouldBe "0m:02s"
    PrettyTime(1.hour) shouldBe "1h:0m:00s"
    PrettyTime(60.seconds) shouldBe "1m:00s"
    PrettyTime(59.seconds) shouldBe "0m:59s"
    PrettyTime(180.seconds) shouldBe "3m:00s"
    PrettyTime(1.hour + 1.minute + 1.second) shouldBe "1h:1m:01s"
  }
}
