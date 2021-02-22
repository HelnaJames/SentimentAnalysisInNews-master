package main.java.language_tools;

/**
 * WordNet_Sense_Disambiguation.java
 * Purpose: Get the senses of the word obtained from Babelfy API after Word Sense Disambiguation
 * @author Helna James Kuttickattu
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.uniroma1.lcl.babelfy.commons.annotation.*;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma1.lcl.babelnet.InvalidBabelSynsetIDException;

import it.uniroma1.lcl.jlt.util.Language;





public class WordNet_Sense_Disambiguation {
	
	/*Word sense disambiguation on the words from sentence
	 * 
	 * @param        inputText	        the sentence
	 * @return		 word_senses		the words and their senses
	 */
	public HashMap<String,ArrayList<String>> Word_Sense_Disambiguation(String inputText) throws IOException, InvalidBabelSynsetIDException{
		
		BabelNet bn = BabelNet.getInstance();
		Babelfy bfy = new Babelfy();
		HashMap<String,ArrayList<String>> word_senses= new HashMap<String,ArrayList<String>>();
	    List<SemanticAnnotation> annotations = bfy.babelfy(inputText, Language.EN);
	    System.out.println("inputText: "+inputText+"\nannotations:");
	    for (SemanticAnnotation annotation : annotations)
	    {
	    	//splitting the input text using the CharOffsetFragment start and end anchors
	    	String frag = inputText.substring(annotation.getCharOffsetFragment().getStart(),
	    		annotation.getCharOffsetFragment().getEnd() + 1);

	    	
	    	// Gets a BabelSynset from a concept identifier (Babel synset ID).
	    	BabelSynset by = bn.getSynset(new BabelSynsetID(annotation.getBabelSynsetID()));
	    	

	    	       
	    	// Gets the senses contained in this BabelSynset.
	    	ArrayList<String> senses_list=new ArrayList<String>();
	    	List<BabelSense> senses = by.getSenses();
	    	
	    	for(BabelSense b:senses){
	    		senses_list.add(b.getSenseString());

	    	}

	    	word_senses.put(frag,senses_list);

	    	
	    }
		return word_senses;
	}
}
