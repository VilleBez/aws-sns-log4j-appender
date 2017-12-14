package org.log;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author paul.chen
 *
 */
public class StackTraceLayout extends Layout {
	private static final Logger log = LoggerFactory.getLogger(StackTraceLayout.class);

	public void activateOptions() {
	}

	@Override
	public String format(LoggingEvent event) {
		StringBuffer content = new StringBuffer();
		try {
			ThrowableInformation throwableInformation = event.getThrowableInformation();
			if(throwableInformation!=null){
				Throwable error = throwableInformation.getThrowable();
				if (error != null) {
					if(event.getMessage()!=null) content.append(event.getMessage()).append(LINE_SEP).append(LINE_SEP);
					content.append(error.getClass()).append(":").append(error.getMessage()).append(LINE_SEP);
					
					StackTraceElement[] stes = (StackTraceElement[]) null;
					stes = error.getStackTrace();
					for (int i = 0; i < stes.length; i++) {
						content.append("\t at ").append(stes[i].toString()).append(LINE_SEP);
					}
					if (error.getCause() != null) {
						stes = error.getCause().getStackTrace();
						content.append("Caused by: ").append(error.getCause().getClass()).append(":").append(error.getCause().getMessage()).append(LINE_SEP);
						for (int i = 0; i < stes.length; i++) {
							content.append("\t at ").append(stes[i].toString()).append(LINE_SEP);
						}
					}
				}
			}else{
				content.append(event.getMessage());
			}
		} catch (Exception e) {
			 log.error("Format Error", e);;
		}
		return content.toString();
	}

	@Override
	public boolean ignoresThrowable() {
		return false;
	}

}
