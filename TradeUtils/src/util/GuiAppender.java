package util;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

public class GuiAppender extends AppenderSkeleton {

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void append(LoggingEvent event) {
        String message = null;
        if(event.locationInformationExists()){
            StringBuilder formatedMessage = new StringBuilder();
            formatedMessage.append(event.getLocationInformation().getClassName());
            formatedMessage.append(".");
            formatedMessage.append(event.getLocationInformation().getMethodName());
            formatedMessage.append(":");
            formatedMessage.append(event.getLocationInformation().getLineNumber());
            formatedMessage.append(" - ");
            formatedMessage.append(event.getMessage().toString());
            message = formatedMessage.toString();
        }else{
            message = event.getMessage().toString();
        }

        switch(event.getLevel().toInt()){
        case Level.INFO_INT:
            //your decision
            break;
        case Level.DEBUG_INT: 
            //your decision
            break;
        case Level.ERROR_INT:
            //your decision
            break;
        case Level.WARN_INT:
            //your decision
            break;
        case Level.TRACE_INT:
            //your decision
            break;
        default:
            //your decision
            break;
        }
	}

}
