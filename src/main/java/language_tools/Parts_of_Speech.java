package main.java.language_tools;

/**
 * Parts_of_Speech.java
 * Purpose: Get the sentence tagged with parts of speech
 * @author Helna James Kuttickattu
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;

public class Parts_of_Speech {
	
	
	public String partsofSpeech(String sentence){
		
		
		//Create the tokens from the sentence
		//StandardTokenizer tokenizer = new StandardTokenizer();
    	//tokenizer.setReader(new StringReader(sentence));
    	//TokenStream tokenStream = tokenizer;
    	//Create Parts of Speech from the sentence
	    InputStream inputStream;
	    StringBuilder sb2 = new StringBuilder();
	    String taggedSentences=null;
    	try {
					//tokenStream.reset();
					//CharTermAttribute token1 = tokenStream.getAttribute(CharTermAttribute.class);
		
					//while (tokenStream.incrementToken()) {
		    		
		    		String[] tokenarray=sentence.split(" ");
		
					//}
		    		
		    		// tag the sentences with Parts of Speech
					inputStream = new FileInputStream("src/main/resource/PosModel/en-pos-maxent.bin");
					POSModel model = new POSModel(inputStream); 
				    POSTaggerME tagger = new POSTaggerME(model); 
			    	 //Generate Tags
			    	String[] tags = tagger.tag(tokenarray);
				    POSSample sample = new POSSample(tokenarray, tags); 
				    sb2.append(sample);
				    sb2.append('\n');
				    taggedSentences=sb2.toString();
				    //tokenizer.close();
			} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			return(taggedSentences);
			
			
	}
	
}
