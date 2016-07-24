package com.sksamuel.exts.jdbc

import java.sql.DriverManager

import org.scalatest.{Matchers, WordSpec}

class ResultSetMapperTest extends WordSpec with JdbcSupport with Matchers {

  Class.forName("org.h2.Driver")
  val conn = DriverManager.getConnection("jdbc:h2:mem:test;IGNORECASE=TRUE")
  conn.createStatement().executeUpdate("create table mytable (a integer, b bit, c text)")
  conn.createStatement().executeUpdate("insert into mytable (a,b,c) values ('1','1','3')")
  conn.createStatement().executeUpdate("insert into mytable (a,b,c) values ('4','5','6')")

  openConnection("jdbc:h2:mem:test;IGNORECASE=TRUE").withCommit { conn =>
    val rs = conn.createStatement().executeQuery("select * from mytable")
    val foos = ResultSetMapper[Foo](rs)
    foos.toList shouldBe List(Foo(1, false, "3"), Foo(4, false, "6"))
  }
}

case class Foo(a: Int, b: Boolean, c: String)

