package com.epam.sudoku_validator.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import com.epam.sudoku_validator.dao.*; 

/**
 * Spring configuration
 * 
 * @author Istvan_Simo
 */
@Configuration
public class AppConfig {
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SimpleSudokuTableDao sudokuTableDao() {
		SimpleSudokuTableDao sudokuTableDao = new SimpleSudokuTableDao();
		sudokuTableDao.init();
		return sudokuTableDao;
	}

}
