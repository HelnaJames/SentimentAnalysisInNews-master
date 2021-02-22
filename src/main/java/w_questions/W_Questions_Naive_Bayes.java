package main.java.w_questions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


import main.java.language_tools.Tokenizer;

public class W_Questions_Naive_Bayes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
				//Create the test data(tokens) from the 5w questions before giving to Naive Bayes Classifier
				 File file0 = new File("src/main/resource/5wfiles");
				    
				    String[] directories = file0.list(new FilenameFilter() {
				    	  public boolean accept(File current, String name) {
				    	    return new File(current, name).isDirectory();
				    	  }
				    	});


				    for(String i:directories)
				    {
				    		BufferedReader br;
							String path ="src/main/resource/5wfiles/"+i;

							String writepath=path+"/"+"5w_tokens.csv";
							PrintWriter pw;
							try {
								pw = new PrintWriter(new FileOutputStream((new File(writepath)), true ));
							
							System.out.println(i);
							String[] article=i.split("\\.");

							
							System.out.println(article);
							File[] files = new File(path).listFiles();
							for(File f:files)
							{
								String name=f.getName();
								String filepath=f.getAbsolutePath();
								
						
								String line;
						        //Read  only the why.txt and what.txt in all the directories
								if(name.equals("why.txt")||name.equals("what.txt"))
								{
									System.out.println(name);
									System.out.println(filepath);

									br = new BufferedReader(new FileReader(filepath));
									Tokenizer tokenizer=new Tokenizer();

									while((line=br.readLine())!=null)
									{
										ArrayList<String> w_tokens=tokenizer.tokenize(line);
										for(String token:w_tokens)
										{
											pw.write(token);
										}
										
									}
								}
							}
							pw.close();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				    }
							

			
		
	}
	

	
}
