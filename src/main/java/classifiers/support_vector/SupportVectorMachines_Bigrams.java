package main.java.classifiers.support_vector;

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

public class SupportVectorMachines_Bigrams extends SupportVectorMachines_Unigrams implements Algorithm{
	
	public void classification() throws Exception {
		String choice=null;
		System.out.println("Please enter the option:1)Sentences 2) Source_phrase_level");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    try {
	        choice = reader.readLine();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    //Sentences as the option
	    if (choice.equals("1")){
					//Create the LibSVm training and test files
					this.createDataSet("src/main/resource/Ngrams/training_sentence_bigram.csv","sentence","training","bigrams");
					this.createDataSet("src/main/resource/Ngrams/test_sentence_bigram.csv","sentence","test","bigrams");
					//Get the training and testing dataset size of files with sentences
					int training_sentence_count =super.getRowCount("src/main/resource/SVM/sentence_training_bigrams_index.txt");
					int test_sentence_count =super.getRowCount("src/main/resource/SVM/sentence_test_bigrams_index.txt");
					//Train the dataset
					svm_model model1 = super.svmTrain("src/main/resource/SVM/sentence_training_bigrams_index.txt", training_sentence_count); 
					//Test the dataset
					super.evaluate_all_instances("src/main/resource/SVM/sentence_test_bigrams_index.txt", model1,test_sentence_count);
	    }
	    else if (choice.equals("2")){
					this.createDataSet("src/main/resource/Ngrams/training_source_phrase_bigram.csv","source","training","bigrams");
					this.createDataSet("src/main/resource/Ngrams/test_source_phrase_bigram.csv","source","test","bigrams");
					
					//Get the training and testing dataset size of files with source phrases
				    int training_source_count =super.getRowCount("src/main/resource/SVM/source_training_bigrams_index.txt");
					int test_source_count =super.getRowCount("src/main/resource/SVM/source_test_bigrams_index.txt");
					
					//Train the dataset
					svm_model model2 = super.svmTrain("src/main/resource/SVM/source_training_bigrams_index.txt", training_source_count); 
					
					//Test the dataset
					super.evaluate_all_instances("src/main/resource/SVM/source_test_bigrams_index.txt", model2,test_source_count);
					System.out.println("Operation complete");
	    }
	    else {
	    			System.out.println("Wrong choice");
	    }
		}
					
	
	   public void classification(String url,String level,String type) throws Exception{
		
				   String indexfile="src/main/resource/SVM/"+level+"_"+type+"_bigrams_index.txt";
					this.createIndexFiles(indexfile,url);
		}
							

				//Create the index files for training and test data
		public void createIndexFiles(String indexfile,String url) throws IOException{
					
					//Find the wordcount in each texts
					BufferedReader br4 = new BufferedReader(new FileReader(url));
					String row;
					Map<String,Integer> duplicates= new HashMap<String,Integer>();
					while((row = br4.readLine()) != null){
				         String tokens1[]=row.split(",");
				         String tokens[]=tokens1[0].split(" ");
				         for(int j=0;j<tokens.length;j++){
					         int wordcount=1;
					             if (duplicates.containsKey(tokens[j])) { // Keep the wordcount
					                 wordcount=duplicates.get(tokens[j])+1;
					                 duplicates.remove(tokens[j]);
					                 duplicates.put(tokens[j],wordcount);
					             } else {
					            	 duplicates.put(tokens[j],wordcount);// add as forwarded 
					             }
				         } 
				         
				     }
					    
					br4.close();
					
					
					int maxwordcount=1;
					for (String name: duplicates.keySet()){
							if(duplicates.get(name)>maxwordcount){
								maxwordcount=duplicates.get(name);
							}
				    } 
		
				 //Get the index of the key to store in SVM dataset
				   List<String> indexes = new ArrayList<String>(duplicates.keySet());
					//Create a file with index for each keyword
					BufferedReader br;
					PrintWriter pw2 = new PrintWriter(new FileOutputStream(new File(indexfile), true));
					
					
						try {
								br = new BufferedReader(new FileReader(url));
								String line;
								int score;
								line = br.readLine();
								while ((line = br.readLine()) != null) {
									String[] words=line.split(",");
									//System.out.println(words[0]);
									//System.out.println(words[1]);
									StringBuilder sb1=new StringBuilder();
									if(words[1].equals("neutral"))
										 score=1;
									 else if(words[1].equals("neutral-uncertain"))
										 score=2;
									 else if(words[1].equals("mixed"))
										 score=3;
									 else if(words[1].equals("mixed-uncertain"))
										 score=4;
									 else if(words[1].equals("positive"))
										 score=5;
									 else if(words[1].equals("positive-uncertain"))
										 score=6;
									 else if(words[1].equals("negative"))
										 score=7;
									 else
										 score=8;
									sb1.append(score);
									sb1.append(" ");
									String [] tokens=words[0].split(" ");
									int i=1;
									   for (int j=0;j<tokens.length;j++){
											if (duplicates.containsKey(tokens[j])) { 
												int k =indexes.indexOf(tokens[j]); 
												double scalednumber=(double)((duplicates.get(tokens[j]))-1)/(double)(maxwordcount-1);
												sb1.append(k+":"+scalednumber);
												sb1.append(" ");
												i++;
												
											}
								
									   	}
									   sb1.append("\n");
									   if(i!=1)
										   pw2.write(sb1.toString());
									   
									   System.out.println(sb1.toString());
								}
								br.close();
								pw2.close();
						}
						catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						
					
				}
				

}
