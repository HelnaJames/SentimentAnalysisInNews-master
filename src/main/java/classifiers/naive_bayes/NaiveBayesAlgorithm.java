package main.java.classifiers.naive_bayes;

/**
 * NaiveBayesAlgorithm.java
 * Purpose: Naive Bayes Classification
 * @author Helna James Kuttickattu
 */


import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Instance;
import weka.core.converters.ArffLoader;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
public class NaiveBayesAlgorithm{

	/**
	 * Naive Bayes Classification on the data
	 * @param trainingdata path of the training data file
	 * @param testdata     path of the test data file
	 * @return results     the parameters such as accuracy, precision, recall, confusion matrix
	 */

	
	public String[] classification(String trainingdata, String testdata) throws Exception{
		
		 //Training data
		  DataSource trainingsource = new DataSource(trainingdata);	
		  Instances training_instances = trainingsource.getDataSet();
		  
		  //Test data
		  DataSource testsource = new DataSource(testdata);
		  Instances test_instances = testsource.getDataSet();
		  
		  StringToWordVector stringToWord = new StringToWordVector();                        
          stringToWord.setInputFormat(test_instances);
		  Instances testinstances_filtered = Filter.useFilter(test_instances, stringToWord);
		  


		  stringToWord.setInputFormat(training_instances);
		  Instances traininginstances_filtered = Filter.useFilter(training_instances, stringToWord);

			  
		  testinstances_filtered.setClassIndex(testinstances_filtered.numAttributes() - 1);
		  traininginstances_filtered.setClassIndex(traininginstances_filtered.numAttributes() - 1);
			  


		  // train NaiveBayes
		  ArffLoader loader = new ArffLoader();
		  NaiveBayesUpdateable naive_bayes = new NaiveBayesUpdateable();
		  naive_bayes.buildClassifier(traininginstances_filtered);
		  Instance current;
		  while ((current = loader.getNextInstance(traininginstances_filtered)) != null)
			   naive_bayes.updateClassifier(current);

	     // Test the classifier on the test data and predict the class of test data
		 Evaluation eval = new Evaluation(testinstances_filtered);
		 eval.evaluateModel(naive_bayes, testinstances_filtered);
		 System.out.println(eval.toSummaryString("\nNaive_bayes Classifier Results\n======\n ", false));
		 System.out.println(eval.toClassDetailsString());
		 System.out.println(eval.toMatrixString());

		 String results[]=new String[3];
		 results[0]=eval.toSummaryString("\nNaive_bayes Classifier Results\n======\n ", false);
		 results[1]=eval.toClassDetailsString();
		 results[2]=eval.toMatrixString();
		 return(results);

	}
	    
		    
}
	

