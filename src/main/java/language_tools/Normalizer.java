package main.java.language_tools;

/**
 * Normalizer.java
 * Purpose: Min_Max or Z Score normalization
 * @author Helna James Kuttickattu
 */

public class Normalizer {
	
	/* Min Max Normalization on numbers
	 * @param double[] numbers 				The double array of numbers
	 * @return double[]scaled_numbers	    The numbers after min-max normalization
	 */


	public double[] min_max_Normalization(double [] numbers)
	{

		double max=0, min=0;
		for(int i=0;i<numbers.length;i++)
		{
			max=Math.max(numbers[i],max);
			min=Math.min(numbers[i],min);
		}
		
		double[] scaled_numbers=new double[numbers.length];
		for(int i=0;i<numbers.length;i++)
		{
			scaled_numbers[i]=(numbers[i]-min)/(max-min);
		}

		return scaled_numbers;
		
	}
	
	/*Z Normalization on numbers
	 * @param double[] numbers 				The double array of numbers
	 * @return double[]scaled_numbers		The numbers after Z normalization
	 */
	
	public double[]  z_Normalization(int [] numbers)
	{
		
		int total=0,temp=0;
		for(int i = 0; i < numbers.length; i++){
			   total += numbers[i]; // this is the calculation for summing up all the values
		}

		double mean = total / numbers.length;
		for(int i = 0; i < numbers.length; i++){

			   temp+=Math.pow((numbers[i]-mean),2);
	
		}
		
		double std_dev=Math.sqrt(temp/numbers.length);
		
		double[] scaled_numbers=new double[numbers.length];
		for(int i=0;i<numbers.length;i++){
			scaled_numbers[i]=(numbers[i]-mean)/std_dev;
		}

		return scaled_numbers;
		

		
	}

}
