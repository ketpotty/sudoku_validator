package com.epam.sudoku_validator.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

/**
 * Sudoku Table class, the representation of a standard 9x9 sudoku table
 *  
 * @author Istvan_Simo
 */
public class SudokuTable {
	private Integer[][] grid = new Integer[9][9];
	private Integer errorRow = null;
	private Integer errorCol = null;
	
	/**
	 * Gets a cell value
	 * 
	 * @param col - column index (x)
	 * @param row - row index (y)
	 * @return
	 */
	public int getCellAt(int col, int row) {
		return grid[row][col];
	}
	
	/**
	 * Populates the grid from a nested lists of strings
	 * 
	 * @param rows - the nested lists of string representation of the sudoku table
	 * @return true, if the process finished successfully
	 */
	public void populateGrid(List<List<String>> rows) {
		Integer rowIndex = 0;
		for (List<String> row: rows) {
			if (rowIndex > 8) {
				throw new IllegalArgumentException("There are too many rows (" + ((Integer)(rowIndex + 1)).toString() + ") in CSV file, it should be exactly 9!");
			}
			Integer colIndex = 0;
			for (String cell: row) {
				if (colIndex > 8) {
					throw new IllegalArgumentException("There are too many values (" + ((Integer)(colIndex + 1)).toString() + ") in row " + ((Integer)(rowIndex + 1)).toString() + ", it should be exactly 9!");
				}
				Integer sudokuCell = null;
				try {
					sudokuCell = Integer.parseInt(cell.trim());
				} catch(Exception e1) {
					throw new IllegalArgumentException("Invalid sudoku cell value (\"" + cell + "\") at (row: " + ((Integer)(rowIndex + 1)).toString() + ", column: " + ((Integer)(colIndex + 1)).toString() + "), it should be an integer value!");
				}
				if (sudokuCell < 1 || sudokuCell > 9) {
					throw new IllegalArgumentException("Invalid sudoku cell value (" + sudokuCell.toString() + ") at (row: " + ((Integer)(rowIndex + 1)).toString() + ", column: " + ((Integer)(colIndex + 1)).toString() + "), it should be between 1 and 9!");
				} else {
					grid[rowIndex][colIndex] = sudokuCell;
				}
				colIndex++;
			}
			if (colIndex < 9) {
				throw new IllegalArgumentException("There are too few values (" + colIndex.toString() + ") in row " + ((Integer)(rowIndex + 1)).toString() + ", it should be exactly 9!");
			}
			rowIndex++;
		}
		if (rowIndex < 9) {
			throw new IllegalArgumentException("There are too few rows (" + rowIndex.toString() + ") in CSV file, it should be exactly 9!");
		}
	}

	/**
	 * Validates a set of 9 numbers (rows, columns, subtables)
	 * The main idea that the Java Set object doesn't accept an item more than once
	 * Therefore if there's a duplication, the length of the set will be less then 9
	 * Additionally it checks that the first and the last element should be 1 and 9, even though the previous check is enough in itself 
	 * 
	 * @param LOG - the Logger object from the caller (Application.run method)
	 * @param validateType - the target set of the validation, can be ROW, COLUMN or SUBTABLE
	 * @param itemIndex - the target index in the specified set, e.g. for rows it's the column index, for columns it's the row index, etc. 
	 * @return true if the given set doesn't contain any duplicated item
	 */
	private boolean validateSet(Logger LOG, ValidateType validateType, Integer itemIndex) {
		Set<Integer> items = new HashSet<Integer>();
		String cellPos = validateType.toString().toLowerCase() + " " + itemIndex.toString();
		String set = "";
		for(Integer index = 0; index < 9; index++) {
			Integer item = null;
			Integer setSize = items.size();
			Integer rowIndex = null;
			Integer colIndex = null;
			switch(validateType) {
				case ROW:
					rowIndex = itemIndex;
					colIndex = index;
					break;
				case COLUMN:
					rowIndex = index;
					colIndex = itemIndex;
					break;
				case SUBTABLE:
					rowIndex = (itemIndex / 3) * 3 + index / 3;
					colIndex = (itemIndex % 3) * 3 + index % 3;
			}
			item = this.grid[rowIndex][colIndex];
			set += item.toString();
			items.add(item);
			if (setSize == items.size()) {
				this.errorRow = rowIndex;
				this.errorCol = colIndex;
				LOG.error("Duplicate value (" + item.toString() + ") found while checking " + cellPos + ", at row: " + rowIndex.toString() + ", col: " + colIndex.toString());
			}
		}
		Integer minVal = Collections.min(items);
		Integer maxVal = Collections.max(items);
		Integer setSize = items.size();
		LOG.debug("@" + itemIndex.toString() + validateType.toString().substring(0,  1) + " - min: " + minVal.toString() + "; max: " + maxVal.toString() + "; size: " + setSize.toString() + "; set: " + set);
		return (minVal == 1 && maxVal == 9 && setSize == 9);
	}
	
	/**
	 * Main validation process, calls validateSet for each rows / columns / subtable
	 * Because of using logical operand between return values it will stop at the first error and terminate the validation process.
	 * That means there's no complex check, which cell is - or cells are - the real root cause of the invalid result!
	 * 
	 * @return true if the table is valid
	 */
	public boolean validate(Logger LOG) {
		boolean retVal = true;
		for(int index = 0; index < 9; index++) {
			retVal = retVal && this.validateSet(LOG, ValidateType.ROW, index);
			retVal = retVal && this.validateSet(LOG, ValidateType.COLUMN, index);
			retVal = retVal && this.validateSet(LOG, ValidateType.SUBTABLE, index);
		}
		return retVal;
	}
	
	/**
	 * Returns with the string representation of the sudoku table
	 * After a validation it shows the first found error as well by displaying the affected cell between <> signs
	 */
	public String toString() {
		String retVal = "";
		for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
			if (rowIndex == 3 || rowIndex == 6) {
				retVal += "\n+===+===+===+===+===+===+===+===+===+\n|";
			} else {
				retVal += "\n+---+---+---+---+---+---+---+---+---+\n|";
			}
			for (int colIndex = 0; colIndex < 9; colIndex++) {
				if (this.errorRow != null && this.errorCol != null && rowIndex == this.errorRow && colIndex == this.errorCol) {
					retVal += "<" + this.grid[rowIndex][colIndex].toString() + ">";
				} else {
					retVal += " " + this.grid[rowIndex][colIndex].toString() + " ";
				}
				if (colIndex == 2 || colIndex == 5) {
					retVal += "║";
				} else {
					retVal += "|";
				}
			}
		}
		retVal += "\n+---+---+---+---+---+---+---+---+---+";
		return retVal;
	}

}
