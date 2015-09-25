package com.countryrecommender.app

import java.sql.{Connection, DriverManager}

trait SQLiteCredentials {
    val conn_str = "jdbc:sqlite:/Users/willwolf/sqlite/tweet-reco.db"
    Class.forName("org.sqlite.JDBC")
    val conn = DriverManager.getConnection(conn_str)
}
