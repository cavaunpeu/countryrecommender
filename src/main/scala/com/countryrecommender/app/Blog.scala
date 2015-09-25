package com.countryrecommender.app

import org.scalatra._
import scalate.ScalateSupport
import scala.collection.mutable.ListBuffer

class Blog extends CountryrecommenderStack {

    get("/") {

        contentType="text/html"

        if (params.contains("country")) {
            /* log country to database! */
            val country = params("country").split(" ").map(_.capitalize).mkString(" ")
            val country_sims = new RetrieveCountrySimilarities(country).run()
            ssp("result", "country" -> country, "country_sims" -> country_sims)
        } else {
            ssp("query")
        }
    }
}
