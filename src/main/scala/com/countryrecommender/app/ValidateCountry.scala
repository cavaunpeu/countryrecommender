package com.countryrecommender.app

class ValidateCountry(val country: String) extends PosgreSQLCredentials {

    val countries_table = "countries"

    def is_valid = {
        val query = s"""
            SELECT COUNT(1) AS count
            FROM $countries_table
            WHERE country = '$country'
        """
        val statement = conn.createStatement()
        val resultSet = statement.executeQuery(query); resultSet.next()
        val count = resultSet.getInt("count")
        count > 0
    }
}
