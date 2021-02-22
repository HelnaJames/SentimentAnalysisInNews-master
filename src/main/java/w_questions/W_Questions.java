package main.java.w_questions;

/**
 * W_Questions.java
 * Purpose: Get the answers for the 5Wquestions (what, Why, Who, What, When) from the article
 * @author Helna James Kuttickattu
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


import main.java.language_tools.Stanford_Sentiment_Analyzer;


public class W_Questions {

	public static void main(String args[]) throws IOException
	{

		    File file = new File("src/main/resource/5wfiles");
		    
		    String[] directories = file.list(new FilenameFilter() {
		    	  public boolean accept(File current, String name) {
		    	    return new File(current, name).isDirectory();
		    	  }
		    	});


			//Stanford Sentiment Analysis in different dataset
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		    System.out.println("Please enter the type of text to perform Sentiment Analysis:");
		    System.out.println("a.What.txt");
		    System.out.println("b.Who.txt");
		    System.out.println("c.What and Why.txt");
		    System.out.println("d.First user defined number of sentences from what.txt");
		    System.out.println("e.What.txt and Who.txt");

		    String option = null;
	
		    option = reader.readLine();


		    switch (option) {
		       case "a":W_Questions.getStanfordSentiment("a", directories);
		    	   		break;
		    	   		
		     case "b":  W_Questions.getStanfordSentiment("b", directories);
			 		
			         	break;  
			         
		     case "c":  W_Questions.getStanfordSentiment("c", directories);
		    	 
		         		break; 
		         
		     case "d":  W_Questions.getStanfordSentiment("d", directories);
		    	 		break;
		    
		     case "e":  W_Questions.getStanfordSentiment("e", directories);
 	 					break;
		    	 
		     default:
			       System.out.println("Invalid selction");
			       break;
		    	 
		    }


			
	}
	
	/* Get the polarity count of article through Stanford sentiment analysis on 5w answers of each article
	 * 
	 * @param	choice				the 5w data set
	 * @param   directories			the document folders consisting 5w answer data set
	 */
		    
	public static void getStanfordSentiment(String choice, String[] directories){
		    	
			W_Questions_Sentiment w_answers_sentiment=new W_Questions_Sentiment() ;
	    	ArrayList<ArrayList<String>> artcilewithPolaritycounts =new ArrayList<ArrayList<String>>();


			 
			    for(String dir:directories)
			    {

			    
					try {
						String path ="src/main/resource/5wfiles/"+dir;
						String[] article=dir.split("\\.");

						File[] files = new File(path).listFiles();
						for(File file:files)
						{
							String name=file.getName();
							String filepath=file.getAbsolutePath();
							
							if(name.equals("what.txt")&&choice.equals("a")){
								artcilewithPolaritycounts=W_Questions.polaritycountinFile(filepath, choice);
								
							}
							else if(name.equals("who.txt")&&choice.equals("b")){
								artcilewithPolaritycounts=W_Questions.polaritycountinFile(filepath, choice);

							}
							else if((name.equals("why.txt")||name.equals("what.txt"))&&choice.equals("c")){
								artcilewithPolaritycounts=W_Questions.polaritycountinFile(filepath, choice);

							}
							else if(name.equals("what.txt")&&choice.equals("d")){
								artcilewithPolaritycounts=W_Questions.polaritycountinFile(filepath, choice);

							}
							else if((name.equals("who.txt")||name.equals("what.txt"))&&choice.equals("e")){
								artcilewithPolaritycounts=W_Questions.polaritycountinFile(filepath, choice);

							}
								
					  
					}
						
			if(choice.equals("a")){
				w_answers_sentiment.writeFile("a",artcilewithPolaritycounts.get(0),article[0],(artcilewithPolaritycounts.get(1)).get(0),(artcilewithPolaritycounts.get(1)).get(1),(artcilewithPolaritycounts.get(1)).get(2));		
				
			}
			else if(choice.equals("b"))
			{
				w_answers_sentiment.writeFile("b",artcilewithPolaritycounts.get(0),article[0],(artcilewithPolaritycounts.get(1)).get(0),(artcilewithPolaritycounts.get(1)).get(1),(artcilewithPolaritycounts.get(1)).get(2));		

				
			}
			else if(choice.equals("c")){
				w_answers_sentiment.writeFile("c",artcilewithPolaritycounts.get(0),article[0],(artcilewithPolaritycounts.get(1)).get(0),(artcilewithPolaritycounts.get(1)).get(1),(artcilewithPolaritycounts.get(1)).get(2));		

				
			}
			else if(choice.equals("d")){
				w_answers_sentiment.writeFile("d",artcilewithPolaritycounts.get(0),article[0],(artcilewithPolaritycounts.get(1)).get(0),(artcilewithPolaritycounts.get(1)).get(1),(artcilewithPolaritycounts.get(1)).get(2));		

				
			}
			else if(choice.equals("e")){
				w_answers_sentiment.writeFile("e",artcilewithPolaritycounts.get(0),article[0],(artcilewithPolaritycounts.get(1)).get(0),(artcilewithPolaritycounts.get(1)).get(1),(artcilewithPolaritycounts.get(1)).get(2));		

				
			}
				

						
				} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			    }
			
		    	
		    }
	
	/*	PolarityCount - Get the number of sentences with positive, negative or neutral polarity in each document
	 * 
	 * 	@param filepath		the path of file
	 * 
	 * 
	 */
	
	public static ArrayList<ArrayList<String>> polaritycountinFile(String filepath,String choice) throws IOException{


		BufferedReader br = new BufferedReader(new FileReader(filepath));
		Stanford_Sentiment_Analyzer stanfordsentiment=new Stanford_Sentiment_Analyzer();
    	ArrayList<String> sentenceswithStanfordPolarity =new ArrayList<String>();
    	ArrayList<String> polarity =new ArrayList<String>();

    	ArrayList<ArrayList<String>> fileswithPolarity =new ArrayList<ArrayList<String>>();
    	int negative=0,positive=0,neutral=0,i=0;
		String line;

		while((line=br.readLine())!=null)
		{
			
			String stanfordpolarity=stanfordsentiment.getSentimentPolarity(line);
			sentenceswithStanfordPolarity.add(line+":"+stanfordpolarity);
			
			if(stanfordpolarity.equals("Positive")||stanfordpolarity.equals("Very Positive"))
				positive++;
			else if (stanfordpolarity.equals("Negative")||stanfordpolarity.equals("Very Negative"))
				negative++;
			else
				neutral++;
			i++;
			//This consition for getting the first five sentences
			if(choice.equals("d")&&(i==5)){
				break;
			}
		}
		polarity.add(Integer.toString(positive));
		polarity.add(Integer.toString(negative));
		polarity.add(Integer.toString(neutral));

		fileswithPolarity.add(sentenceswithStanfordPolarity);
		fileswithPolarity.add(polarity);
		br.close();
		return fileswithPolarity;
		
	}
}
