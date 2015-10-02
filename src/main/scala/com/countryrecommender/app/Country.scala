package com.countryrecommender.app

class Country(country: String) {

    val stopwords = List("and", "of")

    def normalize(country: String) = {
        country.toLowerCase
            .replaceAll("[^a-z- ]", "")
            .split(" ")
            .map(word => if (stopwords.contains(word)) word else word.capitalize)
            .mkString(" ")
    }

    def name = {
        normalize(country)
    }

    def is_valid = {
        val normalized_country = normalize(country)
        new ValidateCountry(name).is_valid
    }
}