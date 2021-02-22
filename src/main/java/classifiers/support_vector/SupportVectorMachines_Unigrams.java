package main.java.classifiers.support_vector;

/**
 * SupportVectorMachines_Unigrams.java
 * Purpose: Support Vector Machine Classification on Unigrams
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import main.java.classifiers.Algorithm;

	public class SupportVectorMachines_Unigrams implements Algorithm{
				/*
				 * Support Vector Machine classification on unigrams
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
				    
				    //Sentences as the dataset option
				    if (choice.equals("1")){
					    //Classification on sentence level
						this.createDataSet("src/main/resource/Unigrams/training_sentence_data.csv","sentence","training","tokens");
						this.createDataSet("src/main/resource/Unigrams/test_sentence_data.csv","sentence","test","tokens");
					
						int training_sentence_count =this.getRowCount("src/main/resource/SVM/sentence_training_tokens_index.txt");
						int test_sentence_count =this.getRowCount("src/main/resource/SVM/sentence_test_tokens_index.txt");
						svm_model model1 = this.svmTrain("src/main/resource/SVM/sentence_training_tokens_index.txt", training_sentence_count); 
						this.evaluate_all_instances("src/main/resource/SVM/sentence_test_tokens_index.txt", model1,test_sentence_count);
				    }
					else if(choice.equals("2")){
						
						//Classification on source_phrase level
						this.createDataSet("src/main/resource/Unigrams/training_source_data.csv","source","training","tokens");
						this.createDataSet("src/main/resource/Unigrams/test_source_data.csv","source","test","tokens");
						int training_source_count =this.getRowCount("src/main/resource/SVM/source_training_tokens_index.txt");
						int test_source_count =this.getRowCount("src/main/resource/SVM/source_test_tokens_index.txt");
						svm_model model2 = this.svmTrain("src/main/resource/SVM/source_training_tokens_index.txt", training_source_count); 
						 
						this.evaluate_all_instances("src/main/resource/SVM/source_test_tokens_index.txt", model2,test_source_count);
					}
					else{
						System.out.println("Wrong choice");
					}
				}
				
				/*Get the number of rows in the training and test files
				 * @param filename		name of the file
				 */
				
				
				
				public int getRowCount(String filename){
					
					BufferedReader br2;
					int rowcount = 0;
					try {
						br2 = new BufferedReader(new FileReader(filename));
					
					     while(br2.readLine() != null){
					         rowcount++;
					     }
				         
					     br2.close();
					}
					catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 return rowcount;
					
				}
				
				/*Create the training and test files in the LibSVM format
				 * @param file		Input file
				 * @param level		Sentence or source phrase
				 * @param filetype  training or test file	
				 * @param datatype  unigrams,bigrams,trigrams, etc.
				 */
					  
			
				public void createDataSet(String file,String level,String filetype,String datatype) throws Exception{
					
					String indexfile="src/main/resource/SVM/"+level+"_"+filetype+"_"+datatype+"_index.txt";
					this.createSVMFiles(indexfile,file);
			       }
							
	
				//Create the files in LibSVM format for training and test data
				public void createSVMFiles(String indexfile,String inputfile) throws IOException{
					
					//word counts in files of each polarity
					BufferedReader br4 = new BufferedReader(new FileReader(inputfile));
					String row;
					Map<String,Integer> wordDictionary= new HashMap<String,Integer>();
					while((row = br4.readLine()) != null){
						
				         	String tokens[]=row.split(",");

					         int wordcount=1;
					             if (wordDictionary.containsKey(tokens[0])) { 
					            	 // Word Exists, update the count of the word
					                 wordcount=wordDictionary.get(tokens[0])+1;
					                 wordDictionary.remove(tokens[0]);
					                 wordDictionary.put(tokens[0],wordcount);
					             } else {
					            	 
					            	 wordDictionary.put(tokens[0],wordcount);// new word, add word and word count in dictionary
					             }
				        	 
				         
				     }
				
				    
					br4.close();
					
					//Get the maximum count of the word from files of all polarity (positive, negative, etc)
					//This is done to scale the word count in the files
					int maxwordcount=1;
					for (String word: wordDictionary.keySet()){
							if(wordDictionary.get(word) > maxwordcount){
								maxwordcount=wordDictionary.get(word);
							}
			        } 
					
					//Get the index and count of the word from the word dictionary to create SVM data set
					//Create a SVMfile with format polarity wordindex:wordcount
				   BufferedReader br;
				   PrintWriter pw2 = new PrintWriter(new FileOutputStream(new File(indexfile), true));
					
					
					try {
							br = new BufferedReader(new FileReader(inputfile));
							String line;
							int score;
							line = br.readLine();
							while ((line = br.readLine()) != null) {
								String[] words=line.split(",");
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
								if (wordDictionary.containsKey(words[0])) { 
										double scalednumber=(double)((wordDictionary.get(words[0]))-1)/(double)(maxwordcount-1);
										sb1.append(1+":"+scalednumber);
										sb1.append(" ");
									}
									
								
								sb1.append("\n");
		
								pw2.write(sb1.toString());
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
				
				
				
				/* Create SVM model trained on the data
				 * @param filename		Name of the file
				 * @param record_size	Count of the rows
				 * @return svm_model	Model trained on the data
				 */
				 public svm_model svmTrain(String filename, int record_size) {
					 
					 LinkedList<String> Dataset = new LinkedList<String>();
					 try{
							 FileReader fr1 = new FileReader(filename);
							 BufferedReader br1 = new BufferedReader(fr1);
							 for(String line1 = br1.readLine();line1!=null;line1=br1.readLine()){
									 	 Dataset.add(line1);
							}
								 		 
							 br1.close();
					 }
					 
					 catch(Exception e){
						 	e.printStackTrace();
					 }
					 
					 
					 double node_values[][] = new double[record_size][]; //jagged array used to store values
					 int node_indexes[][] = new int[record_size][];//jagged array used to store node indexes
					 double node_class_labels [] = new double[record_size];//store class labels
					 
					 
					 for(int i=0;i<Dataset.size();i++){
							 try{
									 String [] data1 = Dataset.get(i).split("\\s");
									 
									 node_class_labels[i] = Integer.parseInt(data1[0].trim());
									 
									 LinkedList<Integer> list_indx = new LinkedList<Integer>();
									 LinkedList<Double> list_val = new LinkedList<Double>();
									
									 for(int k=0;k<data1.length;k++)
									 {
											 String [] tmp_data = data1[k].trim().split(":");
											 if(tmp_data.length==2)
											 {
											 list_indx.add(Integer.parseInt(tmp_data[0].trim()));
											 list_val.add(Double.parseDouble(tmp_data[1].trim()));
									 }
									
									 }
									 if(list_val.size()>0)
									 {
											 node_values[i] = new double[list_val.size()];
											 node_indexes[i] = new int[list_indx.size()];
									 }
									 for(int m=0;m<list_val.size();m++)
									 {
											 node_indexes[i][m] = list_indx.get(m);
											 node_values[i][m] = list_val.get(m); 
									 }
							 }
							 catch(Exception e)
							 {
								 	e.printStackTrace();
							 }
					 }
					 
					 svm_problem prob = new svm_problem();
					 int dataCount = record_size;
					 prob.y = new double[dataCount];
					 prob.l = dataCount;
					 prob.x = new svm_node[dataCount][];     
	
					 for (int i = 0; i < dataCount; i++){ 
					      prob.y[i] = node_class_labels[i];
					      double[] values = node_values[i];
					      int [] indexes = node_indexes[i];
					      prob.x[i] = new svm_node[values.length];
					      for (int j = 0; j < values.length; j++){
					             svm_node node = new svm_node();
					             node.index = indexes[j];
					             node.value = values[j];
					             prob.x[i][j] = node;
					         }           
					     }               
	
					svm_parameter param = new svm_parameter();
					param.probability = 1;
					param.gamma = Math.pow(2, -13);
					param.nu = 0.5;
					param.C = Math.pow(2, 10);
					param.svm_type = svm_parameter.C_SVC;
					param.kernel_type = svm_parameter.RBF;      
					param.cache_size = 20000;
				    param.eps = 0.001;      
	
				    svm_model model = svm.svm_train(prob, param);
	
					return model;
				 }

					
					/* Test the trained model on the test data
					 * @param filename		Name of the file
					 * @param record_size	Count of the rows
					 * @param svm_model	    model trained on the data
					 */
				 public void evaluate_all_instances(String filename, svm_model svmmodel, int record_size){
					 
					 LinkedList<String> Dataset = new LinkedList<String>();//stores the lines from the given file 
					 double labels [] = new double[record_size];
					 try { 
							 FileReader fr1 = new FileReader(filename); 
							 BufferedReader br1 = new BufferedReader(fr1); 
							 for(String line1 = br1.readLine();line1!=null;line1=br1.readLine()) { 
								 Dataset.add(line1); 
							 } 
							 br1.close(); 
					 } 
					 catch(Exception e) { 
						 	e.printStackTrace(); 
					 } 
					 
					 double node_values[][] = new double[record_size][]; //jagged array used to store values 
					 int node_indexes[][] = new int[record_size][];//jagged array used to store node indexes 
					 
					 //Save the index and values
					 for(int i=0;i<Dataset.size();i++) { 
					 try { 
							 String [] data = Dataset.get(i).split("\\s"); 
							 labels[i]=Double.parseDouble(data[0]);
							 LinkedList<Integer> list_indx = new LinkedList<Integer>(); 
							 LinkedList<Double> list_val = new LinkedList<Double>();
							 
							 for(int k=0;k<data.length;k++) { 
								 String [] tmp_data = data[k].trim().split(":"); 
							 if(tmp_data.length==2) { 
								 list_indx.add(Integer.parseInt(tmp_data[0].trim())); 
								 list_val.add(Double.parseDouble(tmp_data[1].trim())); 
							 } 
							 } 
							 if(list_val.size()>0) { 
								 node_values[i] = new double[list_val.size()]; 
								 node_indexes[i] = new int[list_indx.size()]; 
							 } 
							 for(int m=0;m<list_val.size();m++) {
								 
							 node_indexes[i][m] = list_indx.get(m); 
							 node_values[i][m] = list_val.get(m); 
					 } 
					 } 
					 catch(Exception e) { 
						 	e.printStackTrace(); 
					 } 
					 }
					 
					 int count=0;
					 
					 //Predict the class labels and the count of the correctly predicted class labels
					 for(int i=0;i<record_size;i++){
						 int tmp_indexes[] = node_indexes[i];
						 double tmp_values[] = node_values[i];
						 double c= evaluate_single_instance(tmp_indexes,tmp_values, svmmodel);
						 
						 if(c==labels[i]){
							count++; 
						 }
					 
					 }
					 
					 System.out.println("==============================================================");
					 System.out.println("Accuracy:"+(100.0 * count) / (record_size) );
				 }
				 
					
					/* Create SVM model trained on the data
					 * @param indexes[]		Name of the file
					 * @param record_size	Count of the rows
					 * @return svm_model	Model trained on the data
					 */
				 public double evaluate_single_instance(int [] indexes, double[] values, svm_model model1){
					 
				     svm_node[] nodes = new svm_node[values.length];
				     for (int i = 0; i < values.length; i++){
				         svm_node node = new svm_node();
				         node.index = indexes[i];
				         node.value = values[i];
				         nodes[i] = node;
				     }

				     int totalClasses = svm.svm_get_nr_class(model1);       
				     int[] labels = new int[totalClasses];
				     svm.svm_get_labels(model1,labels);

				     double[] prob_estimates = new double[totalClasses];
				     double v = svm.svm_predict_probability(model1, nodes, prob_estimates);

				     for (int i = 0; i < totalClasses; i++){
				         System.out.print("(" + labels[i] + ":" + prob_estimates[i] + ")");
				     }
				     System.out.println(" Prediction:" + v );  
				     return v;
				 }

				@Override
				public void classification(String s1, String s2)
						throws Exception {
					// TODO Auto-generated method stub
					
				}
				

	}


