package main.java.classifiers.support_vector;

/**
 * SupportVectorMachinesGeneral.java
 * Purpose: Support Vector Machine Classification depending on data set
 * @author Helna James Kuttickattu
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class SupportVectorMachinesGeneral{

	public static void main(String args[]){
		String continueoption=null;
		//Switch menu to choose the algorithms
		do{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		    System.out.println("Please enter the option:");
		    System.out.println("a.Support Vector Machines(Sentences(SVM data format)");
		    System.out.println("b.Support Vector Machines(Unigrams)");
		    System.out.println("c.Support Vector Machines(Bigrams)");
		    System.out.println("d.Support Vector Machines(Trigrams)");
		    System.out.println("e.Support Vector Machines(Unigrams+Negation)");

		    String option = null;
		    try {
		        option = reader.readLine();
		   
			    switch (option) {
	
			    
			     case "a":SupportVectorMachines supportVectorMachines=new SupportVectorMachines();
					     supportVectorMachines.classification();
			       break;      
			     case "b":SupportVectorMachines_Unigrams supportVectorUnigrams=new SupportVectorMachines_Unigrams();
			              supportVectorUnigrams.classification();
			              break;      
			     case "c":SupportVectorMachines_Bigrams supportVectorBigrams=new SupportVectorMachines_Bigrams();
			              supportVectorBigrams.classification();
			              break;  
			              
			     case "d":SupportVectorMachines_Trigrams supportVectorTrigrams=new SupportVectorMachines_Trigrams();
			     		  supportVectorTrigrams.classification();
	                      break;  
			     case "e":SupportVectorMachines_Unigrams_Negation supportVectorUnigramsandFeatures=new SupportVectorMachines_Unigrams_Negation();
			     		  supportVectorUnigramsandFeatures.classification();
	                      break;   
	
			     default:
			       System.out.println("Invalid selction");
			       break;
			     }
		    
		    System.out.println("Do you want to continue?if Yes, Enter Y Else N");
		    BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
		
				continueoption = reader1.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     }while(continueoption.equals("Y"));
		
		
	  
	}

}
