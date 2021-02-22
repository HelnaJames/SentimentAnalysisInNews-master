package main.java.classifiers.maximum_entropy;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import main.java.classifiers.Algorithm;

/**
 * MaximumEntropyClassifier_Bigrams.java
 * Purpose: Maximum Entropy Classification on data set consisting of bigrams
 * @author Helna James Kuttickattu
 */

public class MaximumEntropyClassifier_Bigrams extends MaximumEntropyClassifier implements Algorithm{


	public void classification() {
	    try {
	    	
		    	String choice=null;
				System.out.println("Please enter the option:1)Sentences 2) Source_phrase_level");
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			    try {
			        choice = reader.readLine();
			    
		    
				    //Sentences as the data set option
				    if (choice.equals("1")){
				    	//Sentence level Classification
				        String maximumEntropyModel1 = "src/main/resource/MaximumEntropy/model1_bigrams.bin";
				        String training_sentence = "src/main/resource/Ngrams/training_sentence_bigram.csv";
				        String test_sentence = "src/main/resource/Ngrams/test_sentence_bigram.csv";
						String[] files=super.createData(new File(training_sentence),new File(test_sentence),"sentence","bigrams");
						super.trainDataset(files[0],maximumEntropyModel1);
				        super.resultsClassification(files[1],maximumEntropyModel1);
				    }
				    
				  //Source_Phrase as the data set option
				    else if(choice.equals("2")){
				       //Source_Phrase level Classification
				        String maximumEntropyModel2 = "src/main/resource/MaximumEntropy/model2_bigrams.bin";
				        String training_source = "src/main/resource/Ngrams/training_source_phrase_bigram.csv";
				        String test_source = "src/main/resource/Ngrams/test_source_phrase_bigram.csv";
				        String[] files1=super.createData(new File(training_source),new File(test_source),"source","bigrams");
						super.trainDataset(files1[0],maximumEntropyModel2);
				        super.resultsClassification(files1[1],maximumEntropyModel2);
				    }
				    else{
				    	System.out.println("Wrong choice");
				    }
	        
			  } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	  }catch (Exception e) {
	        e.printStackTrace();
	
	  }
  
	}
	
}
