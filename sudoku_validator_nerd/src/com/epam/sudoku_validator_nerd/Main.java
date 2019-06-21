package com.epam.sudoku_validator_nerd;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{
	private static String[] grid = new String[9];
	
	private static boolean loadFromFile(String fileName) {
		boolean retVal = true;
		File f = new File(fileName);
		if(f.exists() && !f.isDirectory()) {
			try {
				FileInputStream fileIS = new FileInputStream(fileName);
				InputStreamReader inputSR = new InputStreamReader(fileIS);
				BufferedReader bufReader = new BufferedReader(inputSR);
		        String lineBuf = null;
				for(int i = 0; i < 9; i++) {
					lineBuf = bufReader.readLine();
					if (lineBuf != null) {
						grid[i] = lineBuf.replaceAll("(?![1-9]).", "");
						//grid[i] = lineBuf.replaceAll("\\D", "");
						if (grid[i].length() != 9) {
							retVal = false;
							System.out.println("Error! Improper number of values or invalid values in line #" + String.valueOf(i) + ": \"" + lineBuf + "\"");
							break;
						}
					} else {
						retVal = false;
						System.out.println("Error! Too few rows in CSV");
						break;
					}
				}
	        	bufReader.close();
	        	inputSR.close();
	        	fileIS.close();
	        } catch (FileNotFoundException e) {
				retVal = false;
	            System.out.println("Error! The specified input file not found: " + fileName);
	        } catch (IllegalArgumentException e) {
				retVal = false;
	            System.out.println("Error! Can't populate sudoku table because of wrong CSV content: " + e.getMessage());
	        } catch (Exception e) {
				retVal = false;
	            e.printStackTrace();
	        }
		} else {
			retVal = false;
			System.out.println("Error! Invalid input file specification: " + fileName);
		}
		return retVal;
	}

	private static boolean validateGrid() {
		boolean retVal = false;
		String set = "";
		String tgt = "";
		String log = "";
		Pattern pattern = Pattern.compile("([0-9]).*?\\1");
		for(int i = 0; i < 9; i++) {
			log = String.valueOf(i) + " | " + grid[i];
			for(int t = 0; t < 3; t++) {
				set = "";
				switch (t) {
					case 0:
						tgt = "R";
						set = grid[i];
						break;
					case 1:
						tgt = "C";
						for(int j = 0; j < 9; j++) {
							set += grid[j].substring(i, i + 1);
						}
						break;
					case 2:
						tgt = "S";
						int x = (i % 3) * 3;
						int y = (i / 3) * 3;
						for(int j = 0; j < 3; j++) {
							set += grid[y + j].substring(x, x + 3);
						}
				}
				Matcher matcher = pattern.matcher(set);
				boolean res = matcher.find();
				retVal = retVal || res;
				log += " | " + tgt + ":" + set + " " + (res ? "!" : " ");
			}
			System.out.println(log);
		}
		return !retVal;
	}
	
	public static void main(final String[] args)
	{
		int exitCode = -1;
		if (args.length > 0) {
			System.out.println("Validate sudoku CSV: " + args[0]);
			boolean isLoaded = loadFromFile(args[0]);
			if (isLoaded) {
				boolean isValid = validateGrid();
				if (isValid) {
					System.out.println("Validate result: OK");
					exitCode = 0;
				} else {
					System.out.println("Validate result: Invalid");
				}
			}
		} else {
			System.out.println("Error! Input file should be specified!");
		}
        System.exit(exitCode);
	}
}
