package com.countryrecommender.app

import java.sql.{Connection, DriverManager}

class ValidateCountry(val country: String) extends SQLiteCredentials {

    val countries_table = "countries"

    def is_valid = {
        val query = s"""
            SELECT COUNT(1) AS count
            FROM $countries_table
            WHERE country = "$country"
        """
        val statement = conn.createStatement()
        val resultSet = statement.executeQuery(query)
        resultSet.getFloat("count") > 0
    }
}
