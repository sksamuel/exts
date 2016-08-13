package com.sksamuel.exts

import java.sql.{DriverManager, PreparedStatement}

import io.eels.component.avro.AvroSchemaFns
import io.eels.component.jdbc.JdbcSource
import io.eels.{Row, RowListener}

import scala.language.implicitConversions

class Test {

  import com.sksamuel.scalafunc.KotlinConversions._

  val bind: PreparedStatement => Any = stmt => {
    stmt.setString(1, "qwee")
  }

  val source = new JdbcSource(() => DriverManager.getConnection("url"), "query")
    .withFetchSize(1000)
    .withListener(new RowListener {
      override def onRow(row: Row): Unit = ()
    })
    .withBind(bind)
  val schema = source.schema()
  val avro = AvroSchemaFns.toAvroSchema(schema, true)
}
