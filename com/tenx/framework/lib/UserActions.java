package com.tenx.framework.lib;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class UserActions
{
  private static Actions act;
  
  public UserActions() {}
  
  private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("UserActions");
  
  public static boolean mouseOver(WebDriver wDriver, org.openqa.selenium.By objLocator) {
    org.openqa.selenium.WebElement wbElement = Elements.getWebElement(wDriver, objLocator);
    if (wbElement == null) {
      logger.warn("The mouse over operation could not be performed on " + 
        objLocator + " due to " + Messages.errorMsg);
      return false;
    }
    act = new Actions(wDriver);
    act.moveToElement(wbElement);
    act.perform();
    logger.info("The mouse over operation on " + objLocator + 
      " has been performed successfully.");
    return true;
  }
  
  public static boolean click(WebDriver wDriver, org.openqa.selenium.By objLocator) {
    org.openqa.selenium.WebElement wbElement = Elements.getWebElement(wDriver, objLocator);
    if (wbElement == null) {
      logger.warn("The object " + objLocator + 
        " cannot be clicked due to " + Messages.errorMsg);
      return false;
    }
    act = new Actions(wDriver);
    act.click(wbElement);
    act.perform();
    logger.info("The object " + objLocator + 
      " has been clicked succesfully.");
    return true;
  }
  
  public static boolean clickAndHold(WebDriver wDriver, org.openqa.selenium.By objLocator) {
    org.openqa.selenium.WebElement wbElement = Elements.getWebElement(wDriver, objLocator);
    if (wbElement == null) {
      logger.warn("The clickAndHold operation on the element " + 
        objLocator + " could not be performed due to " + 
        Messages.errorMsg);
      return false;
    }
    act = new Actions(wDriver);
    act.clickAndHold(wbElement);
    act.perform();
    logger.info("The clickAndHold operation on the element " + objLocator + 
      " has been performed succesfully.");
    return true;
  }
  
  public static boolean contextClick(WebDriver wDriver, org.openqa.selenium.By objLocator) {
    org.openqa.selenium.WebElement wbElement = Elements.getWebElement(wDriver, objLocator);
    if (wbElement == null) {
      logger.warn("A context-click at middle of the given element " + 
        objLocator + " could not be performed due to " + 
        Messages.errorMsg);
      return false;
    }
    act = new Actions(wDriver);
    act.contextClick(wbElement);
    act.perform();
    logger.info("A context-click at middle of the given element " + 
      objLocator + " has been performed succesfully.");
    return true;
  }
  
  public static void contextClick(WebDriver wDriver) {
    act = new Actions(wDriver);
    act.contextClick();
    act.perform();
    logger.info("A contextClick at the current mouse location has been performed succesfully.");
  }
  
  public static boolean doubleClick(WebDriver wDriver, org.openqa.selenium.By objLocator) {
    org.openqa.selenium.WebElement wbElement = Elements.getWebElement(wDriver, objLocator);
    if (wbElement == null) {
      logger.warn("A double-click at middle of the given element on " + 
        objLocator + " could not be performed due to " + 
        Messages.errorMsg);
      return false;
    }
    act = new Actions(wDriver);
    act.doubleClick(wbElement);
    act.perform();
    logger.info("A double-click at middle of the given element on " + 
      objLocator + " has been performed succesfully.");
    return true;
  }
  
  public static boolean dragAndDrop(WebDriver wDriver, org.openqa.selenium.By objLocatorSource, org.openqa.selenium.By objLocatorDestination)
  {
    org.openqa.selenium.WebElement wbElementSource = Elements.getWebElement(wDriver, 
      objLocatorSource);
    org.openqa.selenium.WebElement wbElementDestination = Elements.getWebElement(wDriver, 
      objLocatorDestination);
    if ((wbElementSource == null) || (wbElementDestination == null)) {
      logger.warn("The drag and drop operation from (source) " + 
        objLocatorSource + " to (target) " + 
        objLocatorDestination + " could not be performed due to " + 
        Messages.errorMsg);
      return false;
    }
    act = new Actions(wDriver);
    act.dragAndDrop(wbElementSource, wbElementDestination);
    act.perform();
    logger.info("The drag and drop operation from (source) " + 
      objLocatorSource + " to (target) " + objLocatorDestination + 
      " has been performed succesfully.");
    return true;
  }
  
  public static void keyDown(WebDriver wDriver, org.openqa.selenium.Keys keyvalue) {
    act = new Actions(wDriver);
    act.keyDown(keyvalue);
    act.perform();
    logger.info("A modifier key press operation has been succesfully performed on " + 
      keyvalue.name());
  }
  
  public static boolean keyDown(WebDriver wDriver, org.openqa.selenium.By objLocator, org.openqa.selenium.Keys keyvalue)
  {
    org.openqa.selenium.WebElement wbElement = Elements.getWebElement(wDriver, objLocator);
    if (wbElement == null) {
      logger.warn("A modifier key " + keyvalue.name() + 
        " press operation could not be performed on " + 
        objLocator + " due to " + Messages.errorMsg);
      return false;
    }
    act = new Actions(wDriver);
    act.keyDown(wbElement, keyvalue);
    act.perform();
    logger.info("A modifier key " + keyvalue.name() + 
      " press operation performed on " + objLocator);
    return true;
  }
  
  public static void keyUp(WebDriver wDriver, org.openqa.selenium.Keys keyvalue) {
    act = new Actions(wDriver);
    act.keyUp(keyvalue);
    act.perform();
    logger.info("A modifier key " + keyvalue.name() + 
      " release operation performed ");
  }
  
  public static boolean keyUp(WebDriver wDriver, org.openqa.selenium.By objLocator, org.openqa.selenium.Keys keyvalue) {
    org.openqa.selenium.WebElement wbElement = Elements.getWebElement(wDriver, objLocator);
    if (wbElement == null) {
      logger.warn("A modifier key " + keyvalue.name() + 
        " release operation could not be performed on " + 
        objLocator + " due to " + Messages.errorMsg);
      return false;
    }
    act = new Actions(wDriver);
    act.keyUp(wbElement, keyvalue);
    act.perform();
    logger.info("A modifier key " + keyvalue.name() + 
      " release operation performed on " + objLocator);
    return true;
  }
  
  public static void sendKeys(WebDriver wDriver, CharSequence... value) {
    act = new Actions(wDriver);
    act.sendKeys(new CharSequence[] { org.openqa.selenium.Keys.chord(value) });
    act.perform();
    logger.info("Send keys to the active element has been performed succesfully.");
  }
}
