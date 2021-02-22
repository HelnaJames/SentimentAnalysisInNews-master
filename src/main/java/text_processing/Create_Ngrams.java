package main.java.text_processing;

/**
 * Create_Ngrams.java
 * Purpose: Create Ngrams from the sentneces
 * @author Helna James Kuttickattu
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import main.java.dictionary_methods.SentiWordNet;

import org.apache.commons.lang3.StringUtils;

public class Create_Ngrams {
	
	/* Create N grams from the sentences
	 * 
	 */
	
	public void createNgrams() throws Exception{

		//Get the annotated files in the folder
		this.listFilesForFolder(new File("src/main/resource/Annotated_texts"));
		//Get the bigrams from sentences
		this.createDataSet("sentence","general");
		//Get the bigrams from source phrases
		this.createDataSet("source_phrase","general");
		
		//Get the bigrams and features from sentences
		this.createDataSet("sentence","negationhandling");
		//Get the bigrams and features from source phrases
		this.createDataSet("source_phrase","negationhandling");
		
	}
	
	/*Remove the unnecessary characters like '(',#, ')', before getting n grams from the sentences
	 * @param 		File f   				the file to be 
	 * @param		preprocessedFile		the file with preprocessed sentences
	 */
	public void preprocessing(File file, String preprocessedFile) throws IOException{
	
				BufferedReader br=new BufferedReader(new FileReader(file));
				String path="src/main/resource/Preprocessed_Sentences/"+preprocessedFile+"_preprocessed.txt";
				PrintWriter pw2 = new PrintWriter(new File(path));
				String line;
				
				while ((line = br.readLine()) != null) {
					
					String[] words = line.split(" ");
					
					for (int i = 0; i < words.length; i++){
						
						for(int j=0;j<words[i].length();j++){
							
							if (!Character.isLetter(words[i].charAt(j))) {
								
								words[i]=words[i].replace(words[i].charAt(j),' ');
							}
							
							 if (words[i].charAt(j)==')'||words[i].charAt(j)=='('||words[i].charAt(j)=='.'||words[i].charAt(j)=='"'||words[i].charAt(j)==','||words[i].charAt(j)=='\''||words[i].charAt(j)=='-'){
					    			  
								 words[i]=words[i].replace(words[i].charAt(j),' ');
							 
								}
			
							}
						words[i]=words[i].replaceFirst("^\\s*", "");
					}
					
					StringBuilder sb2 = new StringBuilder();
					for (String value : words) {
						
						sb2.append(value);
						sb2.append(" ");
						sb2.toString();
					}
								
					sb2.append('\n');
					String temp=(sb2.toString().toLowerCase()).replaceFirst("^\\s*", "");
					//Write the preprocesssed sentences to the file
					pw2.write(temp);
			
				}
				
				br.close();
				pw2.close();
				
	}
	
	/*	Get all the files in the folder
	 * 	@param folder	the folder
	 */
	
	public void listFilesForFolder(final File folder) throws IOException {
			    for (final File fileEntry : folder.listFiles()) {
			        if (fileEntry.isDirectory()) {
			            listFilesForFolder(fileEntry);
			        } else {
			        		String filetype=fileEntry.getName().toString();
							String[] parts1 = filetype.split("\\.");
							this.preprocessing(fileEntry,parts1[0]);
			        }
			    }
	
	}
	
	
	/*	Get the Ngrams from the files in the folder and write it for each data type(unigrams,bigrams) 
	 * 		and level(sentnece, source_phrase)
	 * 
	 * 	@param folder				the folder
	 *  @param bigrampaths			the ArrayList containing the paths of the file to write to.
	 *  @param level				sentence or source phrase
	 *  @param type				general (unigrams, bigrams, or trigrams) or  with features 
	 */
	
	public void getNgrams(final File folder,ArrayList<String> bigrampaths,String level,String type) throws IOException{

				
		    	PrintWriter pw2 = new PrintWriter(new FileOutputStream(new File(bigrampaths.get(0)),true));
		    	PrintWriter pw3 = new PrintWriter(new FileOutputStream(new File(bigrampaths.get(1)),true));
				
		    	PrintWriter pw4 = new PrintWriter(new FileOutputStream(new File(bigrampaths.get(2)),true));
		    	PrintWriter pw5 = new PrintWriter(new FileOutputStream(new File(bigrampaths.get(3)),true));
		    	
		    	
		    	PrintWriter pw6 = new PrintWriter(new FileOutputStream(new File(bigrampaths.get(4)),true));
		    	PrintWriter pw7 = new PrintWriter(new FileOutputStream(new File(bigrampaths.get(5)),true));
		    	
		    	
		    	
		    	PrintWriter pw8 = new PrintWriter(new FileOutputStream(new File(bigrampaths.get(6)),true));
		    	PrintWriter pw9 = new PrintWriter(new FileOutputStream(new File(bigrampaths.get(7)),true));
		    	
	        	BufferedReader br3 = new BufferedReader(new FileReader(folder));
	        	String filename=folder.getName().toString();
				String[] polarity=filename.split("\\_");

    	        String partsofSpeechFile="src/main/resource/POS/"+polarity[0]+"_"+level+"_"+"POS.txt";
	        	String line;
	        	ArrayList<String> bigrams=new ArrayList<String>();
	        	int linenumber=0;
	        	
			 
	            while ((line = br3.readLine()) != null) {
	            	//Check the negation in the sentences
	            	 int negation=StringUtils.indexOfAny(line, new String[]{"not","n't","never","no"});
		    	     boolean negationflag=false;
		    	     if(negation!=-1)
		    	        negationflag=true;
		    	     
		    	   //Check the contrast in the sentences
		    	     int contrast=StringUtils.indexOfAny(line, new String[]{"but","although","however","yet","and yet","nevertheless","nonetheless","after all","otherwise","though","on the contrary","in contrast","notwithstanding","on the other hand","at the same time"});
		    	        int contrastflag=0;
		    	        if(contrast!=-1)
		    	        	contrastflag=1;

		    	        String lines = Files.readAllLines(Paths.get( partsofSpeechFile)).get(linenumber);

		    	        //Get the count of positive  and negative words in the sentences
		    			String pathToSWN = "src/main/resource/Lexicons/home/swn/www/admin/dump/SentiWordNet_3.0.0_20130122.txt";

		    			SentiWordNet sentiwordnet = new SentiWordNet(pathToSWN);
		    	        ArrayList<Integer> sentimentwords=sentiwordnet.getPolaritySentence(lines,sentiwordnet);
		            	
		    	        //Create bigrams from sentence
		                bigrams =Create_Ngrams.ngrams(2, line);
		                int bigramscount=bigrams.size();
		                int bigramtrainingsetSize=(int)(0.90*bigramscount);
			        	int fileSize=0;
						for (String value : bigrams) {
							
							value =value.trim();
							StringBuilder sb = new StringBuilder();
		
							String[] words = value.split(" ");
							int bigramlength = words.length;
							if((bigramlength==2)){
								//Write the bigrams or bigrams with features to the file
								if((!words[0].equals(" "))&&(!words[1].equals(" "))){
									
										sb.append(value);
										if(type.equals("general")){
											
											sb.append(',');
											sb.append(polarity[0]);
											sb.append('\n');
											String temp=sb.toString();
											temp.trim();
											
											if(fileSize<bigramtrainingsetSize){
				
												pw2.write(temp);
											}
											else{
				
												pw3.write(temp);
											}
										}
												
										if(type.equals("negationhandling")){
											
											sb.append(',');
											sb.append(negationflag);
											sb.append(',');
											sb.append(contrastflag);
										    sb.append(',');
										    sb.append(sentimentwords.get(0));
										    sb.append(',');
										    sb.append(sentimentwords.get(1));
										    sb.append(',');
											sb.append(polarity[0]);
											sb.append('\n');
											String temp=sb.toString();
											temp.trim();
											if(fileSize<bigramtrainingsetSize){
														
													    pw4.write(temp);
											}
											else{
				
														pw5.write(temp);
											}
										}
												
								}
									
							}
									
								
								fileSize++;
					}
						
				   //Create Trigrams in the same way as Bigrams
						
			        ArrayList<String> trigrams=new ArrayList<String>();
	
					trigrams =Create_Ngrams.ngrams(3, line);
			        int trigramcount=trigrams.size();
			        int trigramtrainingsetSize=(int)(0.75*trigramcount);
				    int trigramfileSize=0;
				    
					for (String trigram: trigrams) {
						
						StringBuilder sb = new StringBuilder();
						trigram=trigram.trim();
								
						String[] words = trigram.split(" ");
						int trigramlength = words.length;
						if((trigramlength==3)){
							
							//write trigrams or trigrams with features to the file
							if((!words[0].equals(" "))&&(!words[1].equals(" ")&&(!words[2].equals(" ")))){
								
								sb.append(trigram);
								if(type.equals("general")){
									
									sb.append(',');
									sb.append(polarity[0]);
									sb.append('\n');
									String temp=sb.toString();
									temp.trim();
									if(trigramfileSize<trigramtrainingsetSize){
	
										pw6.write(temp);
									}
									else{
	
										pw7.write(temp);
									}
								}
								
								if(type.equals("negationhandling")){
									
									sb.append(',');
									sb.append(negationflag);
									sb.append(',');
									sb.append(contrastflag);
								    sb.append(',');
								    sb.append(sentimentwords.get(0));
								    sb.append(',');
								    sb.append(sentimentwords.get(1));
								    sb.append(',');
									sb.append(polarity[0]);
									sb.append('\n');
									String temp=sb.toString();
									temp.trim();
									if(trigramfileSize<trigramtrainingsetSize){
												
										pw8.write(temp);
									}
									else{
	
										pw9.write(temp);
									}
									
								}
										
	
								trigramfileSize++;
			
							  }
							
							
						  }
						
		            }
		            
						linenumber++;	
	            }	
	            
	            br3.close();
	            pw2.close();
	            pw3.close();
	            pw4.close();
	            pw5.close();
	            pw6.close();
	            pw7.close();
	            pw8.close();
	            pw9.close();
	 
	}

	/*Create ngrams from the string
	 * @param 		n			if bigram, then n=2 or n=3 for trigram
	 * @param 		str   		the sentence
	 * @return 		ngrams		Ngrams from the sentence
	 */

	
	public static ArrayList<String> ngrams(int n, String str) {
		
		        ArrayList<String> ngrams = new ArrayList<String>();
		        String[] words = str.split(" ");
		        //if the string is a one word then it should return just the string
		        if(words.length==1){
		        	ngrams.add(str);
		        }
		        else{
		        for (int i = 0; i < words.length - n + 1; i++)
		            ngrams.add(concat(words, i, i+n));
		        
		        }
		        return ngrams;
    }
	
	/*Concatenate the words to create ngrams
	 * @param 		words[]			words from sentence to create ngrams
	 * @param 		start   		
	 * @return 		 				concatenated string
	 */

    public static String concat(String[] words, int start, int end) {
    	
		        StringBuilder sb = new StringBuilder();
		        for (int i = start; i < end; i++)
		            sb.append((i > start ? " " : "") + words[i]);
		        return sb.toString();
    }
    
	/*Create the training and test ngrams files for each polarity(positive, negative, neutral, mixed)
	 * @param 		words[]			words from sentence to create ngrams
	 * @param 		start   		
	 * @return 		 				concatenated string
	 */
    
    public void createDataSet(String level,String type) throws IOException{
    	
    	
		    	String bigrampath1="src/main/resource/Ngrams/training_"+level+"_bigram.csv";
				String bigrampath2="src/main/resource/Ngrams/test_"+level+"_bigram.csv";
		
				String bigrampath3="src/main/resource/Ngrams/training_"+level+"_negation_bigram.csv";
				String bigrampath4="src/main/resource/Ngrams/test_"+level+"_negation_bigram.csv";
		
		    	
		    	String bigrampath5="src/main/resource/Ngrams/training_"+level+"_trigram.csv";
				String bigrampath6="src/main/resource/Ngrams/test_"+level+"_trigram.csv";
		
		    	
		    	
		    	String bigrampath7="src/main/resource/Ngrams/training_"+level+"_negation_trigram.csv";
				String bigrampath8="src/main/resource/Ngrams/test_"+level+"_negation_trigram.csv";
		
				
				PrintWriter pw2 = new PrintWriter(new FileOutputStream(new File(bigrampath1),true));
		    	PrintWriter pw3 = new PrintWriter(new FileOutputStream(new File(bigrampath2),true));
		    	PrintWriter pw4 = new PrintWriter(new FileOutputStream(new File(bigrampath3),true));
		    	PrintWriter pw5 = new PrintWriter(new FileOutputStream(new File(bigrampath4),true));
		    	
		    	PrintWriter pw6 = new PrintWriter(new FileOutputStream(new File(bigrampath5),true));
		    	PrintWriter pw7 = new PrintWriter(new FileOutputStream(new File(bigrampath6),true));
		    	
		    	
		    	PrintWriter pw8 = new PrintWriter(new FileOutputStream(new File(bigrampath7),true));
		    	PrintWriter pw9 = new PrintWriter(new FileOutputStream(new File(bigrampath8),true));
		
		
		    
		    	//Write the header to the csv file
				StringBuilder sb3 = new StringBuilder();
				sb3.append("Ngrams");
				sb3.append(",");
				
		    	 if(type.equals("general")){
		 				sb3.append("Class");
		 				sb3.append("\n");
						pw2.write(sb3.toString());
						pw3.write(sb3.toString());
						pw6.write(sb3.toString());
						pw7.write(sb3.toString());
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
						pw4.write(sb3.toString());
						pw5.write(sb3.toString());
						pw8.write(sb3.toString());
						pw9.write(sb3.toString());
					}
		
		    	ArrayList<String> bigrampaths= new ArrayList<String>();
		    	bigrampaths.add(bigrampath1);
		    	bigrampaths.add(bigrampath2);
		    	bigrampaths.add(bigrampath3);
		    	bigrampaths.add(bigrampath4);
		    	bigrampaths.add(bigrampath5);
		    	bigrampaths.add(bigrampath6);
		    	bigrampaths.add(bigrampath7);
		    	bigrampaths.add(bigrampath8);
		    	
		    	pw2.close();
		    	pw3.close();
		    	pw4.close();
		    	pw5.close();
		    	pw6.close();
		    	pw7.close();
		    	pw8.close();
		    	pw9.close();
		    	//Create the Ngrmas file for ecah polarity
		    	 String[] sentiment_types={"neutral","neutral-uncertain","mixed","mixed-uncertain","positive","positive-uncertain","negative","negative-uncertain"};
				 for(int n=0;n<sentiment_types.length;n++){
						String filename="src/main/resource/Preprocessed_Sentences/"+sentiment_types[n]+"_"+level+"_preprocessed.txt";
						File f=new File(filename);
						if(f.exists()){
							//Get the bigrams
							this.getNgrams(f,bigrampaths,level,type);
						}
						else{
							System.out.println("No file");
						}
						
				 }
		
    }
    
    

}
