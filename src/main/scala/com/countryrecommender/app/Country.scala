package com.countryrecommender.app

class Country(country: String) {

    def normalize(country: String) = {
        country.toLowerCase
            .replaceAll("[^a-z]", "")
            .split(" ")
            .map(_.capitalize)
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