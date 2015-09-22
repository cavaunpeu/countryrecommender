package com.travelbythenumbers.app

import java.sql.{Connection, DriverManager}
import scala.collection.mutable.ListBuffer


trait SQLiteCredentials {
    val conn_str = "jdbc:sqlite:/Users/willwolf/sqlite/tweet-reco.db"
    Class.forName("org.sqlite.JDBC")
    val conn = DriverManager.getConnection(conn_str)
}


object ComputeTweetSimilarities extends SQLiteCredentials {
    def main(args: Array[String]) {

        /** import tweets from sqlite */
        val tweets = import_data(args(0))

        /** map countries to user_ids who tweeted about that country */
        val countries_user_ids = tweets.groupBy(_._1).map{
            case (k, v) => (k, v.map(l => l._2).toSet)
            }.toList

        /** compute jaccard similarities, write to sqlite */
        val sims = compute_jaccard_sims(countries_user_ids)

        write_sims_to_db(sims)
    }

    def import_data(tbl_name: String) = {

        /** pull tweets from sqlite */
        val q = "SELECT user_id, loc, count FROM " + tbl_name
        val statement = conn.createStatement()
        val resultSet = statement.executeQuery(q)

        /** create ListBuffer of tweets */
        var tweets = ListBuffer[Tuple2[String, String]]()
        while ( resultSet.next() ) {
            tweets += Tuple2(resultSet.getString("loc"), resultSet.getString("user_id"))
        }
        tweets.toList
    }

    def compute_jaccard_sims(countries_user_ids: List[(String, Set[String])]) = {

        countries_user_ids.combinations(2).map{
            pair => (
                (pair(0)._1, pair(1)._1),
                pair(0)._2.intersect(pair(1)._2).size / pair(0)._2.union(pair(1)._2).size.toFloat
            )
        }.toList
    }

    def write_sims_to_db(sims: List[((String, String), Float)]) {
        sims.foreach{
            case (locs, sim) =>
            val ps = conn.prepareStatement("""INSERT INTO sims (loc1, loc2, sim)
                                             VALUES (?, ?, ?)""")
            ps.setString(1, locs._1)
            ps.setString(2, locs._2)
            ps.setFloat(3, sim)
            ps.executeUpdate
            ps.close
        }
    }
}
