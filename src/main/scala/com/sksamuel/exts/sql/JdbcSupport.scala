package com.sksamuel.exts.sql

import java.sql.{Connection, PreparedStatement, ResultSet}

import com.sksamuel.exts.io.Using
import com.sksamuel.exts.jdbc.ResultSetIterator

class JdbcSupport(connFn: () => Connection) extends Using {

  def insert(sql: String, parameters: Seq[Any]): Int = {
    insert(
      sql,
      stmt => parameters.zipWithIndex.foreach { case (param, index) =>
        stmt.setObject(index + 1, param)
      }
    )
  }

  def insert(sql: String, paramFn: PreparedStatement => Unit): Int = {
    using(connFn()) { conn =>
      val stmt = conn.prepareStatement(sql)
      paramFn(stmt)
      stmt.executeUpdate()
    }
  }

  def query[T](sql: String, parameters: Seq[Any], mapper: ResultSet => T): Seq[T] = {
    query(
      sql,
      stmt => parameters.zipWithIndex.foreach { case (param, index) =>
        stmt.setObject(index + 1, param)
      },
      mapper
    )
  }

  def query[T](sql: String, paramFn: PreparedStatement => Unit, mapper: ResultSet => T): Seq[T] = {
    using(connFn()) { conn =>
      val stmt = conn.prepareStatement(sql)
      paramFn(stmt)
      ResultSetIterator(stmt.executeQuery).map(mapper).toVector
    }
  }
}