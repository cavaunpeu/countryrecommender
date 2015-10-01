package com.countryrecommender.app

import scala.collection.mutable.ListBuffer
import scala.math.BigDecimal

class RetrieveCountrySimilarities(val country: String) extends PosgreSQLCredentials {

    val similarities_table = "similarities"

    def run() = {

        val query = s"""
            SELECT *
            FROM $similarities_table
            WHERE location_1 = '$country'
            OR location_2 = '$country'
            ORDER BY similarity DESC
            LIMIT 5
        """
        val statement = conn.createStatement()
        val resultSet = statement.executeQuery(query)

        var country_similarities = ListBuffer[(String, Double)]()
        while ( resultSet.next() ) {
            val similarity = resultSet.getFloat("similarity")
            val location_1 = resultSet.getString("location_1")
            val location_2 = resultSet.getString("location_2")
            country_similarities += (if (location_1 == country) location_2 else location_1) -> round(similarity)
        }
        country_similarities
    }

    def round(double: Double) = {

        BigDecimal(double).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble
    }
}
