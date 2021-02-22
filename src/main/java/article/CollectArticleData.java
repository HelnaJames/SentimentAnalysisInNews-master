package main.java.article;

/**
 * CollectArticleData.java
 * Purpose: Get the first five sentences or the whole text in each article from Gate XML files
 * @author Helna James Kuttickattu
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import main.java.text_processing.ReadXMLFile2;
import main.java.language_tools.Tokenizer;

public class CollectArticleData {

	public void collectArticleData(String folder) {
		
		ReadXMLFile2 obj = new ReadXMLFile2();
		ArrayList<String> Xmlfiles = new ArrayList<String>();
		//Get all the XML files in the folder to the ArrayList
		Xmlfiles=obj.traverseFiles(new File(folder));
		
		//Parse the GAteXML files to get the annotated sentence or the all the sentences
		 
		for (String xmlfile : Xmlfiles) {
			String[]file=xmlfile.split("\\.");
			ArrayList<String[]> annotated_sentences= obj.getAnnotatedDataset(folder,xmlfile);
			//Get the annotated sentences ( first five sentences in the article) to the file			
			for (String[]annotated_sentence :annotated_sentences)
			{
				CollectArticleData.write_AnnotatedSentence(annotated_sentence[0],annotated_sentence[1],annotated_sentence[2],file[0]);

			}
			
			//Get all sentences in the article(GateXML file) to the file
			ArrayList<String> sentence_inarticle=new ArrayList<String>();
			sentence_inarticle= obj.getWholeDataSet(folder,xmlfile);
			String s="";
			Character c=null;
			for (String sentence:sentence_inarticle){
					for (int i = 0; i < sentence.length(); i++){
						    	c = sentence.charAt(i); 
						    	if(c!='.'){
							    	s=s+c;
							    }
							    
						    	else{
							    	CollectArticleData.writewholeFile(s,file[0]);
							    	s="";
							    	}
							    }
						    }

		}

	}


				
	/* write the annotated sentences to the file
	 * Create training and test file with unigrams from annotated sentences
	 * 
	 * @param polarity positive, negative or neutral polarity of the sentence
	 * @param filename name of the file
	 * @param text      the sentence
	 * 		
	 */
	
	
					
	public static void write_AnnotatedSentence(String polarity, String text, String type, String filename){

		String path="src/main/resource/Article_Data/"+filename;
		File f =new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
						
		String filepath=path+"/annotated_"+type+"_text.txt";
		String training_file=path+"/training_"+type+"_data.csv";
		String test_file=path+"/test_"+type+"_data.csv";

		PrintWriter pw1,pw2,pw3;
		try {
							
			pw1 = new PrintWriter(new FileOutputStream(new File(training_file), true ));
			pw2 = new PrintWriter(new FileOutputStream(new File(test_file), true ));
			pw3 = new PrintWriter(new FileOutputStream(new File(filepath), true ));
			pw3.write(text);
			pw3.write("\n");
			
		
			/*Tokenize the sentence to get unigrams from the text. Then do the naive bayes classification 
			 * on training and test file for each article
			 */
					
			Tokenizer tokenizer=new Tokenizer();
			ArrayList<String> tokens=tokenizer.tokenize(text);
			int length=tokens.size();
			int training_data_length=(int) (0.75*length);
			for(int i=0;i<length;i++){
				if(i<training_data_length)
					pw1.write(tokens.get(i));
				else
				   	pw2.write(tokens.get(i));
			}
				   	 
			pw1.close();
			pw2.close();
			pw3.close();
						
					
		} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
		} 
						
						
	}
	
	/*Write all sentences in the article from the GateXML file to the file 
	 * @param text      the sentence
	 * @param filename  the filename
	 */
					
	public static void writewholeFile(String text, String filename){
		
		String path="src/main/resource/Article_Data/"+filename;
		File f =new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
		String filepath= path+"/sentence_text.txt";
						
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileOutputStream(new File(filepath), true ));
			pw.write(text);
			pw.write("\n");
			pw.close();

		} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
	}

}
