package main.java.classifiers.support_vector;

/**
 * SupportVectorMachines.java
 * Purpose: Support Vector Machine Classification depending on data set
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libsvm.svm_model;
import main.java.classifiers.Algorithm;




public class SupportVectorMachines implements Algorithm{

	/*Support Vector Machines Classification
	 * 
	 */
	@Override
	public void classification() throws Exception{
		
		SupportVectorMachines supportVectorMachines =new SupportVectorMachines();
		SupportVectorMachines_Unigrams supportVectorUnigrams=new SupportVectorMachines_Unigrams();
		System.out.println("Please enter the option:1)Sentences 2) Source_phrase_level");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    String choice=null;
	    
	    try {
	        choice = reader.readLine();
		    //Sentences as the data set option
		    if (choice.equals("1")){
		    	
				supportVectorMachines.createDataSet(new File("src/main/resource/Preprocessed_Sentences"),"sentence");
				//Get the size of training and testing data set from sentence data
				int trainingSentencedataSize =supportVectorUnigrams.getRowCount("src/main/resource/SVM/sentence_training_tokens_index11.txt");
				int testSentencedataSize =supportVectorUnigrams.getRowCount("src/main/resource/SVM/sentence_test_tokens_index11.txt");
				
				//Get the SVMModel trained on sentence data
				svm_model svmmodelSentence = supportVectorUnigrams.svmTrain("src/main/resource/SVM/sentence_training_tokens_index11.txt", trainingSentencedataSize); 
				
				//Test the data set
				supportVectorUnigrams.evaluate_all_instances("src/main/resource/SVM/sentence_test_tokens_index11.txt", svmmodelSentence,testSentencedataSize);
		    }
		  
		    //Source_Phrase as the data set option
		    if (choice.equals("2")){
		    	
			    supportVectorMachines.createDataSet(new File("src/main/resource/Preprocessed_Sentences"),"source");
			  
			    //Get the size of training and testing data from source_phrase data
				int training_source_count =supportVectorUnigrams.getRowCount("src/main/resource/SVM/source_training_tokens_index11.txt");
				int test_source_count =supportVectorUnigrams.getRowCount("src/main/resource/SVM/source_test_tokens_index11.txt");
				
				//Get the SVMModel trained on source data
				svm_model svmmodelSource  = supportVectorUnigrams.svmTrain("src/main/resource/SVM/source_training_tokens_index11.txt", training_source_count); 
				//Test the dataset
				supportVectorUnigrams.evaluate_all_instances("src/main/resource/SVM/source_test_tokens_index11.txt", svmmodelSource,test_source_count);
			    }
		    
	    } catch (IOException e) {
	        e.printStackTrace();
	  }
		   
	}
	
	/* Create the training and testing data set from sentences or source_phrase  in the SVM format
	 * 
	 * @param input		the path of directory containing data set
	 * @param type 		sentence or source_phrase
	 */
	
	
	public void createDataSet(File input,String type) throws Exception{
		
		String indexfile="src/main/resource/SVM/"+type+"_training_tokens_index11.txt";
		String indexfile1="src/main/resource/SVM/"+type+"_test_tokens_index11.txt";
		
		for (final File fileEntry : input.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            createDataSet(fileEntry,type);
	        } else {
	        	Path path = Paths.get(fileEntry.getAbsolutePath());
	      		String filenames = path.getFileName().toString();
	      		String[] types=filenames.split("_");

	        	//Get the type(Sentence/Source) from the path

	      		if(types[1].equals(type)){
				
					String filepath=fileEntry.getAbsolutePath();
					//Create the dataset in the LibSVM classifier format
					this.createIndexFiles(indexfile,indexfile1,filepath,types[0]);
	      		}
				
	        }
		}

	}
	        	
	        	

	/* Create the training and testing data set from sentences or source_phrase  in the LibSVM format
	 * 
	 * @param input		the path of directory containing data set
	 * @param type 		sentence or source_phrase
	 */
	public void createIndexFiles(String trainingindexfile,String testindexfile,String inputfile,String polarity) throws IOException{
		
		
		BufferedReader br4 = new BufferedReader(new FileReader(inputfile));
		String row;
		Map<String,Integer> wordcount= new HashMap<String,Integer>();
		//Create a wordcount file of word and their count in the files of same polarity
		int totalcount=0;
		while((row = br4.readLine()) != null){
	         String tokens[]=row.split(" ");
	         
	         for(int s=0;s<tokens.length;s++){
		         int count=1;
		             if (wordcount.containsKey(tokens[s])) { // word already exists in the file. 
		            	 									 //increment the count 
		                 count=wordcount.get(tokens[s])+1;
		                 wordcount.remove(tokens[s]);
		                 wordcount.put(tokens[s],count);
		             } else {
		            	 wordcount.put(tokens[s],count);// new word, add the word and count
		             }
	        	 
	         }
	         totalcount++;
	     }
	
	    
		br4.close();
		
		int training_set_length=(int)(0.75*totalcount);
		int maxwordcount=1;
		for (String name: wordcount.keySet()){
			if(wordcount.get(name)>maxwordcount){
				maxwordcount=wordcount.get(name);
			}
            } 
		
		//Get the index of the word in the wordcount file
		List<String> indexes = new ArrayList<String>(wordcount.keySet());
		BufferedReader br;
		PrintWriter pw2 = new PrintWriter(new FileOutputStream(new File(trainingindexfile), true));
		PrintWriter pw3 = new PrintWriter(new FileOutputStream(new File(testindexfile), true));
		try {
				br = new BufferedReader(new FileReader(inputfile));
		
				String line;
				int score;
				int datasetsize=0;
				line = br.readLine();
				while ((line = br.readLine()) != null) {
					String[] words=line.split(" ");
					StringBuilder sb1=new StringBuilder();
					//Assign a score to the each polarity
					if(polarity.equals("neutral"))
						 score=1;
					 else if(polarity.equals("neutral-uncertain"))
						 score=2;
					 else if(polarity.equals("mixed"))
						 score=3;
					 else if(polarity.equals("mixed-uncertain"))
						 score=4;
					 else if(polarity.equals("positive"))
						 score=5;
					 else if(polarity.equals("positive-uncertain"))
						 score=6;
					 else if(polarity.equals("negative"))
						 score=7;
					 else
						 score=8;
		
					sb1.append(score);
					
					//Add the score wordindex: normalized wordcount to the LibSVM file
					for(int m=0;m<words.length;m++){
						sb1.append(" ");
						if (wordcount.containsKey(words[m])) { 
							int k =indexes.indexOf(words[m]); 
							double scalednumber=(double)((wordcount.get(words[m]))-1)/(double)(maxwordcount-1);
							sb1.append(k+":"+scalednumber);
							sb1.append(" ");
						}
					
					}
					sb1.append("\n");
					
					if(datasetsize<training_set_length){
					
						pw2.write(sb1.toString());
					}
					else{
						pw3.write(sb1.toString());
					}
					datasetsize++;
		
			}
			br.close();
			pw2.close();
			pw3.close();

			}
		catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
	}


	@Override
	public void classification(String s1, String s2) throws Exception {
		// TODO Auto-generated method stub
		
	}






	

}
