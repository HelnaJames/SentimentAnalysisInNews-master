package main.java.language_tools;

/**
 * Tokenizer.java
 * Purpose: Tokenize the text
 * @author Helna James Kuttickattu
 */

import java.util.ArrayList;
import opennlp.tools.stemmer.PorterStemmer;



public class Tokenizer {

	String text;
	
	/* Tokenize the text into words
	 * @param 	text	    the sentence
	 * @return	tokens		ArrayList of tokens		  
	 */

	public ArrayList<String> tokenize(String text)
	{
		
			ArrayList<String> tokens=new ArrayList<String>();
			String[] array_tokens=text.split(" ");

			 for (String token1:array_tokens) {
	
				 	String token=token1.toString();
				 	StringBuilder sb1 = new StringBuilder();
				 	
				 	for (int i = 0; i < token.length(); i++){
				 		
				     	 if (!Character.isLetter(token.charAt(i))){
				     		 
				     	    token = token.replace(token.charAt(i),' ');
				     	 }
				     	 if (token.charAt(i)==')'||token.charAt(i)=='('||token.charAt(i)=='.'||token.charAt(i)=='"'||token.charAt(i)==','||token.charAt(i)=='\''||token.charAt(i)=='-'){
				     		 
				     		 token.replace(token.charAt(i),' ');
						 
							}
			    	}
				 	
			    	token=token.replaceAll("\\s+","");
			    	PorterStemmer stemmer = new PorterStemmer();
			    	token=stemmer.stem(token);  //stem the word
			    	    
			    	  //If the token  size is greater than 3
			    	  if(token.length()>3){
			    		  
			    		  sb1.append(token);
			    		  tokens.add(sb1.toString());
			    	
			    	  }
	   
		   
		   }
			  
	    	
		return(tokens);

	}
}
