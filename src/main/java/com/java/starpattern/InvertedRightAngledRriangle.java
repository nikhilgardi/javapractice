package com.java.starpattern;


/*
 This class prints an inverted right-angled triangle star pattern
Example Output for 5 rows:
*****
****
***
**
*
 */
public class InvertedRightAngledRriangle {
	
	public static void main(String [] args)
	{
		for(int i=5;i>=1;i--)
		{
			for(int j=1;j<=i;j++)
			{
				System.out.print("*");
			}
			System.out.println("");
		}
	}

}
