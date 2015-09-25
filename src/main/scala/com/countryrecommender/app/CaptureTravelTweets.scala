package com.countryrecommender.app

import twitter4j._

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
