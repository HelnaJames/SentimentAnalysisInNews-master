package main.java.language_tools;


/**
 * Compare_Methods.java
 * Purpose: Compare the Sentiment Analysis results on different data 
 * (W questions,First Five Sentences, Whole Article)
 * @author Helna James Kuttickattu
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import opennlp.tools.stemmer.PorterStemmer;

import org.apache.commons.lang3.StringUtils;

import main.java.article.Main_Sentiment_Article;

public class Compare_Methods {
	
/*
 * Compare the Sentiment Analysis results using different methods
 * Method1: Sentiment analysis using Stanford Classifier and Dictionary Methods on 5w answers of article
 * Method2: Sentiment analysis using Stanford Classifier on first five sentences of article
 * Method3: Sentiment analysis using Stanford Classifier on all sentences in article
 * Print the precision, recall and accuracy of each class
 */

 public static void main(String args[])
 {
	 try {
		 
				File f=new File("src/main/resource/5w_Stanford_Sentiment/Sentimentcount_what.csv");
		
				Compare_Methods compareMethods=new Compare_Methods();
				BufferedReader br=new BufferedReader(new FileReader(f));
				String line=null;
				line=br.readLine();
				System.out.format("%32s%32s%32s%32s\n", "Article","Manaully annotatted polarity","5w Polarity","First five sentences","Whole article");
				Main_Sentiment_Article mainSentimentArticle=new Main_Sentiment_Article();
				double i=0,w_polarity=0,leading_sentences_polarity=0,wholearticle_polarity=0;
				double false_negative00=0,true_postive00=0,false_positive00=0;
				double false_negative01=0,true_postive01=0,false_positive01=0;
				double false_negative02=0,true_postive02=0,false_positive02=0;
		
				double false_negative10=0,true_postive10=0,false_positive10=0;
				double false_negative11=0,true_postive11=0,false_positive11=0;
				double false_negative12=0,true_postive12=0,false_positive12=0;
		
				double false_negative20=0,true_postive20=0,false_positive20=0;
				double false_negative21=0,true_postive21=0,false_positive21=0;
				double false_negative22=0,true_postive22=0,false_positive22=0;
		    	
		
		
				while((line=br.readLine())!=null){
					
					String polarityArticle=compareMethods.mainPolarityArticle(line);
					String[] row=line.split(",");
					String document_name=row[0];
					String firstfivesentencemainPolarity=compareMethods.get_FilePolarity("src/main/resource/Article_Stanford_Sentiment/annotated_sentence_sentiment_count.csv",document_name);
					String allSentenceMainPolarity=compareMethods.get_FilePolarity("src/main/resource/Article_Stanford_Sentiment/sentence_sentiment_count.csv",document_name);
				   
					//Get the main polarity(manual polarity) of the article
					String filename="src/main/resource/data/"+document_name+".xml";
					String annotatedmainPolarity=mainSentimentArticle.get_MainSentiment(filename);
					i++;
		
					firstfivesentencemainPolarity = firstfivesentencemainPolarity.trim();
					allSentenceMainPolarity=allSentenceMainPolarity.trim();
					annotatedmainPolarity=annotatedmainPolarity.trim();
					//Compare_Methods.getPOS(document_name);
		
					String name="src/main/resource/5wfiles/"+document_name+".txt"+"/POS.txt";
					polarityArticle=polarityArticle.trim();
					
					//Get the count of the articles with the predicted polarity using methods match 
					//		with manually annotated polarity
					
					if(polarityArticle.equals(annotatedmainPolarity)){
						w_polarity++;
						
					}

					if(firstfivesentencemainPolarity.equals(annotatedmainPolarity)){
						leading_sentences_polarity++;
						
					}
					if(allSentenceMainPolarity.equals(annotatedmainPolarity)){
						wholearticle_polarity++;
						
					}
					
					
					System.out.format("%32s%32s%32s%32s%32s\n",document_name,annotatedmainPolarity,polarityArticle,firstfivesentencemainPolarity,allSentenceMainPolarity);
		
					
					String result00=findTP_TN_FP_FN(polarityArticle,annotatedmainPolarity,"positive");
					if(result00.equals("TP"))
						true_postive00++;
					else if (result00.equals("FN"))
						false_negative00++;
					else
						false_positive00++;
					
					String result01=findTP_TN_FP_FN(polarityArticle,annotatedmainPolarity,"negative");
					if(result01.equals("TP"))
						true_postive01++;
					else if (result01.equals("FN"))
						false_negative01++;
					else
						false_positive01++;
					
					String result02=findTP_TN_FP_FN(polarityArticle,annotatedmainPolarity,"neutral");
					if(result02.equals("TP"))
						true_postive02++;
					else if (result02.equals("FN"))
						false_negative02++;
					else
						false_positive02++;
					
		
					String result10=findTP_TN_FP_FN(firstfivesentencemainPolarity,annotatedmainPolarity,"positive");
					if(result10.equals("TP"))
						true_postive10++;
					else if (result10.equals("FN"))
						false_negative10++;
					else
						false_positive10++;
					
					String result11=findTP_TN_FP_FN(firstfivesentencemainPolarity,annotatedmainPolarity,"negative");
					if(result11.equals("TP"))
						true_postive11++;
					else if (result11.equals("FN"))
						false_negative11++;
					else
						false_positive11++;
					
					String result12=findTP_TN_FP_FN(firstfivesentencemainPolarity,annotatedmainPolarity,"neutral");
					if(result12.equals("TP"))
						true_postive12++;
					else if (result12.equals("FN"))
						false_negative12++;
					else
						false_positive12++;
		
					String result20=findTP_TN_FP_FN(allSentenceMainPolarity,annotatedmainPolarity,"positive");
					if(result20.equals("TP"))
						true_postive20++;
					else if (result20.equals("FN"))
						false_negative20++;
					else
						false_positive20++;
					
					String result21=findTP_TN_FP_FN(allSentenceMainPolarity,annotatedmainPolarity,"positive");
					if(result21.equals("TP"))
						true_postive21++;
					else if (result21.equals("FN"))
						false_negative21++;
					else
						false_positive21++;
					
					String result22=findTP_TN_FP_FN(allSentenceMainPolarity,annotatedmainPolarity,"positive");
					if(result22.equals("TP"))
						true_postive22++;
					else if (result22.equals("FN"))
						false_negative22++;
					else
						false_positive22++;
		
		
				}
			    br.close();
		
				double w_accuracy=(w_polarity/i)*100;
				double leading_sentences_accuracy=(leading_sentences_polarity/i)*100;
				double wholearticle_accuracy=(wholearticle_polarity/i)*100;
				
				//The accuracy of each methods against the main event
				System.out.println("The 5w extraction accuracy\t"+w_accuracy);
				System.out.println("The leading_sentences_accuracy\t"+leading_sentences_accuracy);
				System.out.println("The wholearticle_accuracy\t"+wholearticle_accuracy);
				
				//Precision and Recall Values of Each methods
				System.out.println("The 5w extraction");
				System.out.println("=====================");
				System.out.format("%32s%32s%32s\n", "Class","Precision","Recall");
				System.out.format("%32s%32f%32f\n", "Positive",true_postive00/(true_postive00+false_positive00),true_postive00/(true_postive00+false_negative00));
				System.out.format("%32s%32f%32f\n", "Negative",true_postive01/(true_postive01+false_positive01),true_postive01/(true_postive01+false_negative01));
				System.out.format("%32s%32f%32f\n", "Neutral",true_postive02/(true_postive02+false_positive02),true_postive02/(true_postive02+false_negative02));
		
				//Precision and Recall Values of Each methods
				System.out.println("The Leading Sentences");
				System.out.println("=====================");
				System.out.format("%32s%32s%32s\n", "Class","Precision","Recall");
				System.out.format("%32s%32f%32f\n", "Positive",true_postive10/(true_postive10+false_positive10),true_postive10/(true_postive10+false_negative10));
				System.out.format("%32s%32f%32f\n", "Negative",true_postive11/(true_postive11+false_positive11),true_postive11/(true_postive11+false_negative11));
				System.out.format("%32s%32f%32f\n", "Neutral",true_postive12/(true_postive12+false_positive12),true_postive12/(true_postive12+false_negative12));
				
				//Precision and Recall Values of Each methods
				System.out.println("The Whole Article");
				System.out.println("=====================");
				System.out.format("%32s%32s%32s\n", "Class","Precision","Recall");
				System.out.format("%32s%32f%32f\n", "Positive",true_postive20/(true_postive20+false_positive20),true_postive20/(true_postive20+false_negative20));
				System.out.format("%32s%32f%32f\n", "Negative",true_postive21/(true_postive21+false_positive21),true_postive21/(true_postive21+false_negative21));
				System.out.format("%32s%32f%32f\n", "Neutral",true_postive22/(true_postive22+false_positive22),true_postive22/(true_postive22+false_negative22));
		

		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
 
 /* Find the main polarity of article by finding sentiment class (positive,negative,neutral) with maximum count
  * count of sentiment class - Number of sentences in the article with sentiment
  * 
  * @param line 		the row in CSV file having document and sentiment class counts
  */
 
	 public String mainPolarityArticle(String line)
	 {
		 String[] row=line.split(",");
		 double largest=Double.parseDouble(row[1]);
		 int columnno=1;
		 String polarity=null;
		 for (int i=2;i<4;i++){
			 double number=Double.parseDouble(row[i]);
			 if(number>largest){
				 largest=number;
				 columnno=i;
				 
			 }
		 }
		 
		if(columnno==1){
			polarity="positive";
		}
		else if(columnno==2){
			polarity="negative";
		}
		else{
			polarity="neutral";
		}

		 return(polarity);
	 }
	 
	/* Compare the given filename and the article names in the file.
	 * Retrieve the main polarity of the matched article from the file
	 * 
	 *  @param filename  
	 *  @param file		csv file with document name and sentiment class count of first five sentences or all sentence
	 */
	
	public String get_FilePolarity(String filename, String file){
		BufferedReader br2;
		String line=null,polarity=null;;

		try {
				br2 = new BufferedReader(new FileReader(filename));

				while((line=br2.readLine())!=null){
					String[] row=line.split(",");
					String articlename=row[0];
					if(file.equals(articlename)){
						polarity=this.mainPolarityArticle(line);
		
					}
		}
		
		br2.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return polarity;
	}
	
	/*Find the truepositive, falsepositive, truenegative, falsenegative of
	 * positive, negative, and neutral class
	 * 
	 * @param methodpolarity		Main polarity of the article using methods
	 * @param manualmainpolarity	Manually annoated main polarity of the article
	 * @param polarity				positive, negative, neutral
	 */
	public static String findTP_TN_FP_FN(String methodpolarity,String manualmainpolarity,String polarity){
		
		String result=null;
		if(!(methodpolarity.equals(polarity))&&!(manualmainpolarity.equals(polarity))){
			result="TN";
		}
		if((methodpolarity.equals(polarity))&&(manualmainpolarity.equals(polarity))){
			result="TP";
		}
		
		if(!(methodpolarity.equals(polarity))&&(manualmainpolarity.equals(polarity))){
			result="FN";
		}
		
		if((methodpolarity.equals(polarity))&&!(manualmainpolarity.equals(polarity))){
			result="FP";
		}
		return(result);
	}
	
	/*Get Parts of Speech of sentences
	 * 
	 */
	public static void getPOS(String filename){
		
		Parts_of_Speech p=new Parts_of_Speech();
		File f0=new File("src/main/resource/5wfiles/"+filename+".txt"+"/what.txt");
		File f1=new File("src/main/resource/5wfiles/"+filename+".txt"+"/POS.txt");
		File f2=new File("src/main/resource/5wfiles/"+filename+".txt"+"/why.txt");

		FileWriter fw1;
		try {
				fw1 = new FileWriter(f1.getAbsoluteFile(), true);
			
				BufferedWriter bw1 = new BufferedWriter(fw1);
				BufferedReader br0=new BufferedReader(new FileReader(f0));
				String line=null;
				while((line=br0.readLine())!=null){
					 String s=p.partsofSpeech(line);
					 bw1.write(s.toString());
					
				}
			BufferedReader br1=new BufferedReader(new FileReader(f2));
		
				while((line=br1.readLine())!=null){
					
					String s=p.partsofSpeech(line);
					bw1.write(s.toString());
					
				}
				br0.close();
				//br1.close();
				bw1.close();

		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
