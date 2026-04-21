package com.java.starpattern;

/*
This class prints a right-angled triangle aligned to the right (mirrored).
Example Output for height = 5:
    *
   **
  ***
 ****
*****
*/
public class MirroredRightAngledTriangle {
	
	public static void main(String[] args)
	{
		for(int i=1;i<=5;i++)
		{
			for(int j=5;j>=1;j--)
			{
				if(j>i)
				{
					System.out.print(" ");
				}
				else
				{
					System.out.print("*");
				}
			}
			System.out.println("");
		}
	}

}
