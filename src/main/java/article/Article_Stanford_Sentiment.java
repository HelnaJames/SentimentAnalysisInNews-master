package main.java.article;

/**
 * Article_Stanford_Sentiment.java
 * Purpose: Get the polarity of the sentences in the article using Stanford Sentiment Analyzer
 *
 * @author Helna James Kuttickattu
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import main.java.language_tools.Normalizer;
import main.java.language_tools.Stanford_Sentiment_Analyzer;

public class Article_Stanford_Sentiment {

	public static void main(String[] args) {
		
		//Write the annotated sentences and the whole sentences to the files
		CollectArticleData getarticledata=new CollectArticleData();
		getarticledata.collectArticleData("src/main/resource/Article_Data");
		
		 File file = new File("src/main/resource/Article_Data");
		    
		    String[] directories = file.list(new FilenameFilter() {
		    	  public boolean accept(File current, String name) {
		    	    return new File(current, name).isDirectory();
		    	  }
		    	});
		   
			String writepathcsv="src/main/resource/Article_Stanford_Sentiment/annotated_sentence_sentiment_count.csv";
			PrintWriter pw;
			try {
				pw = new PrintWriter(new FileOutputStream(new File(writepathcsv), true ));
			
				StringBuilder sb=new StringBuilder();
				sb.append("File");
				sb.append(",");
				sb.append("Positive");
				sb.append(",");
				sb.append("Negative");
				sb.append(",");
				sb.append("Neutral");
				pw.println(sb.toString());
				pw.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 

		    for(String dir:directories)
		    {
		    		//Get the sentiment polarity of the first five annotated sentences in the article
					firstFiveorWholesentence(dir,"annotated_sentence");
					//Get the sentiment polarity of the whole text in the article
					firstFiveorWholesentence(dir,"sentence");
			} 

	}

	/* Get the polarity of the sentences (first five or whole sentences) in the file 
	 * @param    folder file containing annotated texts only or whole sentences
	 * @param    choice  annotated_sentence or sentence 
	 */
			
	
	public static void firstFiveorWholesentence(String folder,String choice)
	{
		try {
	    	BufferedReader br;
	    	ArrayList<String> sentencewithPolarity =new ArrayList<String>();
	    	int negative=0,positive=0,neutral=0;

			Stanford_Sentiment_Analyzer s=new Stanford_Sentiment_Analyzer();
			String path ="src/main/resource/Article_Data/"+folder+"/"+choice+"_text.txt";
			br = new BufferedReader(new FileReader(path));
			String line=null;
			
			while((line=br.readLine())!=null)
			{
				line=line.trim();
				if (!(line.equals(""))) {
						
					String polarity=s.getSentimentPolarity(line);
					sentencewithPolarity.add(line+":"+polarity);
									
				    if(polarity.equals("Positive")||polarity.equals("Very Positive"))
				    	positive++;
					else if (polarity.equals("Negative")||polarity.equals("Very Negative"))
						negative++;
					else
						neutral++;
													
				}

			}
					
			br.close();
			ArrayList<Integer> polarityclassCount=new ArrayList<Integer>();
			polarityclassCount.add(positive);
			polarityclassCount.add(negative);
			polarityclassCount.add(neutral);
			Article_Stanford_Sentiment.writeFile(sentencewithPolarity,folder,polarityclassCount,choice);	
				
							
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/* Write the sentences with polarity (first five sentences or whole sentence) to the file 
	 * depending on the parameter choice
	 * Write the documents name  with sentiment (positive, negative, neutral) class count to the CSV file
	 *
	 *@param sentences_withpolarity		the sentences with their polarity
	 *@param filename					the filename
	 *@param polarityclass_count 		the articles with their sentiment class count
	 *@param choice						The first five sentences or whole article
	 *
	 */
	
	public static void writeFile(ArrayList<String> sentences_withpolarity,String filename,ArrayList<Integer> polarityclass_count,String choice)
	{
		try {
			String path="src/main/resource/Article_Stanford_Sentiment/"+filename;
			File f =new File(path);
			if(!f.exists())
			{
				f.mkdirs();
			}
		
			String writepath=path+"/"+choice+"_sentiment.txt";
			/*Save the document name with count of sentiment class ( first five sentences and whole article) are stored in CSV files
			 * 
			 */
			String writepathcsv="src/main/resource/Article_Stanford_Sentiment/"+choice+"_sentiment_count.csv";

			PrintWriter writer = new PrintWriter(writepath, "UTF-8");
			for(String str: sentences_withpolarity) {
				  writer.write(str);
				  writer.write("\n");
			}
				writer.close();
		
			PrintWriter pw = new PrintWriter(new FileOutputStream(new File(writepathcsv), true )); 
			// MinMax Normalization to normalize the counts
			Normalizer n=new Normalizer();
			double numbers[] =new double[3];
			numbers[0]=polarityclass_count.get(0);
			numbers[1]=polarityclass_count.get(1);
			numbers[2]=polarityclass_count.get(2);
			double [] a=n.min_max_Normalization(numbers);
			StringBuilder sb=new StringBuilder();
			sb.append(filename);
			sb.append(",");
			sb.append(a[0]);
			sb.append(",");
			sb.append(a[1]);
			sb.append(",");
			sb.append(a[2]);
			pw.println(sb.toString());
			pw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}

}
