package com.countryrecommender.app

import org.scalatra._
import java.sql.{Connection, DriverManager}

trait SQLiteCredentials {
    val conn_str = s"jdbc:sqlite:" + System.getenv("SQLITE_DB_PATH")
    Class.forName("org.sqlite.JDBC")
    val conn = DriverManager.getConnection(conn_str)
}
