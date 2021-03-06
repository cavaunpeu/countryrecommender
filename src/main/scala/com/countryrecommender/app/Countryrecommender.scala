package com.countryrecommender.app

import org.scalatra._
import scalate.ScalateSupport
import scala.collection.mutable.ListBuffer

class Countryrecommender extends CountryrecommenderStack {

    get("/") {
        contentType="text/html"
        if (params.contains("countries-like")) {

            val country = new Country(params("countries-like"))

            if (!country.is_valid) {
                ssp("invalid")
            } else {
                val country_sims = new RetrieveCountrySimilarities(country.name).run()
                ssp("result", "countries_like" -> country.name, "country_sims" -> country_sims)
            }

        } else {
            ssp("query")
        }
    }

    get("/info") {
        contentType="text/html"
        ssp("info")
    }
}
