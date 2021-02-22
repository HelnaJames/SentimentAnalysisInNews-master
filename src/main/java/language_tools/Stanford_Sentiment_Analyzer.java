package main.java.language_tools;

/**
 * Stanford_Sentiment_Analyzer.java
 * Purpose: Stanford Sentiment Analysis on the sentence
 * @author Helna James Kuttickattu
 */

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class Stanford_Sentiment_Analyzer {

	/** Stanford Sentiment Analysis on sentences
	 * 
	 * @param   line         the sentence
	 * @return  sentiment    the polarity of the sentence
	 */
	public String getSentimentPolarity(String line)
	{
		
		String sentiment=null;
		System.out.println(line);
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = pipeline.process(line);
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
		  sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
	}
		
		System.out.println(sentiment);

		return sentiment;
		
	
	
	}
}
