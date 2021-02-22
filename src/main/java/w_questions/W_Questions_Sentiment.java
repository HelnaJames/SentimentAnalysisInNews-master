package main.java.w_questions;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


import main.java.language_tools.Normalizer;


public class W_Questions_Sentiment
{
	private PrintWriter pw,writer;
	
	
	
	
	
	public void writeFile(String choice,ArrayList<String> s,String filename,String positive, String negative, String neutral)
	{
		try {
			String writepath=null,writepathcsv=null;
			if(choice.equals("a")){
				writepath="src/main/resource/5w_Stanford_Sentiment/"+filename+"_what.txt";
				writepathcsv="src/main/resource/5w_Stanford_Sentiment/Sentimentcount_what.csv";
			}
			if(choice.equals("b")){
				writepath="src/main/resource/5w_Stanford_Sentiment/"+filename+"_who.txt";
			    writepathcsv="src/main/resource/5w_Stanford_Sentiment/Sentimentcount_who.csv";
			}

			if(choice.equals("c")){
				writepath="src/main/resource/5w_Stanford_Sentiment/"+filename+"_what_why.txt";
				writepathcsv="src/main/resource/5w_Stanford_Sentiment/Sentimentcount_what_why.csv";
			}

			if(choice.equals("d")){
				writepath="src/main/resource/5w_Stanford_Sentiment/"+filename+"_first_five_what.txt";
				writepathcsv="src/main/resource/5w_Stanford_Sentiment/Sentimentcount_first_five_what.csv";
			}

			if(choice.equals("e")){
				writepath="src/main/resource/5w_Stanford_Sentiment/"+filename+"_what_who.txt";
				writepathcsv="src/main/resource/5w_Stanford_Sentiment/Sentimentcount_what_who.csv";
			}



			writer = new PrintWriter(writepath, "UTF-8");
			for(String str: s) {
				  writer.write(str);
				  writer.write("\n");
			}
				writer.close();
		
			pw = new PrintWriter(new FileOutputStream(new File(writepathcsv), true )); 
			
			
			//Call the normalization class to normalize the count  to make it comparable across different terms
			Normalizer n=new Normalizer();
			double numbers[] =new double [3];
			numbers[0]=Double.parseDouble(positive);
			numbers[1]=Double.parseDouble(negative);
			numbers[2]=Double.parseDouble(neutral);
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
	

