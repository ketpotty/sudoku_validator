package com.epam.sudoku_validator.app;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.epam.sudoku_validator.dao.SimpleSudokuTableDao;

/**
 * Spring Boot Console application to validate standard 9x9 sudoku tables, which stored in a standard CVS file
 * Usage:
 * java -jar -Dfile.encoding=UTF8 sudoku_validator.jar [fileName] [debugLevel]
 * [fileName]   - the full path of the file to be validated
 * [debugLevel] - debug level, possible values are:
 * 				  TRACE   - trace messages
 * 				  DEBUG   - debug messages
 * 				  INFO    - information messages
 * 				  WARNING - warning messages
 * 				  ERROR   - error messages
 * The program will return 0 if the table is valid, -1 if invalid 
 * 
 * @author Istvan_Simo
 */
@SpringBootApplication
public class SudokuValidatorApplication implements CommandLineRunner {
	
	/**
	 * Create application context, this will load Spring application configuration 
	 */
	private static ApplicationContext context = new AnnotationConfigApplicationContext (AppConfig.class);
	
	/**
	 * Create logger object for logging 
	 */
	private static Logger LOG = (Logger) LoggerFactory.getLogger(SudokuValidatorApplication.class);

	/**
	 * Main procedure for application
	 * 
	 * @param args - comman line arguments
	 * @throws Exception
	 */
	public static void main(String[] args) {
		//System.out.println("Application name: " + context.getApplicationName());
        SpringApplication.run(SudokuValidatorApplication.class, args);
    }
  
	/**
	 * Run procedure for console application, this contains the main action
	 * 
	 * @param args - command line arguments passed from main
	 */
    @Override
    public void run(String... args) {
    	int exitCode = 0;
        // ----------------------------------------------------
    	// Setup logger
        // ----------------------------------------------------
        LOG.debug("Application started");
    	LoggerContext logContext = LOG.getLoggerContext();
    	logContext.reset();
    	PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
    	logEncoder.setContext(logContext);
    	//logEncoder.setPattern("%-5level [%thread]: %message%n");
    	logEncoder.setPattern("%date{yyyy-MM-dd HH:mm:ss.SSS} %level: %message%n");
    	logEncoder.start();
    	ConsoleAppender<ILoggingEvent> logAppender = new ConsoleAppender<ILoggingEvent>();
    	logAppender.setContext(logContext);
    	logAppender.setEncoder(logEncoder);
    	logAppender.start();
    	LOG.addAppender(logAppender);
    	LOG.setLevel(Level.INFO);
        // ----------------------------------------------------
    	// Process command line arguments
        // ----------------------------------------------------
        LOG.debug("Executing command line runner");
        LOG.debug("Command line arguments:");
        String fileName = null;
        String logLevel = null;
        for (int i = 0; i < args.length; ++i) {
            LOG.debug("[{}]: {}", i, args[i]);
            switch (i) {
            	case 0:
            		fileName = args[i];
            		break;
            	case 1:
            		logLevel = args[i];
            		break;
            }
        }
        if (logLevel != null) {
            LOG.setLevel(Level.valueOf(logLevel));
        }
        if (fileName != null) {
            // ------------------------------------------------
        	// Main action - load CSV, populate sudoku table and validate it
            // ------------------------------------------------
            SimpleSudokuTableDao sudokuTableDao = context.getBean(SimpleSudokuTableDao.class);
    		
    		if (sudokuTableDao.loadFromFile(LOG, args[0])) {
                if (sudokuTableDao.validate(LOG)) {
                    LOG.info(sudokuTableDao.toString());
                    LOG.info("Validation result: OK");
                } else {
                    LOG.error(sudokuTableDao.toString());
                    LOG.error("Validation result: Invalid");
                    exitCode = -1;
                }
    		} else {
                LOG.error("Can't validate sudoku table!");
                exitCode = -1;
    		}
        } else {
        	LOG.error("Can't run this tool without at least one file name argument!\nUsage: java -jar -Dfile.encoding=UTF8 sudoku_validator.jar [fileName] [debugLevel]");
            exitCode = -1;
        }
        LOG.debug("Application finished");
        System.exit(exitCode);
    }

}
