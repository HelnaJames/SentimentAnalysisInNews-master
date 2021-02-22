package main.java.classifiers.support_vector;

/**
 * SupportVectorMachines_Unigrams_Negations.java
 * Purpose: Support Vector Machine Classification on Unigrams and features
 * @author Helna James Kuttickattu
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libsvm.svm_model;
import main.java.classifiers.Algorithm;

public class SupportVectorMachines_Unigrams_Negation extends SupportVectorMachines_Unigrams implements Algorithm{
	
	/*
	 * Support Vector Machines with unigrams and some features
	 * @see main.java.classifiers.support_vector.SupportVectorMachines_Unigrams#classification()
	 */
	public void classification() throws Exception {
		
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
	    	
				//Create the LibSvm training and test files
				this.createDataSet("src/main/resource/Unigrams/training_sentence_negations_data.csv","sentence","training");
				this.createDataSet("src/main/resource/Unigrams/test_sentence_negations_data.csv","sentence","test");
				
				//Get the training and testing data set size of the file with sentences
				int training_sentence_count =super.getRowCount("src/main/resource/SVM/sentence_training_negation_unigrams_index.txt");
				int test_sentence_count =super.getRowCount("src/main/resource/SVM/sentence_test_negation_unigrams_index.txt");
				
				//Train the data set
				svm_model svmmodelSentence = super.svmTrain("src/main/resource/SVM/sentence_training_negation_unigrams_index.txt", training_sentence_count); 
				//Test the data set
				super.evaluate_all_instances("src/main/resource/SVM/sentence_test_negation_unigrams_index.txt", svmmodelSentence,test_sentence_count);
	    }
	    
	  //Source Phrase as the data set option
	    else if (choice.equals("2"))
	    {
				//Create the LibSvm training and test files
				this.createDataSet("src/main/resource/Unigrams/training_source_negations_data.csv","source","training");
				this.createDataSet("src/main/resource/Unigrams/test_source_negations_data.csv","source","test");
				
				//Get the training and testing data set size of the file with source phrases
			    int training_source_count =super.getRowCount("src/main/resource/SVM/source_training_negation_unigrams_index.txt");
				int test_source_count =super.getRowCount("src/main/resource/SVM/source_test_negation_unigrams_index.txt");
				
				//Train the data set
				svm_model svmmodelSource = super.svmTrain("src/main/resource/SVM/source_training_negation_unigrams_index.txt", training_source_count); 
				
				//Test the dataset
				super.evaluate_all_instances("src/main/resource/SVM/source_test_negation_unigrams_index.txt", svmmodelSource,test_source_count);
	    }
	    else
	    {
	    	System.out.println("Wrong choice");
	    }
		}
					
	  
	   public void createDataSet(String url,String level,String type) throws Exception{
		
		   String indexfile="src/main/resource/SVM/"+level+"_"+type+"_negation_unigrams_index.txt";
		   SupportVectorMachines_Unigrams_Negation supportVectorModel=new SupportVectorMachines_Unigrams_Negation();
		   supportVectorModel.createSVMFiles(indexfile,url);
		}
							

		//Create the SVM files for training and test data
		public void createSVMFiles(String indexfile,String url) throws IOException{
					
			//Count of words in the files with sentneces or source data
			BufferedReader br4 = new BufferedReader(new FileReader(url));
			String row;
			Map<String,Integer> wordDictionary= new HashMap<String,Integer>();
			
			while((row = br4.readLine()) != null) {
				String tokens1[]=row.split(",");
				String tokens[]=tokens1[0].split(" ");
				for(int j=0;j<tokens.length;j++){
					int wordcount=1;
					if (wordDictionary.containsKey(tokens[j])) { 
						wordcount=wordDictionary.get(tokens[j])+1;
					    wordDictionary.remove(tokens[j]);
					    wordDictionary.put(tokens[j],wordcount);
					} else {
					        	wordDictionary.put(tokens[j],wordcount);
					}
				         
				} 
				         
			}
			
					    
			br4.close();
			int maxwordcount=1;
			    
			for (String name: wordDictionary.keySet()){
				if(wordDictionary.get(name)>maxwordcount){
						maxwordcount=wordDictionary.get(name);
				}
			} 

			//Get the index and count of word from the dictionary
			List<String> indexes = new ArrayList<String>(wordDictionary.keySet());
			//Create SVM files for the training and test files with format polarity word:wordcount
			BufferedReader br;
			PrintWriter pw2 = new PrintWriter(new FileOutputStream(new File(indexfile), true));
					
					
			try {
			    	
					br = new BufferedReader(new FileReader(url));
					String line;
					int score;
					line = br.readLine();
					while ((line = br.readLine()) != null) {
						String[] words=line.split(",");
						StringBuilder sb1=new StringBuilder();
						if(words[5].equals("neutral"))
							 score=1;
						 else if(words[5].equals("neutral-uncertain"))
							 score=2;
						 else if(words[5].equals("mixed"))
							 score=3;
						 else if(words[5].equals("mixed-uncertain"))
							 score=4;
						 else if(words[5].equals("positive"))
							 score=5;
						 else if(words[5].equals("positive-uncertain"))
							 score=6;
						 else if(words[5].equals("negative"))
							 score=7;
						 else
							 score=8;
						sb1.append(score);
						sb1.append(" ");
						String [] tokens=words[0].split(" ");
						int i=1;
						
						for (int j=0;j<tokens.length;j++){
							if (wordDictionary.containsKey(tokens[j])) { 
								int k =indexes.indexOf(tokens[j]); 
								double scalednumber=(double)((wordDictionary.get(tokens[j]))-1)/(double)(maxwordcount-1);
								sb1.append(k+":"+scalednumber);
								sb1.append(" ");
								i++;
								
							}
					
						}
						
						//Add the features along with unigrams in the file
						   
						if(words[1].equals("true")){
							   sb1.append(2+":1");
							   sb1.append(" ");
						}
						else{
							   sb1.append(2+":1");
							   sb1.append(" ");
						}
						   
						   
						if(words[2].equals("No")){
							   sb1.append(3+":1");
							   sb1.append(" ");
						}
						else{
							   sb1.append(3+":1");
							   sb1.append(" ");
						}
						   
						sb1.append(4+":"+words[3]);
						sb1.append(" ");
						sb1.append(5+":"+words[4]);
						sb1.append("\n");
						if(i!=1)
							pw2.write(sb1.toString());
						   
				}
				br.close();
				pw2.close();
			}catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					
				}
}
