package main.java.language_tools;

/**
 * Word_Senses_Terms_babelfy.java
 * Purpose: Get the senses of the word obtained from Babelfy API after Word Sense Disambiguation
 * @author Helna James Kuttickattu
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Word_Senses_Terms_Babelfy {
	
	/*Get the senses of word obtained from Babelfy API by checking the sentence number, filename, and word
	 * 
	 * @param 		word				The input word 
	 * @param		sentence_no			The sentence number in which word appears
	 * @param		filename			The filename
	 * @return		senses				The string array of senses
	 */

	
	public String[] get_Senses(String word, String sentence_no,File filename) throws IOException{
		
		BufferedReader br=new BufferedReader(new FileReader(filename));
		String line=null;
		String[] senses = null;
		while((line=br.readLine())!=null){
			String[] terms=line.split(",");
			String term=terms[1].trim();
			String line_no=terms[0].trim();
			if(sentence_no.equals(line_no)&&term.equals(word)){
				senses= Arrays.copyOfRange(terms, 2, terms.length);
				int i=0;
				for(String words:senses){
					if(words.indexOf('#')>=0){
						String[] sensearray=words.split("#");
						senses[i]=sensearray[0];
						i++;
						
					}
					
				}
				
			}
			
			
		}
		br.close();
		return senses;
		
	}

}
