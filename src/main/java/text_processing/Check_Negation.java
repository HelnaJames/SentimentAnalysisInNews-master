package main.java.text_processing;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;

import java.io.StringReader;
import java.util.List;

/**
 * Check_Negation.java
 * Purpose: Check the negation by presence of "neg" tag in the dependency parsed sentences
 * @author Helna James Kuttickattu
 */


public class Check_Negation {
	
	private String text;
	
	/* Check the presence of negation
	 * @param	text		the sentence
	 * @param	boolean		the flag to be true if the negation present
	 */

	public boolean checknegation(String text){
		
			String modelPath = DependencyParser.DEFAULT_MODEL;
		    String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";
		    MaxentTagger tagger = new MaxentTagger(taggerPath);
		    DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);

		    DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(this.text));
		    boolean flag=false;
		    for (List<HasWord> sentence : tokenizer) {
		    	List<TaggedWord> tagged = tagger.tagSentence(sentence);
		    	GrammaticalStructure gs = parser.predict(tagged);

		      
		      for(TypedDependency t:gs.allTypedDependencies()){
		    	  
		    	  String s=t.toString();
		    	  if(s.contains("neg")){
		    		  flag=true;
		    		  
		    	  }
		    	  
		    	 
		      }
		      //String[] s=gs.allTypedDependencies()
		      //System.out.println(gs.allTypedDependencies());

		    }
		    return(flag);
	}
		
}

