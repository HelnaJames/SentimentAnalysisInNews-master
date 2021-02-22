package main.java.article;

/**
 * Main_Sentiment_Article.java
 * Purpose: Get the main polarity of the articles
 * @author Helna James Kuttickattu
 */

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Main_Sentiment_Article {
	
	/*Get the main polarity (polarity of the headline(first sentence in the article)) of the article
	 * @param filename		name of the file
	 * @return polarity		main polarity of the article	
	 */
	
	public String get_MainSentiment(String filename)
	{
		
		File fXmlFile = new File(filename);
		 /*JavaDOM Parser parses the GateXML file to get the feature "polarity" from the annotated sentences 
		  * Get the polarity of the sentences with sentence_id=S1
		  */
		 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder dBuilder;
		 String polarity=null;
		try {
			
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
       	 	NodeList L1 = doc.getElementsByTagName("AnnotationSet");
       	 	String Name=null,Value=null;
       	 	
       	 	
            //Iterate through the AnnotationSet
			 for (int temp = 0; temp < L1.getLength(); temp++) {
				 Element AnnotationSet = (Element) L1.item(temp);
				 //Get the annotation tags in the AnnotationSet
				 NodeList L2 = AnnotationSet.getElementsByTagName("Annotation");
				 for (int i= 0; i < L2.getLength(); i++) {
					 Element Annotation = (Element) L2.item(i);
					 String Type =Annotation.getAttribute("Type");
					 //Check the annotation type is sentence
		             if(Type.equals("sentence")){
		            	 int flag=0;

			        	 //Get the Feature tags inside the Annotation Sentence
		            	 NodeList L3 = Annotation.getElementsByTagName("Feature");
						 for (int j= 0; j < L3.getLength(); j++) {
							 Element Feature = (Element) L3.item(j);
							 //Get the Name and Value of the Feature tags
							 Name = Feature.getElementsByTagName("Name").item(0).getTextContent();
				             Value = Feature.getElementsByTagName("Value").item(0).getTextContent();
				             //If the Value is polarity, then call the method to write the corresponding text to the File
				             if (Name.equals("sentence_id")&&Value.equals("S1"))
				             {
				            	 flag=1;
				            	 

				             }

				             if(flag==1)
				             {
				            	 if (Name.equals("polarity"))
					             {
					            	 polarity=Value;

					             }
				             }
						 }
		             }
				}
			 }
				             
				            	 

			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return(polarity);
	}
}
