package com.travelbythenumbers.app

import org.scalatra._
import scalate.ScalateSupport
import scala.collection.mutable.ListBuffer

class Blog extends CountryrecommenderStack with SQLiteCredentials {

  get("/") {

    contentType="text/html"
    if (params.contains("country")) {
        /** read similarities from sqlite */
        val country = params.get("country").mkString
        val statement = conn.createStatement()
        val query = s"""SELECT * 
                        FROM sims 
                        WHERE loc1 = "$country"
                        OR loc2 = "$country"
                        ORDER BY sim DESC
                        LIMIT 5"""
        val resultSet = statement.executeQuery(query)
        var jaccard_sims = ListBuffer[(String, String)]()
        while ( resultSet.next() ) {
            val sim = resultSet.getString("sim")
            val loc1 = resultSet.getString("loc1")
            val loc2 = resultSet.getString("loc2")
            // val printthis = 
            jaccard_sims += (if (loc1 == country) loc2 else loc1) -> sim
        }
        layoutTemplate("/WEB-INF/templates/layouts/country.ssp", "country" -> params.get("country"), "jaccard_sims" -> jaccard_sims)
    } else {
        layoutTemplate("/WEB-INF/templates/layouts/main.ssp")
    } 
  }
}
