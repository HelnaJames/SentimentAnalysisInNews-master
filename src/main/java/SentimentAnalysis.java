package main.java;


import javax.xml.transform.TransformerFactoryConfigurationError;

import main.java.text_processing.ReadXMLFile2;


public class SentimentAnalysis {
	public static void main(String args[]) throws TransformerFactoryConfigurationError, Exception{
		ReadXMLFile2 r=new ReadXMLFile2();

		//Get the annotated sentences, Pass the folder with Gate XML files
		String folder="src/main/resource/data/";
		r.readFiles(folder);
		
		
	}

}
