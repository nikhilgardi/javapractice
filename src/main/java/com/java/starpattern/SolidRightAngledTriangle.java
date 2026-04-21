package com.java.starpattern;

import java.util.Scanner;

/*
	 *
	 **
	 ***
	 ****
	 *****
	 This program prints a solid right angled triangle based on the inputs printed by the user
 */
public class SolidRightAngledTriangle {
	
	public static void main(String [] args)
	{
		Scanner scanner=new Scanner(System.in);
		System.out.println("Enter Height:");
		int height=scanner.nextInt();
		
		for(int i=1;i<=5;i++)
		{
			for(int j=1;j<=i;j++)
			{
				System.out.print("*");
			}
			System.out.println("");
		}
		
		
	}

}
