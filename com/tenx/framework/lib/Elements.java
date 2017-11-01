package com.tenx.framework.lib;

import org.openqa.selenium.WebDriver;

public class Elements {
  private static boolean bStatus;
  
  public Elements() {}
  
  private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("Elements");
  
  public static org.openqa.selenium.WebElement getWebElement(WebDriver wDriver, org.openqa.selenium.By objLocator) {
    bStatus = Verify.verifyElementVisible(wDriver, objLocator);
    if (bStatus) {
      logger.info("The Element " + objLocator + 
        " is visible and can be used");
      return wDriver.findElement(objLocator);
    }
    logger.warn("The Element " + objLocator + 
      " is not visible and cannot be used");
    return null;
  }
  
  public static int getXpathCount(WebDriver wDriver, org.openqa.selenium.By objLocator) {
    int iSize = 0;
    bStatus = Verify.verifyElementVisible(wDriver, objLocator);
    if (bStatus) {
      iSize = wDriver.findElements(objLocator).size();
      logger.info("The xpath count of the element " + objLocator + " is " + 
        iSize);
      return iSize;
    }
    logger.warn("The xpath count of the element " + objLocator + " is " + 
      iSize + " because " + Messages.errorMsg);
    return iSize;
  }
  
  public static boolean enterText(WebDriver wDriver, org.openqa.selenium.By objLocator, String sValue)
  {
    bStatus = Verify.verifyElementVisible(wDriver, objLocator);
    if (bStatus) {
      wDriver.findElement(objLocator).clear();
      wDriver.findElement(objLocator).sendKeys(
        new CharSequence[] { sValue });
      logger.info("The text" + sValue + "has been inputted successfully.");
      return true;
    }
    logger.warn("The text " + sValue + "could not be entered successfully");
    return false;
  }
  
  public static boolean selectCheckbox(WebDriver wDriver, org.openqa.selenium.By objLocator) {
    bStatus = Verify.verifyChecked(wDriver, objLocator);
    if (!bStatus) {
      wDriver.findElement(objLocator).click();
      logger.info("The check box has been selected");
      return true;
    }
    logger.warn("Cannot check the CheckBox");
    return false;
  }
  
  public static boolean unSelectCheckbox(WebDriver wDriver, org.openqa.selenium.By objLocator) {
    bStatus = Verify.verifyChecked(wDriver, objLocator);
    if (bStatus) {
      wDriver.findElement(objLocator).click();
      logger.info("The check box has been deselected successfully");
      return true;
    }
    logger.warn("Cannot uncheck the CheckBox because " + Messages.errorMsg);
    return false;
  }
  
  public static boolean clickButton(WebDriver wDriver, org.openqa.selenium.By objLocator) {
    bStatus = Verify.verifyElementVisible(wDriver, objLocator);
    if (bStatus) {
      wDriver.findElement(objLocator).click();
      logger.info("The button " + objLocator + 
        " has been clicked successfully");
      return true;
    }
    logger.warn("The button " + objLocator + " cannot be clicked because " + 
      Messages.errorMsg);
    return false;
  }
  
  public static boolean clearText(WebDriver wDriver, org.openqa.selenium.By objLocator) {
    bStatus = Verify.verifyElementVisible(wDriver, objLocator);
    if (bStatus) {
      wDriver.findElement(objLocator).clear();
      logger.info("The text has been cleared from the input box " + 
        objLocator + " successfully");
      return true;
    }
    logger.warn("The text could not be cleared from the input box " + 
      objLocator);
    return false;
  }
  
  public static boolean selectOptionByIndex(WebDriver wDriver, String objLocator, int iIndexVal)
  {
    bStatus = Verify.verifyElementPresent(wDriver, org.openqa.selenium.By.xpath(objLocator));
    if (!bStatus)
      return false;
    int iOptionCnt = getXpathCount(wDriver, org.openqa.selenium.By.xpath(objLocator));
    if (iOptionCnt < iIndexVal) {
      Messages.errorMsg = iIndexVal + " index value is not valid";
      return false;
    }
    clickButton(wDriver, org.openqa.selenium.By.xpath(objLocator + "[" + iIndexVal + "]"));
    return true;
  }
  
  public static boolean selectOptionByValue(WebDriver wDriver, String objLocator, String sValue)
  {
    org.openqa.selenium.By dropDown = org.openqa.selenium.By.xpath(objLocator);
    bStatus = Verify.verifyElementPresent(wDriver, dropDown);
    if (!bStatus)
      return false;
    int iOptionCnt = getXpathCount(wDriver, dropDown);
    for (int iCnt = 1; iCnt <= iOptionCnt; iCnt++) {
      org.openqa.selenium.By dropDownValues = org.openqa.selenium.By.xpath(objLocator + "[" + iCnt + "]");
      
      if (sValue.equalsIgnoreCase(getText(wDriver, dropDownValues))) {
        clickButton(wDriver, dropDownValues);
        return true;
      }
    }
    Messages.errorMsg = sValue + " value not found in the dropdown";
    return false;
  }
  
  public static String getElementAttribute(WebDriver wDriver, org.openqa.selenium.By objLocator, String sAttrVal)
  {
    String sValue = null;
    bStatus = Verify.verifyElementPresent(wDriver, objLocator);
    if (bStatus) {
      sValue = wDriver.findElement(objLocator).getAttribute(sAttrVal);
      if (sValue == null) {
        Messages.errorMsg = 
          "The element " + objLocator + " has no attribute " + sAttrVal;
        logger.warn(Messages.errorMsg);
        return sValue;
      }
      logger.warn("The element " + objLocator + " has value '" + sValue + 
        "' for attribute " + sAttrVal);
      return sValue;
    }
    logger.warn("The attribute " + sAttrVal + " of element " + objLocator + 
      " value cannot be retrieved because " + Messages.errorMsg);
    return sValue;
  }
  
  public static String getText(WebDriver wDriver, org.openqa.selenium.By objLocator) {
    String sValue = null;
    bStatus = Verify.verifyElementVisible(wDriver, objLocator);
    if (bStatus) {
      sValue = wDriver.findElement(objLocator).getText();
      if (sValue == null) {
        logger.info("The element " + objLocator + " has no text ");
        return sValue;
      }
      logger.warn("The text " + sValue + " from the element " + 
        objLocator + " is retrieved");
      return sValue;
    }
    logger.warn("The text from the element " + objLocator + 
      " cannot be retrieved because " + Messages.errorMsg);
    return sValue;
  }
}
