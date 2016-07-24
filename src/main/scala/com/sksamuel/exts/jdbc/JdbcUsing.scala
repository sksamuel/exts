package com.sksamuel.exts.jdbc

import java.sql.Connection

import scala.util.control.NonFatal

trait JdbcUsing {

  def withCommit[T](conn: Connection)(f: Connection => T): T = {
    try {
      val result = f(conn)
      if (!conn.getAutoCommit)
        conn.commit()
      result
    } catch {
      case NonFatal(e) =>
        if (!conn.getAutoCommit)
          conn.rollback()
        throw e
    }
  }
}
