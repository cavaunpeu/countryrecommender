package com.countryrecommender.app

class Country(country: String) {

    val stopwords = List("and", "of")

    def normalize(country: String) = {
        country.toLowerCase
            .replaceAll("[^a-z- ]", "")
            .split(space_or_hyphen)
            .map(word => if (stopwords.contains(word)) word else word.capitalize)
            .mkString(space_or_hyphen)
    }

    def name = {
        normalize(country)
    }

    def space_or_hyphen = {
        if (country.contains("-")) {
            "-"
        }
        else {
            " "
        }
    }

    def is_valid = {
        val normalized_country = normalize(country)
        new ValidateCountry(name).is_valid
    }
}