package main.java.dependancy_parsing;

/**
 * DependancyParsing.java
 * Purpose: DependancyParsing on the sentences
 * @author Helna James Kuttickattu
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;

public class DependancyParsing {
	/* Dependancy Parsing on the files containing sentences
	 * @param filename	path of the file
	 */

	public void dependancyParsing(File filename) throws IOException{
		
		String modelPath = DependencyParser.DEFAULT_MODEL;
	    String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";
	    MaxentTagger tagger = new MaxentTagger(taggerPath);
	    DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);
	    BufferedReader br=new BufferedReader(new FileReader(filename));
	    String line=null;
	    ArrayList<String> dependencyparsedSentences=new ArrayList<String>();
        while((line=br.readLine())!=null){
        	DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(line));
    	    for (List<HasWord> sentence : tokenizer) {
    	      List<TaggedWord> tagged = tagger.tagSentence(sentence);
    	      GrammaticalStructure gs = parser.predict(tagged);


    	      System.out.println(gs.allTypedDependencies());
    	      dependencyparsedSentences.add((gs.allTypedDependencies()).toString());
        	
    	    }
	    

        }
        br.close();
		}
	

}
