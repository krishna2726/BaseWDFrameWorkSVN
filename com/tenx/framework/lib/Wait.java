package com.tenx.framework.lib;

import java.io.PrintStream;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Wait
{
  private static WebDriverWait wait;
  private static boolean bStatus;
  private static Logger logger = Logger.getLogger("Wait");
  
  public Wait() {}
  
  public static boolean waitForElementPresence(WebDriver wDriver, By objLocator, long iTimeout) {
    try { wait = new WebDriverWait(wDriver, iTimeout);
      wait.until(ExpectedConditions.presenceOfElementLocated(objLocator));
      logger.info("element " + objLocator + " is present after waiting.");
      return true;
    } catch (Exception e) {
      Messages.errorMsg = e.getMessage();
      logger.warn("element " + objLocator + 
        " is not present after waiting " + iTimeout + ".");
    }
    return false;
  }
  
  public static boolean waitForElementVisibility(WebDriver wDriver, By objLocator, long iTimeout)
  {
    try {
      wait = new WebDriverWait(wDriver, iTimeout);
      wait.until(
        ExpectedConditions.visibilityOfElementLocated(objLocator));
      logger.info("element " + objLocator + " is visible after waiting.");
      return true;
    } catch (Exception e) {
      Messages.errorMsg = e.getMessage();
      logger.warn("element " + objLocator + 
        " is not present after waiting " + iTimeout + " secs.");
    }
    return false;
  }
  
  public static boolean waitForTextVisible(WebDriver wDriver, String sText, long iTimeout)
  {
    long iTimeoutinMillis = iTimeout * 10L;
    long lFinalTime = System.currentTimeMillis() + iTimeoutinMillis;
    while (System.currentTimeMillis() < lFinalTime) {
      bStatus = Verify.verifyTextVisible(wDriver, sText);
      if (bStatus) {
        logger.info("Text '" + sText + "' is present after waiting .");
        return true;
      }
    }
    Messages.errorMsg = 
    
      "Text '" + sText + "' not found in the current page after waiting " + iTimeout + "secs";
    logger.warn(Messages.errorMsg);
    return false;
  }
  
  public static boolean waitForTextVisible(WebDriver wDriver, By objLocator, String sText, long iTimeout)
  {
    long iTimeoutinMillis = iTimeout * 1000L;
    long lFinalTime = System.currentTimeMillis() + iTimeoutinMillis;
    while (System.currentTimeMillis() < lFinalTime) {
      bStatus = Verify.verifyTextVisible(wDriver, objLocator, sText);
      if (bStatus) {
        logger.info("Text '" + sText + "' is present after waiting .");
        return true;
      }
    }
    Messages.errorMsg = 
    
      "Text '" + sText + "' not found in the current page after waiting " + iTimeout + "secs";
    logger.warn(Messages.errorMsg);
    return false;
  }
  
  public static boolean waitForAlert(WebDriver wDriver, long iTimeout) {
    long iTimeoutinMillis = iTimeout * 1000L;
    long lFinalTime = System.currentTimeMillis() + iTimeoutinMillis;
    while (System.currentTimeMillis() < lFinalTime) {
      try {
        wDriver.switchTo().alert();
        logger.info("Alert present");
        return true;
      } catch (NoAlertPresentException e) {
        Messages.errorMsg = e.getMessage();
      }
    }
    logger.warn(Messages.errorMsg + " after waiting " + iTimeoutinMillis + 
      " MilliSecs");
    return false;
  }
  



















  public static boolean waitForEnable(WebDriver wDriver, By objLocator, long iTimeout)
  {
    long iTimeoutinMillis = iTimeout * 1000L;
    long lFinalTime = System.currentTimeMillis() + iTimeoutinMillis;
    while (System.currentTimeMillis() < lFinalTime) {
      bStatus = Verify.verifyEnable(wDriver, objLocator);
      if (bStatus) {
        return true;
      }
    }
    Messages.errorMsg = 
      "TimedOut due to element is not enabled after " + iTimeout + "secs";
    return false;
  }
  
  public static String waitForWindow(WebDriver wDriver, long iTimeout) {
    String BancsChildWin = "";
    String sMainHandler = wDriver.getWindowHandle();
    System.out.println(sMainHandler);
    long iTimeoutinMillis = iTimeout * 1000L;
    long lFinalTime = System.currentTimeMillis() + iTimeoutinMillis;
    while (System.currentTimeMillis() < lFinalTime) {
      java.util.Set<String> handlers = wDriver.getWindowHandles();
      System.out.println("size of windows " + handlers);
      Iterator<String> winIterator = handlers.iterator();
      try {
        while (winIterator.hasNext()) {
          BancsChildWin = (String)winIterator.next();
          if (!sMainHandler.equalsIgnoreCase(BancsChildWin)) {
            return BancsChildWin;
          }
        }
      }
      catch (Exception localException) {}
    }
    Messages.errorMsg = 
      "TimedOut due to new window is not available after " + iTimeout + "secs";
    return BancsChildWin;
  }
}
