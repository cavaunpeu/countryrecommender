package com.countryrecommender.app

import org.scalatra._
import java.net.URI
import java.sql.{Connection, DriverManager}
import java.util.Properties

trait PosgreSQLCredentials {
    val database_url = "jdbc:" + System.getenv("DATABASE_URL")
    Class.forName("org.postgresql.Driver")
    val conn = DriverManager.getConnection(database_url)
}
