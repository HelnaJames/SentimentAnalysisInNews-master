package main.java.classifiers.maximum_entropy;


/**
 * MaximumEntropyClassifier_Unigrams_Attributes.java
 * Purpose: Maximum Entropy Classification on data set consisting of unigrams and features
 * @author Helna James Kuttickattu
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import main.java.classifiers.Algorithm;

public class MaximumEntropyClassifier_Unigrams_Attributes extends MaximumEntropyClassifier implements Algorithm{
	/**
	 * Maximum Entropy Classification on data set containing unigrams and features from the sentences or source_phrases
	 */
	
	public void classification() {
	    try {
	    	
		    	String choice=null;
				System.out.println("Please enter the option:1)Sentences 2) Source_phrase_level");
				BufferedReader choicereader = new BufferedReader(new InputStreamReader(System.in));
		        MaximumEntropyClassifier maximumEntropyClassifer=new MaximumEntropyClassifier_Unigrams_Attributes();

			    try {
			        choice = choicereader.readLine();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
		    
			    //Choose the sentences as the data set option
			    if (choice.equals("1")){
			    	//Sentence level Classification
			        String maximumEntropyModel1 = "src/main/resource/MaximumEntropy/model1_unigrams_attributes.bin";
			        String trainingSentence = "src/main/resource/Unigrams/training_sentence_negations_data.csv";
			        String testSentence = "src/main/resource/Unigrams/test_sentence_negations_data.csv";
			        String[] sentencefiles=maximumEntropyClassifer.createData(new File(trainingSentence),new File(testSentence),"sentence","evaluation");
					super.trainDataset(sentencefiles[0],maximumEntropyModel1);
			        super.resultsClassification(sentencefiles[1],maximumEntropyModel1);
			    }
			    
			    //Choose the source_phrases as the data set option
			    else if(choice.equals("2")){
				    //Source_Phrase level Classification
			        String maximumEntropyModel2 = "src/main/resource/MaximumEntropy/model2_unigrams_attributes.bin";
			        String trainingSource = "src/main/resource/Unigrams/training_source_negations_data.csv";
			        String testSource = "src/main/resource/Unigrams/test_source_negations_data.csv";
			        String[] sourcefiles=maximumEntropyClassifer.createData(new File(trainingSource),new File(testSource),"source","evaluation");
					super.trainDataset(sourcefiles[0],maximumEntropyModel2);
			        super.resultsClassification(sourcefiles[1],maximumEntropyModel2);
			    }
			    else{
			    	System.out.println("Wrong choice");
			    }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Create the training and test data set in the  class sentence format
	 * 
	 * @param  trainingdata	   String array of sentences
	 * @param  testdata		   path of the maximum entropy model
	 * @param  level           unigrams, bigrams, trigrams, or other features
	 * @return files           training and test data set
	 */
	@Override
	public String[] createData(File trainingdata,File testdata,String level,String type) throws IOException{
		
		 String training_file="src/main/resource/MaximumEntropy/training_"+level+"_"+type+"_labels.txt";
		 String test_file="src/main/resource/MaximumEntropy/test_"+level+"_"+type+"_labels.txt";
	
	  	 BufferedReader br=new BufferedReader(new FileReader(trainingdata));
	  	 PrintWriter pw2 = new PrintWriter(new FileOutputStream(new File(training_file), true));
	  	 PrintWriter pw3 = new PrintWriter(new FileOutputStream(new File(test_file), true));
	  	 String line;
	  	//Create the training data set
	  	line=br.readLine();
	  	while((line = br.readLine()) != null){
	  		String lines[]=line.split(",");
	  		StringBuilder sb=new StringBuilder();
	  		sb.append(lines[5]);
	  		sb.append("\t");
	  		sb.append(lines[0]);
	  		sb.append("\t");
	  		sb.append(lines[1]);
	  		sb.append("\t");
	  		sb.append(lines[2]);
	  		sb.append("\t");
	  		sb.append(lines[3]);
	  		sb.append("\t");
	  		sb.append(lines[4]);
	  		sb.append(System.getProperty("line.separator"));
	  		pw2.write(sb.toString());
	  	}

	  	br.close();
	  	pw2.close();
	  	BufferedReader br1=new BufferedReader(new FileReader(testdata));
	  	line =br1.readLine();
	  	//Create the test data set
	  	while((line = br1.readLine()) != null){
	  		String lines[]=line.split(",");
	  		StringBuilder sb=new StringBuilder();
	  		sb.append(lines[5]);
	  		sb.append("\t");
	  		sb.append(lines[0]);
	  		sb.append("\t");
	  		sb.append(lines[1]);
	  		sb.append("\t");
	  		sb.append(lines[2]);
	  		sb.append("\t");
	  		sb.append(lines[3]);
	  		sb.append("\t");
	  		sb.append(lines[4]);
	  		sb.append(System.getProperty("line.separator"));
	  		pw3.write(sb.toString());
	  		}
	  		
	  	pw3.close();
	    br1.close();

		String[] files=new String[2];
		files[0]=training_file;
		files[1]=test_file;
		
		
		return(files);
		
	 }

}
