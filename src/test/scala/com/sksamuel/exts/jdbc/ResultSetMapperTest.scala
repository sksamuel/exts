package com.sksamuel.exts.jdbc

import java.sql.DriverManager

import org.scalatest.{Matchers, WordSpec}

class ResultSetMapperTest extends WordSpec with JdbcSupport with Matchers {

  Class.forName("org.h2.Driver")
  val conn = DriverManager.getConnection("jdbc:h2:mem:test;IGNORECASE=TRUE")
  conn.createStatement().executeUpdate("create table mytable (a integer, b double, c text)")
  conn.createStatement().executeUpdate("insert into mytable (a,b,c) values ('1','1.4','qqq')")
  conn.createStatement().executeUpdate("insert into mytable (a,b,c) values ('4','5','www')")

  openConnection("jdbc:h2:mem:test;IGNORECASE=TRUE").withCommit { conn =>
    val rs = conn.createStatement().executeQuery("select * from mytable")
    val foos = ResultSetMapper[Foo](rs)
    foos.toList shouldBe List(Foo(1, 1.4, "qqq"), Foo(4, 5, "www"))
  }
}

case class Foo(a: Int, b: Double, c: String)

