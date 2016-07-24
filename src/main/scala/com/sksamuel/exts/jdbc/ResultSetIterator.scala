package com.sksamuel.exts.jdbc

import java.io.StringReader
import java.sql.ResultSet

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

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

object ResultSetMapper {

  val mapper = new ObjectMapper with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  import scala.reflect.runtime.universe._

  def apply[T: TypeTag : Manifest](rs: ResultSet): Seq[T] = {
    ResultSetIterator(rs).map { rs =>
      var k = 0
      val fields = typeOf[T].members.filter(_.isConstructor).head.asMethod.paramLists.flatten.map { symbol =>
        k = k + 1
        s""" "${symbol.name}" : "${rs.getString(k)}" """
      }
      val json = fields.mkString("{", ",", "}")
      mapper.readValue[T](new StringReader(json))
    }.toList
  }
}