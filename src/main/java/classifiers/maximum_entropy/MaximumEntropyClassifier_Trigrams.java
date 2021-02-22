package main.java.classifiers.maximum_entropy;

/**
 * MaximumEntropyClassifier_Trigrams.java
 * Purpose: Maximum Entropy Classification on data set consisting of trigrams
 * @author Helna James Kuttickattu
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import main.java.classifiers.Algorithm;

public class MaximumEntropyClassifier_Trigrams extends MaximumEntropyClassifier implements Algorithm{
	/**
	 * Maximum Entropy Classification on data set containing trigrams from the sentences or source_phrases
	 */
	
	public void classification() {
	    try {
	    	
		    	String choice=null;
				System.out.println("Please enter the option:1)Sentences 2) Source_phrase_level");
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			    try {
			        choice = reader.readLine();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
		    
			    //Sentences as the data set option
			    if (choice.equals("1")){
			    	//Sentence level Classification
			        String outputModelPath1 = "src/main/resource/MaximumEntropy/model1_trigrams.bin";
			        String training_sentence = "src/main/resource/Ngrams/training_sentence_trigram.csv";
			        String test_sentence = "src/main/resource/Ngrams/test_sentence_trigram.csv";
					String[] files=super.createData(new File(training_sentence),new File(test_sentence),"sentence","trigrams");
					super.trainDataset(files[0],outputModelPath1);
			        super.resultsClassification(files[1],outputModelPath1);
			    }
			    
			  //Source_phrase as the data set option
			    else if(choice.equals("2")){
			     //Source_Phrase level Classification
			        String outputModelPath2 = "src/main/resource/MaximumEntropy/model2_trigrams.bin";
			        String training_source = "src/main/resource/Ngrams/training_source_phrase_trigram.csv";
			        String test_source = "src/main/resource/Ngrams/test_source_phrase_trigram.csv";
			        String[] files1=super.createData(new File(training_source),new File(test_source),"source","trigrams");
					super.trainDataset(files1[0],outputModelPath2);
			        super.resultsClassification(files1[1],outputModelPath2);
			    }
			    else{
			    	System.out.println("Wrong choice");
			    }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
