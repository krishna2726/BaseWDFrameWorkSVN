package com.tenx.framework.lib;

import org.openqa.selenium.WebDriver;

public class Alerts { private static boolean bStatus;
  
  public Alerts() {}
  
  public static String getAlertMessage(WebDriver wDriver) { String alertMsg = null;
    bStatus = Wait.waitForAlert(wDriver, 5L);
    if (bStatus) {
      alertMsg = wDriver.switchTo().alert().getText();
      if (alertMsg != null) {
        return alertMsg;
      }
      return alertMsg;
    }
    return alertMsg;
  }
  
  public static boolean acceptAlert(WebDriver wDriver) {
    bStatus = Verify.verifyAlertPresent(wDriver);
    if (bStatus) {
      wDriver.switchTo().alert().accept();
      return true;
    }
    return false;
  }
  
  public static boolean closeAlert(WebDriver wDriver) {
    bStatus = Verify.verifyAlertPresent(wDriver);
    if (bStatus) {
      wDriver.switchTo().alert().dismiss();
      return true;
    }
    return false;
  }
}
