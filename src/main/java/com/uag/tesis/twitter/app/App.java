package com.uag.tesis.twitter.app;

import java.util.ArrayList;

import com.twitter.Extractor;
import com.twitter.Extractor.Entity;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class App {
	public static final double BOSQUES_VICTORIA_SE_LAT = 20.644354;
	public static final double BOSQUES_VICTORIA_SE_LON = -103.388406;
	public static final double ANALCO_LAT = 20.668296;
	public static final double ANALCO_LON = -103.339865;
	public static final double ABASTOS_LAT = 20.656907;
	public static final double ABASTOS_LON = -103.383362;
	public static final double JARDINES_SAN_FRANCISCO_LAT = 20.665598;
	public static final double JARDINES_SAN_FRANCISCO_LON = -103.283045;

	public static void main(String[] args) throws Exception {
		Query query = new Query().geoCode(new GeoLocation(ABASTOS_LAT, ABASTOS_LON), 0.9,
				Query.KILOMETERS.name());
		query.count(200);
		int i = 0;
		Twitter twitter = TwitterFactory.getSingleton();
		QueryResult results = null;
		do {
			results = twitter.search(query);
			for (Status result : results.getTweets()) {
				String tweet = result.getText();
				tweet = tweet.replaceAll("@ ", "@");
				Extractor extractor = new Extractor();
				for (Entity entities : extractor.extractEntitiesWithIndices(tweet)) {
					switch (entities.getType()) {
					case HASHTAG:
						tweet = tweet.replace("#" + entities.getValue(), "");
						break;
					case URL:
						tweet = tweet.replace(entities.getValue(), "");
						break;
					case MENTION:
						tweet = tweet.replace("@" + entities.getValue(), "");
						break;
					case CASHTAG:
						tweet = tweet.replace("$" + entities.getValue(), "");
						break;
					}
				}
				tweet = tweet.replaceAll("\"", "");
				System.out.println(tweet);
				i++;
			}
			query = results.nextQuery();
		} while (results.hasNext());
		System.out.println(i);
	}
}
