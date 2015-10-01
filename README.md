# CountryRecommender #

## How's this work? ##

When you query for a country, this application will return the 5 countries most similar to your query. These similarities are computed as follows:

1. Capture travel-related tweets from the Twitter API, defined as any containing the strings "#ttot", "#travel", "#lp", "vacation", "holiday", "wanderlust", "viajar", "voyager", "destination", "tbex", or "tourism."
2. If a tweet contains the name of one of 248 countries or territories, or their respective capitals, label this tweet with this country. For example, the tweet "backpacking Iran is awesome! #travel" would be labled with "Iran."
3. Represent each country as a set of the user_id's who have tweeted about it.
4. Compute a Jaccard similarity - defined as the size of the intersection of 2 sets divided by the size of their union - between all combinations of countries.
5. When a country is queried, return the 5 countries Jaccard-most similar.

## Build & Run ##

```sh
$ cd CountryRecommender
$ ./sbt
> container:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.