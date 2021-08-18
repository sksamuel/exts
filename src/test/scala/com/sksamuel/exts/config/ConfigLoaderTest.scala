package com.sksamuel.exts.config

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ConfigLoaderTest extends AnyFunSuite with Matchers {

  test("config should parse application after reference") {
    sys.props.put(ConfigLoader.ConfigKey, "")
    ConfigLoader("test1").getString("foo.name") shouldBe "b"
  }

  test("config should parse cwd override field first") {
    sys.props.put(ConfigLoader.ConfigKey, "")
    ConfigLoader("test2").getString("foo.x") shouldBe "c"
  }

  test("config should load env conf when env set which overrides app conf") {
    sys.props.put(ConfigLoader.ConfigKey, "DEV")
    ConfigLoader("test1").getString("foo.name") shouldBe "d"
  }

  test("override conf should take precedence over env") {
    sys.props.put(ConfigLoader.ConfigKey, "DEV")
    ConfigLoader("test2").getString("foo.x") shouldBe "c"
  }
}
