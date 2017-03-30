package com.sksamuel.exts.jdbc

import java.sql.{Connection, DriverManager}
import java.util.Properties

import scala.util.control.NonFatal

trait JdbcSupport {
  outer =>

  def withConnection[T](url: String,
                        properties: Map[String, AnyRef] = Map.empty,
                        autoCommit: Boolean = true,
                        schema: String = null,
                        catalog: String = null)(fn: Connection => T): T = {
    val conn = openConnection(url, properties, autoCommit, schema, catalog)
    try {
      fn(conn)
    } finally {
      conn.close()
    }
  }

  def openConnection(url: String,
                     properties: Map[String, AnyRef] = Map.empty,
                     autoCommit: Boolean = true,
                     schema: String = null,
                     catalog: String = null): Connection = {
    val props = new Properties()
    properties.foreach { case (key, value) => props.put(key, value) }
    val conn = DriverManager.getConnection(url, props)
    conn.setAutoCommit(autoCommit)
    if (schema != null)
      conn.setSchema(schema)
    if (catalog != null)
      conn.setCatalog(catalog)
    conn
  }

  def withCommit[T](conn: Connection)(f: Connection => T): T = {
    conn.setAutoCommit(false)
    try {
      val result = f(conn)
      conn.commit()
      result
    } catch {
      case NonFatal(e) =>
        conn.rollback()
        throw e
    }
  }

  implicit class RichConnection(conn: Connection) {
    def withCommit[T](f: Connection => T): T = outer.withCommit(conn)(f)
  }
}
