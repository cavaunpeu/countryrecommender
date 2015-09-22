package com.travelbythenumbers.app

import org.scalatra._
import scalate.ScalateSupport
import scala.collection.mutable.ListBuffer

class Blog extends CountryrecommenderStack {

    get("/") {

        contentType="text/html"

        if (params.contains("country")) {
            val country = params.get("country").mkString
            val retrieve_country_similarities = new RetrieveCountrySimilarities(country)
            val country_sims = retrieve_country_similarities.run()
            ssp("result", "country" -> params.get("country"), "country_sims" -> country_sims)
        } else {
            val country = "Colombia"
            val retrieve_country_similarities = new RetrieveCountrySimilarities(country)
            val country_sims = retrieve_country_similarities.run()
            println(country_sims)
            ssp("query")
        }
    }
}