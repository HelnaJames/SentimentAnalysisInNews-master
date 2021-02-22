package main.java.language_tools;

/**
 * Word_Senses_Sentences.java
 * Purpose: Get the senses of the words in the sentences
 * @author Helna James Kuttickattu
 */

import it.uniroma1.lcl.babelnet.InvalidBabelSynsetIDException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;


public class Word_Senses_Sentence {
	
	/*  Get the senses of words from sentences in the file 
	 *  
	 */
	public static void main(String args[]) throws IOException, InvalidBabelSynsetIDException{
		
		Word_Senses_Sentence word_senses=new Word_Senses_Sentence();
		BufferedReader files=new BufferedReader(new FileReader(new File("src/main/resource/temporary_file_list.txt")));
		String line=null;
		
		while((line=files.readLine())!=null){
		
			String[] file=line.split("_");
			String writepath="src/main/resource/5wfiles_Word_Sense_Disambiguation/"+file[0]+".csv";

			String whatfile ="src/main/resource/5wfiles/" + line+"/what.txt";
			int index=1;
			
			int line_index=word_senses.save_Word_Senses(whatfile, writepath, index);

			//String whyfile ="src/main/resource/5wfiles/" + line +"/why.txt";
			
			//word_senses.save_Word_Senses(whyfile, writepath, line_index);


		}
		
		files.close();
		
	}
	

	/* Write the word, senses after Word Sense Disambiguation, and line index of the word to the file 
	 * @param  file		the file with words to be disambiguated		
	 * @param  outpath   Write the results to the file
	 * @param  index     the line index
	 * @return index	 the last line index
	 */
	public  int save_Word_Senses(String file, String outfile,int index) throws IOException, InvalidBabelSynsetIDException{
		
		String line=null;
		BufferedReader br=new BufferedReader(new FileReader(new File(file)));
		WordNet_Sense_Disambiguation senses_disambiguation=new WordNet_Sense_Disambiguation();
		PrintWriter pw = new PrintWriter(new FileOutputStream(
			    new File(outfile), 
			    true /* append = true */)); 

		while((line=br.readLine())!=null){
			
			HashMap<String,ArrayList<String>> senses_dictionary=senses_disambiguation.Word_Sense_Disambiguation(line);
			
			for (HashMap.Entry<String, ArrayList<String>> entry : senses_dictionary.entrySet()) {
			    String word = entry.getKey();
			    ArrayList<String> senses  = entry.getValue();
			    StringBuilder sb=new StringBuilder();
			    sb.append(index);
			    sb.append(',');
			    sb.append(word);
			    
			    for(String sense:senses){
			    	sb.append(',');
			    	sb.append(sense);
			    }
			    sb.append('\n');
			    pw.write(sb.toString());

			}
			
			index++;
		}
		
		br.close();
		pw.close();
		
		return index;
	}

}