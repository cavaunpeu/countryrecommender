package com.countryrecommender.app

import scala.collection.mutable.ListBuffer
import scala.math.BigDecimal

class RetrieveCountrySimilarities(val country: String) extends SQLiteCredentials {

    val similarities_table = "similarities"

    def run() = {

        val query = s"""
            SELECT *
            FROM $similarities_table
            WHERE loc1 = "$country"
            OR loc2 = "$country"
            ORDER BY sim DESC
            LIMIT 5
        """
        val statement = conn.createStatement()
        val resultSet = statement.executeQuery(query)

        var country_similarities = ListBuffer[(String, Double)]()
        while ( resultSet.next() ) {
            val sim = resultSet.getFloat("sim")
            val loc1 = resultSet.getString("loc1")
            val loc2 = resultSet.getString("loc2")
            country_similarities += (if (loc1 == country) loc2 else loc1) -> round(sim)
        }
        println(country_similarities)
        country_similarities
    }

    def round(double: Double) = {

        BigDecimal(double).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble
    }
}
