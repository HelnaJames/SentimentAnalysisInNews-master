package main.java.dictionary_methods;

/**
 * SentiWordNet.java
 * Purpose: Dictionary Sentiment Analysis using SentiWordNet
 * @author Helna James Kuttickattu
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class SentiWordNet {
	

		private Map<String, Double> dictionary;
		
		/* SentiWordNet Dictionary Method
		 * @param pathToSWN	path to the SentiWordNet dictionary
		 */

		public SentiWordNet(String pathToSWN) throws IOException {
			
			dictionary = new HashMap<String, Double>();
			
			HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

			BufferedReader csv = null;
			try {
				
				csv = new BufferedReader(new FileReader(pathToSWN));
				int lineNumber = 0;

				String line;
				while ((line = csv.readLine()) != null) {
					lineNumber++;
					if (!line.trim().startsWith("#")) {
						String[] data = line.split("\t");
						String wordTypeMarker = data[0];

						if (data.length != 6) {
							throw new IllegalArgumentException(
									"Incorrect tabulation format in file, line: "
											+ lineNumber);
						}

						// Calculate synset score as score = PosS - NegS
						Double synsetScore = Double.parseDouble(data[2])
								- Double.parseDouble(data[3]);

						// Get all Synset terms
						String[] synTermsSplit = data[4].split(" ");

						// Get all senses 
						for (String synTermSplit : synTermsSplit) {
							// Get synterm and synterm rank of each sense
							String[] synTermAndRank = synTermSplit.split("#");
							String synTerm = synTermAndRank[0] + "#"
									+ wordTypeMarker;

							int synTermRank = Integer.parseInt(synTermAndRank[1]);

							if (!tempDictionary.containsKey(synTerm)) {
								tempDictionary.put(synTerm,
										new HashMap<Integer, Double>());
							}

							// Put SynTerm, Rank and Score of each sense to the dictionary
							tempDictionary.get(synTerm).put(synTermRank,
									synsetScore);
						}
					}
				}

				// Loop through the senses and get weighted score for each sense
				for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
						.entrySet()) {
					String word = entry.getKey();
					Map<Integer, Double> synSetScoreMap = entry.getValue();

					// Calculate weighted average. Weigh the synsets according to
					// their rank.
					// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
					// Sum = 1/1 + 1/2 + 1/3 ...
					double score = 0.0;
					double sum = 0.0;
					for (Map.Entry<Integer, Double> setScore : synSetScoreMap
							.entrySet()) {
						score += setScore.getValue() / (double) setScore.getKey();
						sum += 1.0 / (double) setScore.getKey();
					}
					score /= sum;

					dictionary.put(word, score);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (csv != null) {
					csv.close();
				}
			}
		}
		
		/* Get the word score from dictionary
		 * @param word		word to check in the dictionary
		 * @param pos       parts of speech
		 */

		public double getWordScore(String word, String pos) {
	        Double value =5000.000;
				if(dictionary.get(word + "#" + pos)!=null)
					{
					 return dictionary.get(word + "#" + pos);
					}
				else
				{
					return value;
				}
		}
		
		public String category(double score){
			String sent=null;
			if(score>=0.75)
	            sent = "strong_positive";
	        else
	        if(score > 0.25 && score<=0.5)
	            sent = "positive";
	        else
	        if(score > 0 && score>=0.25)
	            sent = "weak_positive";
	        else
	        if(score < 0 && score>=-0.25)
	            sent = "weak_negative";
	        else
	        if(score < -0.25 && score>=-0.5)
	            sent = "negative";
	        else
	        if(score<=-0.75)
	            sent = "strong_negative";
			return sent;
		}
		
		/*Get the count of positive and negative words in the sentence
		 * 
		 * @param sentence			Sentence
		 * @param sentiwordnet  	SentiWordNet Dictionary
		 * @return sentimentwords	the count of positive and negative words in the sentence
		 */
		
		public ArrayList<Integer> getPolaritySentence(String sentence,SentiWordNet sentiwordnet) {
			
			ArrayList<Integer> sentimentwords=new ArrayList<Integer>();
			Double value=0.0;
			int positive=0,negative =0;
			 String[] words = sentence.split("\\s+"); 
			 for(String word : words) {
				 
				 
			     String[] parts=word.split("_");
			     
			     //Check the word is positive or negative adjective
			     int adjectives=StringUtils.indexOfAny(parts[1], new String[]{"JJ","JJR","JJS"});
	 	         if(adjectives!=-1){
	 	        	 	value=sentiwordnet.getWordScore(parts[0],"a");
	
	 	        	 	if(!(value==5000.000)){
		
	 	        	 		int adjPositive=StringUtils.indexOfAny(sentiwordnet.category(value), new String[]{"positive","weak_positive","strong_positive"});
	 	        	 		if(adjPositive!=-1){

	 	        	 			positive++;
	 	        	 		}
			 			  
	 	        	 		int adjnegative=StringUtils.indexOfAny(sentiwordnet.category(value), new String[]{"negative","weak_negative","strong_negative"});
	 	        	 		if(adjnegative!=-1){

							  negative++;
	 	        	 		}
	 	        	 	}
	 	        }
	 	        
	 	        
	 	        //Check the word is positive or negative verb
	 	         int verb=StringUtils.indexOfAny(parts[1], new String[]{"VB","VBD","VBG","VBN","VBP","VBZ"});
	 	         
	 	         if(verb!=-1){
		        	
	 	        	 	value=sentiwordnet.getWordScore(parts[0],"v");
	 	        	 	
	 	        	 	if(!(value==5000.000)){
				    	
	 	        	 		int verbPositive=StringUtils.indexOfAny(sentiwordnet.category(value), new String[]{"positive","weak_positive","strong_positive"});
	 	        	 		if(verbPositive!=-1){
	
			 				  positive++;
	 	        	 		}
			 			  
	 	        	 		int verbNegative=StringUtils.indexOfAny(sentiwordnet.category(value), new String[]{"negative","weak_negative","strong_negative"});
	 	        	 		if(verbNegative!=-1){

							  negative++;
	 	        	 		}
	 	        	 	}
	 	         }
		        
	 	         //Check the word is positive or negative noun
		 	       int noun=StringUtils.indexOfAny(parts[1], new String[]{"NN","NNS","NNP","NNPS"});
		 	       
			        if(noun!=-1){
			 	   		   value=sentiwordnet.getWordScore(parts[0],"n");
			 	   		
			 	   		   int nounPositive=StringUtils.indexOfAny(sentiwordnet.category(value), new String[]{"positive","weak_positive","strong_positive"});
			 	   		   if(nounPositive!=-1){
			 	   			   
			 	   			   	positive++;
		 				  
			 	   		   }
		 			  
			 	   		   int nounNegative=StringUtils.indexOfAny(sentiwordnet.category(value), new String[]{"negative","weak_negative","strong_negative"});
			 	   		   if(nounNegative!=-1){
			 	   			   
			 	   			   	negative++;
						  
			 	   		   }
			        }
			        
			        //Check the word is positive or negative adverb
			 	    int adverb=StringUtils.indexOfAny(parts[1], new String[]{"RB","RBR","RBS"});
			 	    
				    if(adverb!=-1){
				    	 
				 	   		value=sentiwordnet.getWordScore(parts[0],"r");
				 	   		if(!(value==5000.000)){
				 	   			
				 	   				int adverbPositive=StringUtils.indexOfAny(sentiwordnet.category(value), new String[]{"positive","weak_positive","strong_positive"});
						 			 if(adverbPositive!=-1){

						 				  positive++;
						 			  }
						 			  
						 			 int adverbNegative=StringUtils.indexOfAny(sentiwordnet.category(value), new String[]{"negative","weak_negative","strong_negative"});
									  if(adverbNegative!=-1){

										  negative++;
									  }
						    }
				     }
		     }
			 			 


		 sentimentwords.add(positive);
		 sentimentwords.add(negative);
		 

		
		return sentimentwords;
		}
		
		
}
