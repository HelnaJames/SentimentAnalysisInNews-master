package main.java.article;

/**
 * CollectArticleData.java
 * Purpose: Naive bayes classification on unigrams from (sentence or source_phrase)of each document
 * @author Helna James Kuttickattu
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;

import main.java.classifiers.naive_bayes.*;

/**
 * Naive_Bayes_Article.java
 * Purpose: Naive bayes classification on unigrams from (sentence or source_phrase)of each document
 * Write the naive bayes clasification results on sentence and source_phrase
 * @author Helna James Kuttickattu
 */
public class Naive_Bayes_Article {

	public static void main(String[] args) {

		File file1 = new File("src/main/resource/Article_Data");
			    
		String[] directories = file1.list(new FilenameFilter() {
			    	  public boolean accept(File current, String name) {
			    	    return new File(current, name).isDirectory();
			    	  }
		});
		
		for(String dir:directories){
			
			String training_sentence="src/main/resource/Article_Data/"+dir+"/training_sentence_data.csv";
			String test_sentence="src/main/resource/Article_Data/"+dir+"/test_sentence_data.csv";
			String path="src/main/resource/Article_Naive_Bayes/"+dir;
			File file=new File(path);

			if(!file.exists())
				file.mkdirs();
			try {
			    	
					String naive_sentence_path=path	+"/Naive_Bayes_Sentence_Results.txt";
					String naive_source_path=path+"/Naive_Bayes_Source_Phrase_Results.txt";
					NaiveBayesAlgorithm naive_classifier=new NaiveBayesAlgorithm();
					
				
					String sentence_classification_result[]=naive_classifier.classification(training_sentence,test_sentence);
					PrintWriter pw0=new PrintWriter(new FileOutputStream(new File(naive_sentence_path)),true);
					pw0.write(sentence_classification_result[0]);
					pw0.write("\n");
					pw0.write(sentence_classification_result[1]);
					pw0.write("\n");
					pw0.write(sentence_classification_result[2]);
					
			    	String training_source="src/main/resource/Article_Data/"+dir+"/training_source_phrase_data.csv";
			    	String test_source="src/main/resource/Article_Data/"+dir+"/test_source_phrase_data.csv";
					
					String source_classification_result[]=naive_classifier.classification(training_source,test_source);
					PrintWriter pw1=new PrintWriter(new FileOutputStream(new File(naive_source_path)),true);
					pw1.write(source_classification_result[0]);
					pw1.write("\n");
					pw1.write(source_classification_result[1]);
					pw1.write("\n");
					pw1.write(source_classification_result[2]);
					pw0.close();
					pw1.close();

					} catch (Exception e) {
						e.printStackTrace();
					}


			    }

	}

}
