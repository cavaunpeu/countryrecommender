package com.travelbythenumbers.app

import twitter4j._
import java.sql.{Connection, DriverManager}
import scala.collection.mutable.{ListBuffer, Map}
import scala.util.matching.Regex

object CaptureTravelTweets {

    val travel_tags = Array("#ttot", "#travel", "#lp",
            "vacation", "holiday", "wanderlust", "viajar", "voyager", "destination",
            "tbex", "tourism")

    def main(args: Array[String]) {
        val twitterStream = new TwitterStreamFactory(TwitterUtil.config).getInstance
        twitterStream.addListener(TwitterUtil.simpleStatusListener)
        twitterStream.filter(new FilterQuery().track(travel_tags))
        Thread.sleep(36000000)
        twitterStream.cleanUp
        twitterStream.shutdown
        TwitterUtil.conn.close()
    }
}

object TwitterUtil {
    /** define API credentials */
    val config = new twitter4j.conf.ConfigurationBuilder()
        .setOAuthConsumerKey("s83zMIukhW1PyA6fKrEl6EjKc")
        .setOAuthConsumerSecret("flFs0z27xjpM00jTN8hkIw4BAKRukbOgM1AHxnkQa4kEn8vrIR")
        .setOAuthAccessToken("1201496244-PtQ2ybfSEu0vQWYOx60YQeH4N5UpFLF5apQbxsP")
        .setOAuthAccessTokenSecret("bqa1FKFmZvo2jzmte1cZfB0X4OrXpeOSWMplv6ImH4aCS")
        .build

    /** define sqlite parameters */
    val conn_str = "jdbc:sqlite:/Users/willwolf/sqlite/tweet-reco.db"
    Class.forName("org.sqlite.JDBC")
    val conn = DriverManager.getConnection(conn_str)

    /** read in list of world countries and capitals */
    val statement = conn.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM countries")
    var countries_capitals = Map[String, String]()
    while ( resultSet.next() ) {
        countries_capitals(resultSet.getString("country")) = resultSet.getString("capital")
    }

    def giveLocation(tweet: String) = {
        var matches = ListBuffer[String]()
        for ((k,v) <- countries_capitals) {
            val patterns = List(
                    s"$k",
                    s"$k".replaceAll(" ", ""),
                    s"$v",
                    s"$v".replaceAll(" ", "")
                )
            var matches_inner = ListBuffer[Option[String]]()
            for (p <- patterns) {
                matches_inner += new Regex(p.toLowerCase) findFirstIn tweet.toLowerCase
            }
            if (matches_inner.exists(_.isDefined)) matches += k
        }
        matches.toList
    }

    def simpleStatusListener = new StatusListener() {
        def onStatus(status: Status) {
            /** get id, tweet, and count (always 1) */
            val id = status.getId
            val user_id =  status.getUser.getId
            val tweet = status.getText
            val count = 1
            val loc = giveLocation(tweet)

            /** if tweet contains an accepted location, write row to sqlite */
            if ( loc.size > 0 ) {
                for ( l <- loc ) {
                    println(l + " : " + tweet)
                    val ps = conn.prepareStatement("""INSERT INTO tweets (id, user_id, tweet, loc, count)
                                             VALUES (?, ?, ?, ?, ?)""")
                    ps.setLong(1, id)
                    ps.setLong(2, user_id)
                    ps.setString(3, tweet)
                    ps.setString(4, l)
                    ps.setInt(5, count)
                    ps.executeUpdate
                    ps.close
                }
            }
        }
        def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
        def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
        def onException(ex: Exception) { ex.printStackTrace }
        def onScrubGeo(arg0: Long, arg1: Long) {}
        def onStallWarning(warning: StallWarning) {}
    }
}