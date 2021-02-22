package main.java.text_processing;

/**
 * Create_Tokens.java
 * Purpose: Create Tokens from sentneces
 * @author Helna James Kuttickattu
 */


import main.java.dictionary_methods.SentiWordNet;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.stemmer.PorterStemmer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/* Create the input data set for training
 * Input data set format -{unigrams, labels}, {unigrams, negation,label}
 */
public class Create_Tokens {
	
	/*Create tokens from sentences
	 * 
	 */
		
	public void createTokens() throws Exception{
		
			Create_Tokens createTokens=new Create_Tokens();
			final File annotatedtexts = new File("src/main/resource/Annotated_texts");
			try {
				
					createTokens.getFiles(annotatedtexts);

					//Create training and test file with unigrams,trigrams, etc from sentences 
					createTokens.createDataset("sentence","src/main/resource/Unigrams/sentence_tokens.csv","src/main/resource/Unigrams/training_sentence_data.csv","src/main/resource/Unigrams/test_sentence_data.csv","general");
					createTokens.createDataset("source","src/main/resource/Unigrams/source_tokens.csv","src/main/resource/Unigrams/training_source_data.csv","src/main/resource/Unigrams/test_source_data.csv","general");
					
					//Create training and test file with unigrams,trigrams, etc and features from sentences 
					createTokens.createDataset("sentence","src/main/resource/Unigrams/sentence_negations_tokens.csv","src/main/resource/Unigrams/training_sentence_negations_data.csv","src/main/resource/Unigrams/test_sentence_negations_data.csv","evaluation");
					createTokens.createDataset("source","src/main/resource/Unigrams/source_negations_tokens.csv","src/main/resource/Unigrams/training_source_negations_data.csv","src/main/resource/Unigrams/test_source_negations_data.csv","evaluation");
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		        
	        
			
		}
	
	/*Tokenize and Preprocess the text to remove unwanted characters from the text
	 * @param	filename		the name of the file
	 * @param	datatype		unigrams, bigrams, or trigrams 
	 * 
	 */
		

	  public void tokenizePreprocessText(File filename,String datatype, String level) throws Exception{
		  
		 
	      String path="src/main/resource/Unigrams/";
	      
	    	File file = new File(path);
	    	if (!file.exists()) {
	    	   if (file.mkdir()) {
	    	         System.out.println("Directory is created!");
	    	            }
	    	        }
	    	File ngrams = new File(path+level+"_tokens.csv");
	    	ngrams.createNewFile();
	    	
	    	File ngramswithFeatures = new File(path+level+"_negations_tokens.csv");
	    	ngramswithFeatures.createNewFile();//Create only the file if it doesn't exist
		    FileWriter fw = new FileWriter(ngrams.getAbsoluteFile(), true);
	  		BufferedWriter bw = new BufferedWriter(fw);

		    FileWriter fw1 = new FileWriter(ngramswithFeatures.getAbsoluteFile(), true);
	  		BufferedWriter bw1 = new BufferedWriter(fw1);
	  		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
	    	    String line=null;
	    	    int linenumber=0;

	    	    
	    	    while ((line=br.readLine())!= null) {
	    	    	
		    	    StringBuilder sb = new StringBuilder();
	    	        sb.append(line);
	    	        sb.append(System.lineSeparator());
	    	        String sentence = (sb.toString()).toLowerCase();

	    	        //Check the negation in the sentence
	    	        int negation=StringUtils.indexOfAny(sentence, new String[]{"not","n't","never","no"});
	    	        boolean negationflag=false;
	    	        if(negation!=-1)
	    	        	negationflag=true;
	    	        
	    	        //Check the contrast in the sentence
	    	        int contrast=StringUtils.indexOfAny(sentence, new String[]{"but","although","however","yet","and yet","nevertheless","nonetheless","after all","otherwise","though","on the contrary","in contrast","notwithstanding","on the other hand","at the same time"});
	    	        String contrastflag="No";
	    	        if(contrast!=-1)
	    	        	contrastflag="Yes";
	    	        
	    	        //The parts of speech file
	    	        String partsofSpeechfile="src/main/resource/POS/"+datatype+"_"+level+"_"+"POS.txt";

	    	        
	    	        String lines = Files.readAllLines(Paths.get(partsofSpeechfile)).get(linenumber);

     
	    	        //Get the count of positive adjectives and negative words in the sentences
	    			String pathToSWN = "src/main/resource/Lexicons/home/swn/www/admin/dump/SentiWordNet_3.0.0_20130122.txt";

	    			SentiWordNet sentiwordnet = new SentiWordNet(pathToSWN);
	    	        ArrayList<Integer> sentimentwords=sentiwordnet.getPolaritySentence(lines,sentiwordnet);
	    	        	
			    	StandardTokenizer tokenizer = new StandardTokenizer();
			    	tokenizer.setReader(new StringReader(sentence));
			    	TokenStream tokenStream = tokenizer;
			    	
			    	//Remove the stop words
			    	//tokenStream = new StopFilter(tokenStream, StandardAnalyzer.STOP_WORDS_SET);
			    	tokenStream.reset();
			    	ArrayList<String> tokens =new ArrayList<String>();
			    	CharTermAttribute token1 = tokenStream.getAttribute(CharTermAttribute.class);


					 while (tokenStream.incrementToken()) {

					    String token=token1.toString();
						tokens.add(token);
					 }
					 
					 //Write the sentence tagged with POS to the file
			    	 this.getpartsofSpeech(tokens,datatype,level);

					 for(String token:tokens){

						    StringBuilder ngramsStringBuilder = new StringBuilder();
						    StringBuilder ngramswithfeaturesStringBuilder = new StringBuilder();
						    
						    for (int i = 0; i < token.length(); i++){
						    	
						     	 if (!Character.isLetter(token.charAt(i))){
						     	    token = token.replace(token.charAt(i),' ');
						     	 }
						     	 if (token.charAt(i)==')'||token.charAt(i)=='('||token.charAt(i)=='.'||token.charAt(i)=='"'||token.charAt(i)==','||token.charAt(i)=='\''||token.charAt(i)=='-'){
						     		 token.replace(token.charAt(i),' ');
								 
									}
						    }
			    	    	token=token.replaceAll("\\s+","");
			    	    	
			    	    	//Stem the token
					    	PorterStemmer stemmer = new PorterStemmer();
					    	token=stemmer.stem(token);  //stem the word
					    	    
					    	 //If the token  size is greater than 3
					    	 if(token.length()>3){
					    		  
					    		  //Write ngrams to the file
					    		  ngramsStringBuilder.append(token);
					    		  ngramsStringBuilder.append(',');
					    		  ngramsStringBuilder.append(datatype);
					    		  ngramsStringBuilder.append('\n');
					    		  bw.write(ngramsStringBuilder.toString());
					    		  
					    		  //Write the ngrams with features to the file
					    		  ngramswithfeaturesStringBuilder.append(token);
					    		  ngramswithfeaturesStringBuilder.append(',');
					    		  ngramswithfeaturesStringBuilder.append(negationflag);
					    		  ngramswithfeaturesStringBuilder.append(',');
					    		  ngramswithfeaturesStringBuilder.append(contrastflag);
	
					    		  ngramswithfeaturesStringBuilder.append(',');
					    		  ngramswithfeaturesStringBuilder.append(sentimentwords.get(0));
					    		  ngramswithfeaturesStringBuilder.append(',');
					    		  ngramswithfeaturesStringBuilder.append(sentimentwords.get(1));
					    		  ngramswithfeaturesStringBuilder.append(',');
					    		  ngramswithfeaturesStringBuilder.append(datatype);
					    		  ngramswithfeaturesStringBuilder.append('\n');
					    		  bw1.write(ngramswithfeaturesStringBuilder.toString());
			    	    	 }
		       
		    	   
					 }
					  
			    	 tokenStream.close();
			    	 linenumber++;
			    	 
	    	    }
	    	    
	    	    
	    	    if (bw != null)
	  				bw.close();

	  			if (fw != null)
	  				fw.close();
	  			if (bw1 != null)
		  			bw1.close();

		  		if (fw1 != null)
		  			fw1.close();
				br.close();

	    	   
	       }
	       catch (IOException e) {
				e.printStackTrace();
			}
	 
	      
	}
	  
	  /* Get the Parts of speech of from sentences
	   * @param 	tokens	the arraylist of tokens from sentences
	   * @param		type	the unigrams, bigrams, or trigrams
	   * @param     level   sentence or source phrase
	   * 
	   */
	  
	  
	  public void getpartsofSpeech(ArrayList<String> tokens,String type, String level){
      
		    InputStream inputStream;
			try {
				
				
						inputStream = new FileInputStream("src/main/resource/PosModel/en-pos-maxent.bin");
						POSModel model = new POSModel(inputStream); 
						POSTaggerME tagger = new POSTaggerME(model); 
						
						String[] tokenarray = new String[tokens.size()];
						tokenarray = tokens.toArray(tokenarray);
			    	   
				        String[] tags = tagger.tag(tokenarray);
				        
				        POSSample sample = new POSSample(tokenarray, tags); 
		
		
				    	String partsofSpeechpath="src/main/resource/POS/";
				    	File partsofSpeechfolder = new File(partsofSpeechpath);
				    	if (!partsofSpeechfolder.exists()) {
				    		
				    	   if (partsofSpeechfolder.mkdir()) {
				    	         System.out.println("Directory is created!");
				    	            }
				    	}
				    	
				    	
				    	File partsofSpeechfile  = new File(partsofSpeechfolder+type+"_"+level+"_POS.txt");
				    	partsofSpeechfile .createNewFile();//Create only the file if it doesn't exist 
				    	
				    	FileWriter partsofSpeechFileWriter = new FileWriter(partsofSpeechfile .getAbsoluteFile(), true);
				  		BufferedWriter partsofSpeechWriter = new BufferedWriter(partsofSpeechFileWriter);
					    StringBuilder sb2 = new StringBuilder();
					    sb2.append(sample);
					    sb2.append('\n');
					    partsofSpeechWriter.write(sb2.toString());
					    partsofSpeechWriter.close();
			    		
			    		
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	  }
	  
	  
	  
	 /*Get all the files in the folder
	  * @param folder
	  * 
	  * 
	  */
	 public void getFiles(final File folder) throws Exception {
		 
		 	for (final File fileEntry : folder.listFiles()) {
	 				
		 		if (fileEntry.isDirectory()) {
	 					
	 					getFiles(fileEntry);
	 					
	 			} else {
	 				
	 				//Get the annotated text files from the folder
	 				String s=fileEntry.getName().toString();
	 				String[] polarity = s.split("\\_");
	 				String[] level = polarity[1].split("\\.");
	 				//Tokenize the annotated sentences
	 				this.tokenizePreprocessText(fileEntry,polarity[0],level[0]);
	 			}
	 		}
		 	
	 }
	 		
	
	 /*Create the training and test data set
	  * 
	  * @param 			level					sentence or source phrase
	  * @param			filename    			the files containing ngrams or ngrams with features and their polarity
	  * @param			trainingdatafile		the training data
	  * @param			testdatafile			the test file
	  * @param			type					unigrams, bigrams, trigrams
	  */
	 		
	 		
	 public void createDataset(String level,String filename,String trainingdatafile,String testdatafile,String type) throws Exception{
	 			
		 	//Initialize the array with sentiment types

	 		String[] sentiment_polarities={"neutral","neutral-uncertain","mixed","mixed-uncertain","positive","positive-uncertain","negative","negative-uncertain"};
	 		String lines;
	 		PrintWriter pw2 = new PrintWriter(new File(trainingdatafile));
	 		PrintWriter pw3 = new PrintWriter(new File(testdatafile));
	 		 //Write the header to the csv file
		 	StringBuilder sb3 = new StringBuilder();
		 	sb3.append("Unigrams");
		 	sb3.append(",");

		 	if(type.equals("general")){
		 		
		 		sb3.append("Class");
	 			sb3.append("\n");
	 			pw2.write(sb3.toString());
	 			pw3.write(sb3.toString());
	 		}
		 	
		 	else{
		 		
	 			sb3.append("Negation");
	 			sb3.append(",");
	 			sb3.append("Contrast");
	 			sb3.append(",");
	 			sb3.append("Positive entities");
	 			sb3.append(",");
	 			sb3.append("Negative entities");
	 			sb3.append(",");
	 			sb3.append("Class");
	 			sb3.append("\n");
	 			//System.out.println(sb3.toString());
	 			pw2.write(sb3.toString());
	 			pw3.write(sb3.toString());
	 		}
	 				 
		 	for(int n=0;n<sentiment_polarities.length;n++){
		 		
		 		BufferedReader br = new BufferedReader(new FileReader(filename));
	 			ArrayList<String> ngramswithsamePolarity = new ArrayList<String>();
	 			ArrayList<String> negation = new ArrayList<String>();
	 			ArrayList<String> contrast = new ArrayList<String>();
	 			ArrayList<String> adjectives_positive_no = new ArrayList<String>();
	 			ArrayList<String> adjectives_negative_no = new ArrayList<String>();
	 					 //Get the tokens with same polarity to the same_type ArrayList
	 			while ((lines = br.readLine()) != null) {
	 				
	 				String[] columnvalue=lines.split(",");
	 						
	 				if(type.equals("general")){
	 					
	 					if(columnvalue[1].equals(sentiment_polarities[n])){
	 							ngramswithsamePolarity .add(columnvalue[0]);
	 							}
	 				}
	 						
	 				if(type.equals("evaluation")){
	 					
	 					if(columnvalue[6].equals(sentiment_polarities[n])){
	 						
	 								ngramswithsamePolarity.add(columnvalue[0]);
	 								negation.add(columnvalue[1]);
	 								contrast.add(columnvalue[2]);
	 								adjectives_positive_no.add(columnvalue[4]);
	 								adjectives_negative_no.add(columnvalue[5]);
	 						}
	 				}
	 						
	 			}
	 			
	 			int fileSize=ngramswithsamePolarity.size();
	 			int training_set_rowcount=(int)(0.75*fileSize);
	 			int rownumber=0;
	 				 
	 				 
	 			//Write the tokens with same polarity(75%) to the training dataset and 25% to the test dataset
	 			for (String ngrams : ngramswithsamePolarity){
	 				
	 				StringBuilder sb2 = new StringBuilder();
	 				if(type.equals("general")){
	 					
	 					if(rownumber<training_set_rowcount){
	 						
	 						sb2.append(ngrams);
	 						sb2.append(",");
	 						sb2.append(sentiment_polarities[n]);
	 						sb2.append("\n");
	 						pw2.write(sb2.toString());
	 					}
	 					
	 					else{
	 							
	 						sb2.append(ngrams);
	 						sb2.append(",");
	 						sb2.append(sentiment_polarities[n]);
	 						sb2.append("\n");
	 						pw3.write(sb2.toString());
	 						}
	 					}
	 				
	 				if(type.equals("evaluation")){
	 					
	 						sb2.append(ngrams);
	 						sb2.append(",");
	 						sb2.append(negation.get(rownumber));
	 						sb2.append(",");
	 						sb2.append(contrast.get(rownumber));
	 						sb2.append(",");
	 						sb2.append(adjectives_positive_no.get(rownumber));
	 						sb2.append(",");
	 						sb2.append(adjectives_negative_no.get(rownumber));
	 						sb2.append(",");
	 						sb2.append(sentiment_polarities[n]);
	 						sb2.append("\n");
	 						if(rownumber<=training_set_rowcount){
		 						pw2.write(sb2.toString());
		 					}
		 					else{
		 						pw3.write(sb2.toString());
		 					}
	 				}
	 				
	 						rownumber++;
	 			}

	 			br.close();
	 			
	 	}
		 	
	 	pw2.close();
	 	pw3.close();
	 				
	}
	 			

}
				

	



