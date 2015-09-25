package com.travelbythenumbers.app

import org.scalatra._
import scalate.ScalateSupport
import scala.collection.mutable.ListBuffer

class Blog extends CountryrecommenderStack {

    get("/") {

        contentType="text/html"

        if (params.contains("country")) {
            val country = params("country").split(" ").map(_.capitalize).mkString(" ")
            val country_sims = new RetrieveCountrySimilarities(country).run()
            ssp("result", "country" -> params.get("country"), "country_sims" -> country_sims)
        } else {
            ssp("query")
        }
    }
}
