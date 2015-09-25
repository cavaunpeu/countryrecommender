package com.travelbythenumbers.app

import org.scalatra._
import scalate.ScalateSupport
import scala.collection.mutable.ListBuffer

class Blog extends CountryrecommenderStack {

    get("/") {

        if (params.contains("country")) {
            val country = params("country").split(" ").map(_.capitalize).mkString(" ")
            val retrieve_country_similarities = new RetrieveCountrySimilarities(country)
            val country_sims = retrieve_country_similarities.run()
            ssp("result", "country" -> params.get("country"), "country_sims" -> country_sims)
        } else {
            ssp("query")
        }
    }
}
