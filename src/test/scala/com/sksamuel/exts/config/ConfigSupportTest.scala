package com.sksamuel.exts.config

import com.typesafe.config.ConfigFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ConfigSupportTest extends AnyFlatSpec with Matchers with ConfigSupport {

  "config support" should "flatten maps" in {
    val config = ConfigFactory.parseResources("flattentest.conf")
    config.flattenMap shouldBe Map("a.b.c.d" -> "d", "a.b.c.e" -> "e", "a.f.g" -> "g", "a.f.h" -> "h")
  }

}
