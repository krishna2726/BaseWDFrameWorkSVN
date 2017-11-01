package com.tenx.framework.lib;

import org.openqa.selenium.WebDriver;

public class Windows
{
  private static boolean bStatus;
  
  public Windows() {}
  
  public static String getWindowTitle(WebDriver wDriver)
  {
    return wDriver.getTitle();
  }
  
  public static String[] getWindowTitles(WebDriver wDriver) {
    java.util.Set<String> windows = wDriver.getWindowHandles();
    int iSize = windows.size();
    String[] arrWindows = new String[iSize];
    int iInc = 0;
    for (String handle : windows) {
      wDriver.switchTo().window(handle);
      arrWindows[iInc] = wDriver.getTitle();
      iInc++;
    }
    return arrWindows;
  }
  
  public static boolean switchToWindowByIndex(WebDriver wDriver, int iWindowIndex)
  {
    java.util.Set<String> windows = wDriver.getWindowHandles();
    java.util.Iterator<String> itr = windows.iterator();
    int iSize = windows.size();
    if (iSize > 1) {
      if (iWindowIndex < iSize) {
        String[] arrWin = new String[iSize];
        int inc = 0;
        while (itr.hasNext()) {
          arrWin[inc] = ((String)itr.next()).toString();
          inc++;
        }
        wDriver.switchTo().window(arrWin[iWindowIndex]);
        return true;
      }
      Messages.errorMsg = 
        iWindowIndex + " is greater than windows count " + iSize;
      return false;
    }
    Messages.errorMsg = "only one window is available";
    return false;
  }
  
  public static boolean switchToWindowByTitle(WebDriver wDriver, String sWindowName)
  {
    String sFocusedWindow = wDriver.getWindowHandle();
    java.util.Set<String> windows = wDriver.getWindowHandles();
    int iSize = windows.size();
    if (iSize > 1) {
      for (String handle : windows) {
        wDriver.switchTo().window(handle);
        if (wDriver.getTitle().equalsIgnoreCase(sWindowName)) {
          return true;
        }
      }
      wDriver.switchTo().window(sFocusedWindow);
      Messages.errorMsg = sWindowName + " not found";
      return false;
    }
    Messages.errorMsg = "only one window is available";
    return false;
  }
  
  public static boolean switchToFrameByFrameElement(WebDriver wDriver, org.openqa.selenium.By objLocator)
  {
    bStatus = Verify.verifyElementVisible(wDriver, objLocator);
    if (bStatus) {
      wDriver.switchTo().frame(wDriver.findElement(objLocator));
      return true;
    }
    return false;
  }
  
  public static boolean switchToFrameByName(WebDriver wDriver, String sName) {
    org.openqa.selenium.By objLocator = org.openqa.selenium.By.name(sName);
    bStatus = Verify.verifyElementVisible(wDriver, objLocator);
    if (bStatus) {
      try {
        wDriver.switchTo().frame(sName);
        return true;
      } catch (Exception e) {
        Messages.errorMsg = e.getMessage();
        return false;
      }
    }
    return false;
  }
  
  public static boolean switchToFrameById(WebDriver wDriver, String sId) {
    org.openqa.selenium.By objLocator = org.openqa.selenium.By.id(sId);
    bStatus = Verify.verifyElementVisible(wDriver, objLocator);
    if (bStatus) {
      try {
        wDriver.switchTo().frame(sId);
        return true;
      } catch (Exception e) {
        Messages.errorMsg = e.getMessage();
        return false;
      }
    }
    return false;
  }
}
