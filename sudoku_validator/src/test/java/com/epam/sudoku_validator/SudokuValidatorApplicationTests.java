package com.epam.sudoku_validator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.epam.sudoku_validator.app.AppConfig;
import com.epam.sudoku_validator.app.SudokuValidatorApplication;
import com.epam.sudoku_validator.dao.SimpleSudokuTableDao;

import ch.qos.logback.classic.Logger;

@ContextConfiguration(classes = AppConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class SudokuValidatorApplicationTests {
	
	private static ApplicationContext context = new AnnotationConfigApplicationContext (AppConfig.class);
	
	private static Logger LOG = (Logger) LoggerFactory.getLogger(SudokuValidatorApplication.class);

	@Test
	public void testValid() {
        SimpleSudokuTableDao sudokuTableDao = context.getBean(SimpleSudokuTableDao.class);
		assertEquals(true, sudokuTableDao.loadFromFile(LOG, "src\\main\\resources\\testValid.csv"));
		assertEquals(true, sudokuTableDao.validate(LOG));
	}

	@Test
	public void testInvalid() {
        SimpleSudokuTableDao sudokuTableDao = context.getBean(SimpleSudokuTableDao.class);
		assertEquals(true, sudokuTableDao.loadFromFile(LOG, "src\\main\\resources\\testInvalid.csv"));
		assertEquals(false, sudokuTableDao.validate(LOG));
	}

	@Test
	public void testWrongNumber() {
        SimpleSudokuTableDao sudokuTableDao = context.getBean(SimpleSudokuTableDao.class);
		assertEquals(false, sudokuTableDao.loadFromFile(LOG, "src\\main\\resources\\testWrongNumber.csv"));
	}

	@Test
	public void testWrongValue() {
        SimpleSudokuTableDao sudokuTableDao = context.getBean(SimpleSudokuTableDao.class);
		assertEquals(false, sudokuTableDao.loadFromFile(LOG, "src\\main\\resources\\testWrongValue.csv"));
	}

	@Test
	public void testTooManyCols() {
        SimpleSudokuTableDao sudokuTableDao = context.getBean(SimpleSudokuTableDao.class);
		assertEquals(false, sudokuTableDao.loadFromFile(LOG, "src\\main\\resources\\testTooManyCols.csv"));
	}

	@Test
	public void testMissingCol() {
        SimpleSudokuTableDao sudokuTableDao = context.getBean(SimpleSudokuTableDao.class);
		assertEquals(false, sudokuTableDao.loadFromFile(LOG, "src\\main\\resources\\testMissingCol.csv"));
	}

	@Test
	public void testTooManyRows() {
        SimpleSudokuTableDao sudokuTableDao = context.getBean(SimpleSudokuTableDao.class);
		assertEquals(false, sudokuTableDao.loadFromFile(LOG, "src\\main\\resources\\testTooManyRows.csv"));
	}

	@Test
	public void testMissingRow() {
        SimpleSudokuTableDao sudokuTableDao = context.getBean(SimpleSudokuTableDao.class);
		assertEquals(false, sudokuTableDao.loadFromFile(LOG, "src\\main\\resources\\testMissingRow.csv"));
	}

	@Test
	public void testEmptyFile() {
        SimpleSudokuTableDao sudokuTableDao = context.getBean(SimpleSudokuTableDao.class);
		assertEquals(false, sudokuTableDao.loadFromFile(LOG, "src\\main\\resources\\testEmptyFile.csv"));
	}

	@Test
	public void testMissingFile() {
        SimpleSudokuTableDao sudokuTableDao = context.getBean(SimpleSudokuTableDao.class);
		assertEquals(false, sudokuTableDao.loadFromFile(LOG, "src\\main\\resources\\missingFile.csv"));
	}

}
