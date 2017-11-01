package com.tenx.framework.reporting;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;


public class Logger
  extends FileAppender
{
  public Logger() {}
  
  public Logger(Layout layout, String filename, boolean append, boolean bufferedIO, int bufferSize)
    throws IOException
  {
    super(layout, filename, append, bufferedIO, bufferSize);
  }
  
  public Logger(Layout layout, String filename, boolean append) throws IOException
  {
    super(layout, filename, append);
  }
  
  public Logger(Layout layout, String filename) throws IOException
  {
    super(layout, filename);
  }
  
  public void activateOptions() {
    if (fileName != null) {
      try {
        fileName = getNewLogFileName();
        setFile(fileName, fileAppend, bufferedIO, bufferSize);
      } catch (Exception e) {
        errorHandler.error("Error while activating log options", e, 
          4);
      }
    }
  }
  
  private String getNewLogFileName() {
    if (this.fileName != null) {
      String DOT = ".";
      String HIPHEN = "-";
      File logFile = new File(this.fileName);
      String fileName = logFile.getName();
      String newFileName = "";
      
      int dotIndex = fileName.indexOf(".");
      if (dotIndex != -1)
      {

        newFileName = 
        
          fileName.substring(0, dotIndex) + "-" + System.currentTimeMillis() + "." + fileName.substring(dotIndex + 1);
      }
      else
      {
        newFileName = fileName + "-" + System.currentTimeMillis();
      }
      return logFile.getParent() + File.separator + newFileName;
    }
    return null;
  }
}
