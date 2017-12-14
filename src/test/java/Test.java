import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.log.SNSAppender;
import org.log.StackTraceLayout;

public class Test {

	public static void main(String[] args) {
		// creates pattern layout
        PatternLayout layout = new PatternLayout();
        String conversionPattern = "%-7p %d [%t] %c %x - %m%n";
        layout.setConversionPattern(conversionPattern);
 
        // creates console appender
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setLayout(layout);
        consoleAppender.activateOptions();
 
        // creates sns appender
        SNSAppender snsAppender = new SNSAppender();
        snsAppender.setTopicName("test");
        snsAppender.setLayout(new StackTraceLayout());
        snsAppender.setThreshold(Level.ERROR);
        snsAppender.activateOptions();
        
        // configures the root logger
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.DEBUG);
        rootLogger.addAppender(consoleAppender);
        rootLogger.addAppender(snsAppender);
 
        // creates a custom logger and log messages
        Logger logger = Logger.getLogger(Test.class);
        logger.debug("this is a debug log message");
        logger.info("this is a information log message");
        logger.warn("this is a warning log message");
        logger.error("this is a error log message", new RuntimeException("123"));
	}

}
