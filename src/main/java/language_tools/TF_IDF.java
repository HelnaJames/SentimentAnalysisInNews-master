package main.java.language_tools;

/**
 * TF_IDF.java
 * Purpose: Tf Idf values of words on sentences
 * @author Helna James Kuttickattu
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;



public class TF_IDF {
	
		/* Get the tfidf of the words in the documents
		 * 
		 */

		public static void main(String args[]){
			
				File[] files = new File("src/main/resource/Article_Data").listFiles();
			    ArrayList<ArrayList<String>> documents = new ArrayList<ArrayList<String>>();
				ArrayList<HashMap<String, Double>> tf_idf=new ArrayList<HashMap<String, Double>>();
	
	
		    	documents=TF_IDF.getTokensinFile(files,documents);
				TF_IDF tfIdf =new TF_IDF();
	
		    	
		    	for(int i = 0 ; i < documents.size() ; i++) {
		    		
			    	    ArrayList<String> currentList = documents.get(i);
						HashMap<String, Double> tfidfdict = new HashMap<String, Double>();
						
						System.out.println(currentList.size());
		
			    	    //now iterate on the current list
			    	    for (int j = 0; j < currentList.size(); j++) {
			    	    	String word=currentList.get(j);
			    	    	
				    	    double tfidfscore = tfIdf.tfIdf(currentList, documents, word);
					        tfidfdict.put(word, tfidfscore);
		
							tf_idf.add(tfidfdict);
		    	 
		    	}
	    	    
	    	}

	    	System.out.println(tf_idf);
		        

	
		}
		
		/* Get the documents from the folder with all the sentences in the article
		 * @param 	files File[]			all the article subfolders in the main folder
		 * @param 	tokensinecahFile		words in each article
		 * @return	Tokens					Tokens in File 
		 */
		
		
		public static ArrayList<ArrayList<String>> getTokensinFile(File[] files,ArrayList<ArrayList<String>> tokensinFile) {
			
		    for (File file : files) {
		        if (file.isDirectory()) {
		        	
		            getTokensinFile(file.listFiles(), tokensinFile); // Calls same method again.
		        } else {
		        	
		        	if(file.getName().equals("sentence_text.txt")){
			            
			            String filename="src/main/resource/Article_Data/"+file.getParentFile().getName()+"/sentence_text.txt";
			            
			            ArrayList<String> tokens=TF_IDF.getTokens(filename);
			            tokensinFile.add(tokens);

		            
		        	}
		        }
		       

		    }
			return tokensinFile;
			
		}
		
		public static  ArrayList<String> getTokens(String filename){
			
	    	ArrayList<String> tokens =new ArrayList<String>();

			try {
				BufferedReader br=new BufferedReader(new FileReader(filename));
				String line=null;
				//Tokenize the sentences and add it to the TokenStream
				
				while((line=br.readLine())!= null){
			    	StandardTokenizer tokenizer = new StandardTokenizer();
					tokenizer.setReader(new StringReader(line));
			    	TokenStream tokenStream = tokenizer;
			    	//Remove the stop words
			    	//tokenStream = new StopFilter(tokenStream, StandardAnalyzer.STOP_WORDS_SET);
			    	tokenStream.reset();
			    	CharTermAttribute token1 = tokenStream.getAttribute(CharTermAttribute.class);

			    	 while (tokenStream.incrementToken()) {

						    String token=token1.toString();
							tokens.add(token);
						 }
					
			    		tokenizer.close();
				}
				br.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return(tokens);
			
			
		}
		
		/*Get the term frequency of the word in the document
		 * @param doc	The document or article
		 * @param term	The word
		 * 
		 * @return term frequeny of the word
		 */
		

	     public double tf(ArrayList<String> doc, String term) {
	         double result = 0;
	         for (String word : doc) {
	             if (term.equalsIgnoreCase(word))
	                 result++;
	         }
	         return result / doc.size();
	     }
	     
			/*Get the Inverse Document frequency of the word in the document
			 * @param docs	The documents in the folder
			 * @param term	The word
			 * 
			 * @return Inverse Document Frequency of the word
			 */

	     public double idf(ArrayList<ArrayList<String>> docs, String term) {
	    	 
	         double n = 0;
	         for (ArrayList<String> doc : docs) {
	             for (String word : doc) {
	                 if (term.equalsIgnoreCase(word)) {
	                     n++;
	                     break;
	                 }
	             }
	         }
	         return Math.log(docs.size() / n);
	     }

	     	/*Get the TF-IDF of the word in the document
			 * @param doc	The document
			 * @param docs	The main folder containing all documents
			 * 
			 * @return term 
			 */


	     public double tfIdf(ArrayList<String> doc, ArrayList<ArrayList<String>> docs, String term) {
	         return tf(doc, term) * idf(docs, term);

	     }
	
	
}