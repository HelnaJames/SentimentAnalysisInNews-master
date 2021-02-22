package main.java.feature_evaluation;

/**
 * InformationGain.java
 * Purpose: Compute the information gain on the features of the training data
 * @author Helna James Kuttickattu
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Attribute;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection; 

public class InformationGain {


	public static void main(String[] args) throws Exception {

		  DataSource source = new DataSource("src/main/resource/Unigrams/training_sentence_negations_data.csv");	
		 //Create the training instances
		  Instances training_instances = source.getDataSet();
		  training_instances.setClassIndex(training_instances.numAttributes() - 1);

		  NaiveBayesUpdateable naivebayes = new NaiveBayesUpdateable();
		  naivebayes.buildClassifier(training_instances);
	        
	        
	      AttributeSelection as = new AttributeSelection();
	      Ranker ranker = new Ranker();
	        
	      InfoGainAttributeEval infoGainAttrEval = new InfoGainAttributeEval();
	      as.setEvaluator(infoGainAttrEval);
	      as.setSearch(ranker);
	      as.setInputFormat(training_instances);
	      training_instances = Filter.useFilter(training_instances, as);
	      Evaluation eval3 = new Evaluation(training_instances);
	      eval3.crossValidateModel(naivebayes, training_instances, 9, new Random(1));
	      int count = 0;
	      // Using HashMap to store the infogain values of the attributes 
	      Map<Attribute, Double> infogainscores = new HashMap<Attribute, Double>();
	      
	      for (int i = 0; i < training_instances.numAttributes(); i++) {
	    	  
	    	  
	        Attribute t_attr = training_instances.attribute(i);
	        double infogain  = infoGainAttrEval.evaluateAttribute(i);
	        if(infogain != 0){
	        	
		        infogainscores.put(t_attr, infogain);
		        count = count+1;
	        }
	     }
	        
	     //then you just access the reversedMap however you like...
	     for (Map.Entry entry : infogainscores.entrySet()) {
	            System.out.println(entry.getKey() + ", " + entry.getValue());
	     }
	
	}
}