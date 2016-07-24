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

object ResultSetMapper {

  import scala.reflect.runtime.universe._

  import org.json4s._
  import org.json4s.native.JsonMethods._

  implicit val format = org.json4s.DefaultFormats + new NumberSerializer() + new BooleanSerializer() + new DoubleSerializer()

  class NumberSerializer extends CustomSerializer[Int](format => ( {
    case JString(x) => x.toInt
  }, {
    case x: Int => JInt(x)
  }: PartialFunction[Any, JValue]
    ))

  class DoubleSerializer extends CustomSerializer[Double](format => ( {
    case JString(x) => x.toDouble
  }: PartialFunction[JValue, Double], {
    case x: Int => JInt(x)
  }: PartialFunction[Any, JValue]
    ))

  class BooleanSerializer extends CustomSerializer[Boolean](format => ( {
    case JString(x) => x == "true"
  }, {
    case x: Int => JBool(x == 1)
  }: PartialFunction[Any, JValue]
    ))

  def apply[T: TypeTag : Manifest](rs: ResultSet): Seq[T] = {
    ResultSetIterator(rs).map { rs =>
      var k = 0
      val fields = typeOf[T].members.filter(_.isConstructor).head.asMethod.paramLists.flatten.map { symbol =>
        k = k + 1
        s""" "${symbol.name}" : "${rs.getString(k)}" """
      }
      val json = fields.mkString("{", ",", "}")
      parse(json).extract[T]
    }.toList
  }
}