package com.epam.sudoku_validator.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

import com.epam.sudoku_validator.domain.SudokuTable;

/**
 * Sudoku table DAO class
 * 
 * @author Istvan_Simo
 */
public class SimpleSudokuTableDao implements SudokuTableDao {
	
	private SudokuTable sudokuTable = new SudokuTable();
	
	public void init() {
		
	}
	
	/**
	 * Loads the specified file and populate the sudoku table
	 * 
	 * @param fileName
	 */
	public boolean loadFromFile(Logger LOG, String fileName) {
		boolean retVal = false;
		File f = new File(fileName);
		if(f.exists() && !f.isDirectory()) {
			try {
				FileInputStream fileIS = new FileInputStream(fileName);
				InputStreamReader inputSR = new InputStreamReader(fileIS);
				BufferedReader bufReader = new BufferedReader(inputSR);
				List<List<String>> rows = new ArrayList<>();
		        String lineBuf = null;
				while ((lineBuf = bufReader.readLine()) != null) {
				    List<String> dataLine = Arrays.asList(lineBuf.split(","));
				    rows.add(dataLine);
				}
	        	bufReader.close();
	        	inputSR.close();
	        	fileIS.close();
	        	this.sudokuTable.populateGrid(rows);
	        	retVal = true;
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	            LOG.error("The specified input file not found: " + fileName);
	        } catch (IllegalArgumentException e) {
	            //e.printStackTrace();
	            LOG.error("Can't populate sudoku table because of wrong CSV content: " + e.getMessage());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		} else {
			LOG.error("Invalid input file specification: " + fileName);
		}
		return retVal;
	}
	
	/**
	 * Returns with the string representation of the sudoku table
	 */
	public String toString() {
		return this.sudokuTable.toString();
	}
	
	/**
	 * Returns with the sudoku table validation result
	 * @return
	 */
	public boolean validate(Logger LOG) {
		return this.sudokuTable.validate(LOG);
	}

}
