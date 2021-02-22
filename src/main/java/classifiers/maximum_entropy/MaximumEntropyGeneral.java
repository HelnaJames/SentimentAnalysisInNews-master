package main.java.classifiers.maximum_entropy;

/**
 * MaximumEntropyGeneral.java
 * 
 * Purpose: Main Class for selcting the types of maximum entropy classification depending on the data type
 * @author Helna James Kuttickattu
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MaximumEntropyGeneral{

	public static void main(String args[])
	{
	String continueoption=null;
	//Switch menu to choose the Maximum Entropy Classification on different data 
	do{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    System.out.println("Please enter the option:");
	    System.out.println("a.Maximum Entropy Classifier");
	    System.out.println("b.Maximum Entropy Classifier(Bigrams)");
	    System.out.println("c.Maximum Entropy Classifier(Trigrams)");
	    System.out.println("d.Maximum Entropy Classifier(Unigrams(Evaluation))");
	    String option = null;
	    try {
	        option = reader.readLine();
	    } catch (IOException e) {
	        e.printStackTrace();
	        
	    }
	    switch (option) {
	    
		     case "a"://Maximum Entropy Classification on unigrams
		    	 	  MaximumEntropyClassifier maximum_entropy_classifier_unigrams=new MaximumEntropyClassifier();
		     		  maximum_entropy_classifier_unigrams.classification();
				      break;
		     case "b"://Maximum Entropy Classification on bigrams
		    	 	  MaximumEntropyClassifier_Bigrams maximum_entropy_classifier_bigrams=new MaximumEntropyClassifier_Bigrams();
		     		  maximum_entropy_classifier_bigrams.classification();
					  break;
		     case "c"://Maximum Entropy Classification on trigrams
		    	      MaximumEntropyClassifier_Trigrams maximum_entropy_classifier_trigrams=new MaximumEntropyClassifier_Trigrams();
		     		  maximum_entropy_classifier_trigrams.classification();
			          break;
		     case "d"://Maximum Entropy Classification on unigrams with features
		    	 	  MaximumEntropyClassifier_Unigrams_Attributes maximum_entropy_classifier_unigrams_attributes=new MaximumEntropyClassifier_Unigrams_Attributes();
		     		  maximum_entropy_classifier_unigrams_attributes.classification();
			          break;
		     default:
		    	 	  System.out.println("Invalid selction");
		              break;
	     }
	    
	    System.out.println("Do you want to continue?if Yes, Enter Y Else N");
	    BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
	    try {
			continueoption = reader1.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     }while(continueoption.equals("Y"));

	}


}
