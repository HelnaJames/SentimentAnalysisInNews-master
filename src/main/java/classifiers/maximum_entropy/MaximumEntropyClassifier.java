package main.java.classifiers.maximum_entropy;

/**
 * MaximumEntropyClassifier.java
 * Purpose: Maximum Entropy Classification depending on the data
 * @author Helna James Kuttickattu
 */


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import main.java.classifiers.Algorithm;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.doccat.DoccatFactory;

public class MaximumEntropyClassifier implements Algorithm{
	private DocumentCategorizerME myCategorizer;
	
	/**
	 * Create the model trained on the dataset 
	 * 
	 * @param trainingFileName	path of training data
	 * @param modelpath			path of the maximum entropy model
	 */

	public void trainDataset(String trainingFileName,String modelpath) throws Exception {

		//Create the maximum entropy model trained on the data

		MarkableFileInputStreamFactory factory = new MarkableFileInputStreamFactory(new File(trainingFileName));
		ObjectStream<String> lineStream = new PlainTextByLineStream(factory, "UTF-8");
		ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
	    DoccatModel model = DocumentCategorizerME.train("en", sampleStream,TrainingParameters.defaultParams(), new DoccatFactory());
	    OutputStream modelOut = null;
	    File modelFileTmp = new File(modelpath);
	    modelOut = new BufferedOutputStream(new FileOutputStream(modelFileTmp));
	    model.serialize(modelOut);
	    myCategorizer = new DocumentCategorizerME(model);
	}
	
	
	/**
	 * Predict the class of the data by maximum entropy model
	 * 
	 * @param  text[]			String array of sentences
	 * @param  modelFile		path of the maximum entropy model
	 * @return category         class of the sentences
	 */
	
	public String testDataset(String[] text, String modelFile) {
		
	    InputStream modelStream = null;
	    String category=null;
	    try{
			double [] outcomes = myCategorizer.categorize(text);
			category = myCategorizer.getBestCategory(outcomes);
				
	    }
	    catch(Exception e){
	        	e.printStackTrace();
	    }
	    finally{
		        if(null!=modelStream){
		            try {
		                modelStream.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
	    }
		return category;
	}
	
	/**
	 * Maximum Entropy Classification on sentences or source_phrases
	 * 
	 */
	

	@Override
	public void classification() {
	    try {
		    	String choice=null;
				System.out.println("Please enter the option:1)Sentences 2) Source_phrase_level");
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				try {
					choice = reader.readLine();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		    //Sentences as the data option for the maximum entropy classification
		    if (choice.equals("1"))
		    {
		    	
		        String outputModelPath1 = "src/main/resource/MaximumEntropy/model1_tokens.bin";
		        String training_sentence = "src/main/resource/Unigrams/training_sentence_data.csv";
		        String test_sentence = "src/main/resource/Unigrams/test_sentence_data.csv";
		        
		        //Create training and test data set
				String[] files=this.createData(new File(training_sentence),new File(test_sentence),"sentence","tokens");
				
				//Train the data
				this.trainDataset(files[0],outputModelPath1);
				 
				//Test the data
		        this.resultsClassification(files[1],outputModelPath1);
		    }
		    
		  //Source_Phrases as the data option for the maximum entropy classification
		    else if(choice.equals("2")){
		    	
		      
		        String outputModelPath2 = "src/main/resource/MaximumEntropy/model2_tokens.bin";
		        String training_source = "src/main/resource/Unigrams/training_source_data.csv";
		        String test_source = "src/main/resource/Unigrams/test_source_data.csv";
		        
		        //Create training and test data set
		        String[] files1=this.createData(new File(training_source),new File(test_source),"source","tokens");
		        
		        //Train the data
				 this.trainDataset(files1[0],outputModelPath2);
				 
				 //Test the data and  print the results
		        this.resultsClassification(files1[1],outputModelPath2);
		    }
		    
		    else
		    {
		    	System.out.println("Wrong choice");
		    }
	    } catch (Exception e) {
	        	e.printStackTrace();
	  }
	}
	
	/**
	 * Result of the classification. The number of correctly predicted out of the total.
	 * 
	 * @param  testFile		    test data set
	 * @param  Model            The trained maximum entropy model
	 * 
	 */
	
	public void resultsClassification(String testFile,String Model){
		 BufferedReader br1;
		try {
				br1 = new BufferedReader(new FileReader(testFile));
		        String lines;
		        int total_correct=0,total=0;
	        while((lines=br1.readLine())!=null){
	        	String[] text =new String[1];
	        	text[0]=lines;
	        	String [] classlabel=lines.split("\t");
	        	String result=this.testDataset(text, Model);
	        	//Check the label in the dataset and the result is same
	        	if(result.equals(classlabel[0])){
	        		total_correct++;
	        	}
	        	
	        	total++;
	        }
	        //Print the accuracy
	        System.out.println("Accuracy:"+100.0 * total_correct /total);
	        br1.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Create the training and test data set in the  class sentence format
	 * 
	 * @param  trainingdata	   String array of sentences
	 * @param  testdata		   path of the maximum entropy model
	 * @return type            class of the sentences
	 */
	
	public String[] createData(File trainingdata,File testdata,String level,String type) throws IOException{
		 String training_file="src/main/resource/MaximumEntropy/training_"+level+"_"+type+"_labels.txt";
		 String test_file="src/main/resource/MaximumEntropy/test_"+level+"_"+type+"_labels.txt";
		 BufferedReader br=new BufferedReader(new FileReader(trainingdata));
	  	 PrintWriter pw2 = new PrintWriter(new FileOutputStream(new File(training_file), true));
	  	 PrintWriter pw3 = new PrintWriter(new FileOutputStream(new File(test_file), true));
	  	 String line;
	  	 line = br.readLine();
	  	 //Create the training dataset
	  	 while((line = br.readLine()) != null){
	  		 String lines[]=line.split(",");
	  		 StringBuilder sb=new StringBuilder();
	  		 sb.append(lines[1]);
	  		 sb.append("\t");
	  		 sb.append(lines[0]);
	  		 sb.append(System.getProperty("line.separator"));
	  		 pw2.write(sb.toString());
	  	 }

	  	br.close();
	  	pw2.close();
	  	
	  	BufferedReader br1=new BufferedReader(new FileReader(testdata));
	  	line = br1.readLine();
	  	//Create the test dataset
	  	while((line = br1.readLine()) != null){
	  		String lines[]=line.split(",");
	  		StringBuilder sb=new StringBuilder();
	  		sb.append(lines[1]);
	  		sb.append("\t");
	  		sb.append(lines[0]);
	  		sb.append(System.getProperty("line.separator"));
	  		pw3.write(sb.toString());

	   }
	  		
	  pw3.close();
	  br1.close();
      
	  String[] files=new String[2];
	  files[0]=training_file;
	  files[1]=test_file;
		
		
	  return(files);
		
}


	@Override
	public void classification(String s1, String s2) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	

}
