package com.travelbythenumbers.app

import scala.collection.mutable.ListBuffer

class RetrieveCountrySimilarities(val country: String) extends SQLiteCredentials {
    def run() = {
        /** capitalize the first letter of country! **/
        val query = s"""
            SELECT *
            FROM sims
            WHERE loc1 = "$country"
            OR loc2 = "$country"
            ORDER BY sim DESC
            LIMIT 5
        """
        println(query)
        val statement = conn.createStatement()
        val resultSet = statement.executeQuery(query)

        var country_similarities = ListBuffer[(String, String)]()
        while ( resultSet.next() ) {
            val sim = resultSet.getString("sim")
            val loc1 = resultSet.getString("loc1")
            val loc2 = resultSet.getString("loc2")
            country_similarities += (if (loc1 == country) loc2 else loc1) -> sim
        }
        country_similarities
    }
}
