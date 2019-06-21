package com.epam.sudoku_validator.dao;

import org.slf4j.Logger;

/**
 * Interface for SudokuTable
 * 
 * @author Istvan_Simo
 */
public interface SudokuTableDao {
	public boolean loadFromFile(Logger LOG, String fileName);
}
