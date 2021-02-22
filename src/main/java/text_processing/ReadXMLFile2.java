package main.java.text_processing;

/**
 * ReadXMLFile2.java
 * Purpose: Read XML file, create tokens and unigrams from it.
 * @author Helna James Kuttickattu
 */

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;



import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ReadXMLFile2 {
	
	
	 public void readFiles(String folder) throws TransformerFactoryConfigurationError, Exception{
		 
			ReadXMLFile2 obj = new ReadXMLFile2();
			Create_Tokens createtokens = new Create_Tokens();
			Create_Ngrams createngrams=new Create_Ngrams();
			final File files = new File(folder);
			ArrayList<String> Xmlfiles = new ArrayList<String>();
			//Get all the XML files in the folder to the ArrayList
			Xmlfiles=traverseFiles(files);
			for (String xmlfile : Xmlfiles) {

			   ArrayList<String[]> results=obj.getAnnotatedDataset(xmlfile,folder);
			   for (String[] result:results){
				   
				   this.writeFile(result[0],result[1],result[2]);
				}

			}
			
			//Create tokens and ngrams from the annotated sentences
			createtokens.createTokens();
			createngrams.createNgrams();
			
			
	}

	
	 /* Get the annotated text from XML files
	  * @param	folder				the folder containing Gate XML files
	  * @param  file				the Gate XML file
	  * @return annotatedtexts		the arraylist containing annotated texts, polarity and type (sentence 
	  * 																						or source phrase)
	  */
	public ArrayList<String[]> getAnnotatedDataset(String folder,String file){


		 ArrayList<String[]> annotatedtexts =new ArrayList<String[]>();
		 File fXmlFile = new File(folder.concat(file));
		 //Create the JavaDOM parser to parse the XML documents
		 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder dBuilder;
		 try {
			 
			dBuilder = dbFactory.newDocumentBuilder();
		
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			//Iterate through the TextWithNodes in XML to get the text 
			NodeList T1 = doc.getElementsByTagName("TextWithNodes");
			StringWriter sw = new StringWriter(); 
			Transformer serializer = TransformerFactory.newInstance().newTransformer(); 
			serializer.transform(new DOMSource(T1.item(0)), new StreamResult(sw));	
			String result = sw.toString();
   	 		Element TextWithNodes = (Element) T1.item(0);

		    String finalstring=null;
        	NodeList L1 = doc.getElementsByTagName("AnnotationSet");

        	NodeList T2 = TextWithNodes.getElementsByTagName("Node");
             //Iterate through the AnnotationSet
			for (int temp = 0; temp < L1.getLength(); temp++) {
				 
				 Element AnnotationSet = (Element) L1.item(temp);
				 NodeList L2 = AnnotationSet.getElementsByTagName("Annotation");
				 for (int i= 0; i < L2.getLength(); i++) {
					 
					 Element Annotation = (Element) L2.item(i);
					 String Type =Annotation.getAttribute("Type");
					 //Get the annotated text from the XML files
		             if(Type.equals("sentence")||Type.equals("source_phrase")){
						 	int Startnode = Integer.parseInt(Annotation.getAttribute("StartNode"));
						 	int Endnode = Integer.parseInt(Annotation.getAttribute("EndNode"));
						 	for (int n= 0; n< T2.getLength(); n++){
						 		
						 		Element Nodes = (Element) T2.item(n);
						 		int id = Integer.parseInt(Nodes.getAttribute("id"));
						 		if(id ==Startnode){
			            			 	finalstring=this.getAnnotatedSentences(Startnode, Endnode,result);
			            		 }
	 
		            		 }
						 
						 //Sentence with polarity and type 
		            	 NodeList L3 = Annotation.getElementsByTagName("Feature");
						 for (int j= 0; j < L3.getLength(); j++) {
							 Element Feature = (Element) L3.item(j);
							 String Name = Feature.getElementsByTagName("Name").item(0).getTextContent();
				             String Value = Feature.getElementsByTagName("Value").item(0).getTextContent();
				             if (Name.equals("polarity")){
				            	    String[] b  = new String[3];

				            	    b[0] = Value;
				            	    b[1] = finalstring;
				            	    b[2] = Type;

				            	    annotatedtexts.add(b);

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
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return(annotatedtexts);
			}
				
		/*	Get only the XML files from the folder
		 * 	@param   folder		the folder containing files
		 * 	@return  xml_file	the arraylist containing xml files		
		 */
		public ArrayList<String> traverseFiles(final File folder) {
			
			ArrayList<String> xml_file = new ArrayList<String>();
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					
					traverseFiles(fileEntry);
					} else {
						
					String filename=fileEntry.getName().toString();
					String [] words= filename.split("\\.(?=[^\\.]+$)");
					if(words[1].equals("xml"))
						xml_file.add(filename);
					}
		}
			return(xml_file);
			
	}
		
	/*	Get the annotated sentences by regular expression match
	 *  @param 		node1    		Start node
	 *  @param 		node2   		End node
	 *  @return 	text			text between the start node and end node
	 */
		
	private String getAnnotatedSentences(int node1,int node2,String Xmldata){
		

		 String Starttag ="<Node id=\""+node1+"\"/>";
		 String Endtag="<Node id=\""+node2+"\"/>";
		 final Pattern pattern = Pattern.compile(""+Starttag+"((?s)(.)*)"+Endtag+"");
		 final Matcher matcher = pattern.matcher(Xmldata);
		 if(matcher.find()){
			 
			String result2=matcher.group(1);
			String text=result2.replaceAll("<Node id=\"((?s)[0-9]+)\"/>","");
		 	return(text);
		 }
		 else
			 return(null);
	}
	
	/*	Write the sentences of each polarity to each files
	 * 	@param		polarity	positive, negative, or neutral
	 *  @param		sentence	the text to write
	 *  @param		level		the sentence or source phrase
	 */
	
	private void writeFile(String polarity,String sentence,String level) throws IOException{
		
		String path="src/main/resource/Annotated_texts/";
		File file = new File(path);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            }
        }
		File sentencesofeachPolarity = new File(path+polarity+"_"+level+".txt");
		sentencesofeachPolarity.createNewFile();//Create only the file if it doesn't exist
		//Append to the file
		FileWriter fw = new FileWriter(sentencesofeachPolarity.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write((sentence).trim());
		bw.newLine();
		if (bw != null)
			bw.close();

		if (fw != null)
			fw.close();
	}
	
	/*	Get all the sentences from the file
	 *  @param		folder		
	 *  @param		fileName
	 * 
	 */
	
	public ArrayList<String> getWholeDataSet(String folder,String fileName)
	{
   	 	ArrayList<String> text=new ArrayList<String>();

		 File fXmlFile = new File(folder.concat(fileName));
		 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder dBuilder;

		try {
			
				dBuilder = dbFactory.newDocumentBuilder();
			
				 Document doc = dBuilder.parse(fXmlFile);
				 
				 doc.getDocumentElement().normalize();
		
				 NodeList T1 = doc.getElementsByTagName("TextWithNodes");
		    	 StringWriter sw = new StringWriter(); 
		    	 Transformer serializer = TransformerFactory.newInstance().newTransformer(); 
		    	 serializer.transform(new DOMSource(T1.item(0)), new StreamResult(sw));	
		    	 String result = sw.toString();
		    	 Element TextWithNodes = (Element) T1.item(0);
		       	 NodeList T2 = TextWithNodes.getElementsByTagName("Node");
				
				 for (int n= 0; n< T2.getLength()-1; n++){
					 
					 
		         	Element Node0 = (Element) T2.item(n);
		         	Element Node1=  (Element) T2.item(n+1);
		        	int Startid = Integer.parseInt(Node0.getAttribute("id"));
		        	int Endid = Integer.parseInt(Node1.getAttribute("id"));
		
		        	//If id is equal to id of Annotation, then get the text inside TextWithNodes
		        	String finalstring=this.getAnnotatedSentences(Startid, Endid,result);
		        	finalstring=finalstring.trim();
		
		        	if (finalstring.length()!=0)
		        		text.add(finalstring);
		
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
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		 return(text);
		
	}
		
					
}			  
						
					  




















































				
			  
	

