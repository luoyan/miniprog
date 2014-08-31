package com.luoyan.mahout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;

public class CalculateMovies {
	private static void usage() {
		System.out.println("command ratingsCSVFile");
	}
	public static void main(String[] args) throws IOException, TasteException {
		//createCSVRatingFile();
		if (args.length != 1) {
			usage();
			System.exit(-1);
		}
		String ratingsCSVFile = args[0];
		File ratingsFile = new File(ratingsCSVFile);
		DataModel model = new FileDataModel(ratingsFile);
		
		CachingRecommender cachingRecommender = new CachingRecommender(new SlopeOneRecommender(model));
		for (LongPrimitiveIterator it = model.getUserIDs(); it.hasNext();) {
			long userId = it.nextLong();
			List<RecommendedItem> recommendations = cachingRecommender.recommend(userId, 10);
			System.out.println("recommendations.size() " + recommendations.size());
			if (recommendations.size() == 0) {
				System.out.println("User " + userId + " : no recommendations");
			}
			for (RecommendedItem recomendedItem : recommendations) {
				System.out.println("User " + userId + " : " + recomendedItem);
			}
		}
	}

}
