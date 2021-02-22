package main.java.classifiers.naive_bayes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * NaiveBayesGeneral.java
 * Purpose: Main Class for selcting the types of naive bayes classification depending on the data type
 * @author Helna James Kuttickattu
 */

public class NaiveBayesGeneral{

	
	public static void main(String args[]) {
		
		String continueoption=null;
		//Switch menu to choose naive bayes classifier
		do{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		    System.out.println("Please enter the option:");
		    System.out.println("a.Naive bayes Classifier(Unigrams)");
		    System.out.println("b.Naive bayes Classifier(Bigrams)");
		    System.out.println("c.Naive bayes Classifier(Trigrams)");
		    System.out.println("d.Naive bayes Classifier(Unigrams+Adjectives)");
		    System.out.println("e.Naive bayes Classifier(Unigrams+Atttributes)");
		    System.out.println("f.Naive bayes Classifier(Bigrams+Attributes)");
		    System.out.println("g.Naive bayes Classifier(Trigrams+Attributes)");
		    String option = null;
		    try {
			        option = reader.readLine();
		 			 NaiveBayesAlgorithm naive_classifier=new NaiveBayesAlgorithm();

		    switch (option) {
		       case "a":
		    	   
		    	   //NaiveBayesClassification on unigrams from sentences
				
					naive_classifier.classification("src/main/resource/Unigrams/training_sentence_data.csv","src/main/resource/Unigrams/test_sentence_data.csv");
				
					//NaiveBayesClassification on unigrams from source phrase

	 				naive_classifier.classification("src/main/resource/Unigrams/training_source_data.csv","src/main/resource/Unigrams/test_source_data.csv"); 
	 				break;
		     case "b":
			 		 //Naive Bayes Classification on bigrams from sentence
		    	 	naive_classifier.classification("src/main/resource/Ngrams/training_sentence_bigram.csv","src/main/resource/Ngrams/test_sentence_bigram.csv");
			 		 
			 		 //Naive Bayes Classification on bigrams from source phrase
		    	 	naive_classifier.classification("src/main/resource/Ngrams/training_source_phrase_bigram.csv","src/main/resource/Ngrams/test_source_phrase_bigram.csv");
			         break;  
			         
		     case "c":
		 		 	//Naive Bayes Classification on trigrams from sentences
		    	    naive_classifier.classification("src/main/resource/Ngrams/training_sentence_trigram.csv","src/main/resource/Ngrams/test_sentence_trigram.csv");
		 		 	
		 		 	//Naive Bayes Classification on trigrams from source phrase
		    	    naive_classifier.classification("src/main/resource/Ngrams/training_source_phrase_trigram.csv","src/main/resource/Ngrams/test_source_phrase_trigram.csv");
		 		 	break; 
		         
		     case "d":
		    	 
		    	 	BufferedReader reader1= new BufferedReader(new InputStreamReader(System.in));
		    	 	System.out.println("Please enter the option:");
		    	 	System.out.println("1.Sentences 2. Source_Phrase");
		    	 	String option1=reader1.readLine();
		    	 	//Naive Bayes Classification on sentence
		    	 	if(option1.equals("1"))
		    	 	{
		    	 		naive_classifier.classification("src/main/resource/Unigrams/training_sentence_trial_data.csv","src/main/resource/Unigrams/test_sentence_trial_data.csv");
		    	 	} 
		    	 	
		    	 	//Naive Bayes Classification on source_phrase
		    	 	if(option1.equals("2"))
		    	 	{
		    	 		naive_classifier.classification("src/main/resource/Unigrams/training_source_trial_data.csv","src/main/resource/Unigrams/test_source_trial_data.csv");
		    	 	}
		    	 	break;  
		    	 	
		     case "e":
			    	 BufferedReader reader2= new BufferedReader(new InputStreamReader(System.in));
			    	 System.out.println("Please enter the option:");
					 System.out.println("1.Sentences 2. Source_Phrase");
					 String option2=reader2.readLine();
					 
			    	 //Naive Bayes Classification on unigrams with some features(Presence of the contrast,negation, etc) from sentences
					 if(option2.equals("1"))
					 {
						 naive_classifier.classification("src/main/resource/Unigrams/training_sentence_negations_data.csv","src/main/resource/Unigrams/test_sentence_negations_data.csv");
					 } 
			 		
			    	 //Naive Bayes Classification on unigrams with some features(Presence of the contrast,negation, etc) from source_phrase

					 if(option2.equals("2"))
					 {
						 naive_classifier.classification("src/main/resource/Unigrams/training_source_negations_data.csv","src/main/resource/Unigrams/test_source_negations_data.csv");
					 }
					 break;  
		     case "f":
			 		 
			    	 BufferedReader reader3= new BufferedReader(new InputStreamReader(System.in));
			    	 System.out.println("Please enter the option:");
					 System.out.println("1.Sentences 2. Source_Phrase");
					 String option3=reader3.readLine();
					 
			    	 //Naive Bayes Classification on bigrams with some features(Presence of the contrast,negation, etc) from sentences
					 if(option3.equals("1"))
					 {
						 naive_classifier.classification("src/main/resource/Ngrams/training_sentence_negation_bigram.csv","src/main/resource/Ngrams/test_sentence_negation_bigram.csv");
					 }
					 
			    	 //Naive Bayes Classification on bigrams with some features(Presence of the contrast,negation, etc) from source_phrase

					 if(option3.equals("2"))
					 {
						 naive_classifier.classification("src/main/resource/Ngrams/training_source_phrase_negation_bigram.csv","src/main/resource/Ngrams/test_source_phrase_negation_bigram.csv");
	
					 }
			         break;  
		     case "g":
		 		 
			    	 BufferedReader reader4= new BufferedReader(new InputStreamReader(System.in));
			    	 System.out.println("Please enter the option:");
					 System.out.println("1.Sentences 2. Source_Phrase");
					 String option4=reader4.readLine();
					 
			    	 //Naive Bayes Classification on trigrams with some features(Presence of the contrast,negation, etc) from source_phrase
					 if(option4.equals("1"))
					 {
						 naive_classifier.classification("src/main/resource/Ngrams/training_sentence_negation_trigram.csv","src/main/resource/Ngrams/test_sentence_negation_trigram.csv");
					 }
					 
			    	 //Naive Bayes Classification on trigrams with some features(Presence of the contrast,negation, etc) from source_phrase

					 if(option4.equals("2"))
					 {
						 naive_classifier.classification("src/main/resource/Ngrams/training_source_phrase_negation_trigram.csv","src/main/resource/Ngrams/test_source_phrase_negation_trigram.csv");
	
					 }
			         break; 
		    
		     default:
		       System.out.println("Invalid selction");
		       break;
				
		     }
		    } catch (Exception e) {
		        e.printStackTrace();
		        
		    }
		    System.out.println("Do you want to continue with NaiveBAyes Algorithm?if Yes, Enter Y Else N");
		    BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
		    try {
				continueoption = reader1.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     }while(continueoption.equals("Y||y"));

	}


}
