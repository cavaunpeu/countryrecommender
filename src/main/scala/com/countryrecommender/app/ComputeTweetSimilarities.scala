package com.countryrecommender.app

import java.sql.{ResultSet}
import scala.collection.mutable.ListBuffer

object ComputeTweetSimilarities extends PosgreSQLCredentials {
    def main(args: Array[String]) {

        val database_table = args(0)

        val tweets = import_tweets(database_table)
        val mapped_tweets = map_tweet_countries_to_user_ids(tweets)
        val reduced_tweets = reduce_to_vector_of_user_ids(mapped_tweets)

        val similarities = compute_jaccard_similarities(reduced_tweets)
        write_similarities_to_postgres(similarities)
    }

    def import_tweets(tbl_name: String) = {

        val query = "SELECT user_id, location, count FROM " + tbl_name
        val statement = conn.createStatement()
        val resultSet = statement.executeQuery(query)
        resultSet
    }

    def map_tweet_countries_to_user_ids(resultSet: ResultSet) = {

        var tweets = ListBuffer[Tuple2[String, String]]()
        while ( resultSet.next() ) {
            tweets += Tuple2(resultSet.getString("location"), resultSet.getString("user_id"))
        }
        tweets.toList
    }

    def reduce_to_vector_of_user_ids(mapped_tweets: List[Tuple2[String, String]]) = {

        mapped_tweets.groupBy(_._1)
            .map{
                case (k, v) => (k, v.map(l => l._2).toSet)
            }.toList
    }

    def compute_jaccard_similarities(reduced_tweets: List[(String, Set[String])]) = {

        reduced_tweets.combinations(2).map{
            pair => (
                (pair(0)._1, pair(1)._1),
                pair(0)._2.intersect(pair(1)._2).size / pair(0)._2.union(pair(1)._2).size.toFloat
            )
        }.toList
    }

    def write_similarities_to_postgres(sims: List[((String, String), Float)]) {

        val delete_statement = conn.prepareStatement("DELETE FROM similarities")
        delete_statement.executeUpdate

        sims.foreach{
            case (countries, sim) =>
            val ps = conn.prepareStatement("""INSERT INTO similarities (location_1, location_2, similarity)
                                             VALUES (?, ?, ?)""")
            ps.setString(1, countries._1)
            ps.setString(2, countries._2)
            ps.setFloat(3, sim)
            ps.executeUpdate
            ps.close
        }
    }
}
