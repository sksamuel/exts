package com.sksamuel.exts.jdbc

import java.sql.ResultSet

object ResultSetIterator {

  def strings(rs: ResultSet): Iterator[Array[String]] = new Iterator[Array[String]] {
    override def hasNext: Boolean = rs.next()
    override def next(): Array[String] = {
      for ( k <- 1 to rs.getMetaData.getColumnCount ) yield rs.getString(k)
    }.toArray
  }

  def apply(rs: ResultSet): Iterator[ResultSet] = new Iterator[ResultSet] {
    override def hasNext: Boolean = rs.next()
    override def next(): ResultSet = rs
  }
}
