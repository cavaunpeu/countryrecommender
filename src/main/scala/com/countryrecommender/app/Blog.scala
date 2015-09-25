package com.countryrecommender.app

import org.scalatra._
import scalate.ScalateSupport
import scala.collection.mutable.ListBuffer

class Blog extends CountryrecommenderStack {

    get("/") {

        contentType="text/html"

        if (params.contains("countries-like")) {

            val country = params("countries-like").split(" ").map(_.capitalize).mkString(" ")
            val country_is_valid = new ValidateCountry(country).is_valid

            if (!country_is_valid) {
                ssp("invalid")
            } else {
                val country_sims = new RetrieveCountrySimilarities(country).run()
                ssp("result", "countries_like" -> country, "country_sims" -> country_sims)
            }

        } else {
            ssp("query")
        }
    }
}
