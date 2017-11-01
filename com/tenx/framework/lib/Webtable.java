package com.tenx.framework.lib;

import atu.webdriver.utils.table.WebTable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Webtable
{
  public Webtable() {}
  
  public static String getValueFromTableBody(WebDriver wDriver, By objLocator, int iRowIndex, int iColIndex)
  {
    org.openqa.selenium.WebElement TableLocator = Elements.getWebElement(wDriver, objLocator);
    if (TableLocator == null) {
      return null;
    }
    WebTable table = WebTable.getTable(TableLocator);
    try {
      return table.getRow(iRowIndex).getCell(iColIndex).getText();
    } catch (Exception e) {
      Messages.errorMsg = e.getMessage();
    }
    return null;
  }
  
  public static boolean tableBodyCellShouldContain(WebDriver wDriver, By objLocator, int iRowIndex, int iColIndex, String sExpectedValue)
  {
    org.openqa.selenium.WebElement TableLocator = Elements.getWebElement(wDriver, objLocator);
    if (TableLocator == null) {
      return false;
    }
    WebTable table = WebTable.getTable(TableLocator);
    try {
      String sActualValue = table.getRow(iRowIndex).getCell(iColIndex)
        .getText();
      if (sExpectedValue.contains(sActualValue)) {
        return true;
      }
      return false;
    } catch (Exception e) {
      Messages.errorMsg = e.getMessage();
    }
    return false;
  }
  
  public static boolean tableBodyColumnShouldContain(WebDriver wDriver, By objLocator, int iColIndex, String sExpectedValue)
  {
    org.openqa.selenium.WebElement TableLocator = Elements.getWebElement(wDriver, objLocator);
    if (TableLocator == null) {
      return false;
    }
    WebTable table = WebTable.getTable(TableLocator);
    int iRowcount = table.getRowCount();
    System.out.println(iRowcount);
    for (int iCount = 0; iCount < iRowcount; iCount++) {
      System.out.println("iCount" + iCount);
      try {
        if (table.getRow(iCount).getCoulmnCount() > 0) {
          String sActualValue = table.getRow(iCount)
            .getCell(iColIndex).getText();
          System.out.println(sActualValue);
          if (sExpectedValue.contains(sActualValue)) {
            return true;
          }
        }
      } catch (Exception e) {
        Messages.errorMsg = e.getMessage();
        return false;
      }
    }
    Messages.errorMsg = 
      sExpectedValue + " value not found in the table body";
    return false;
  }
  
  public static boolean tableBodyRowShouldContain(WebDriver wDriver, By objLocator, int iRowIndex, String sExpectedValue)
  {
    org.openqa.selenium.WebElement TableLocator = Elements.getWebElement(wDriver, objLocator);
    if (TableLocator == null) {
      return false;
    }
    WebTable table = WebTable.getTable(TableLocator);
    
    int iColcount = table.getRow(iRowIndex).getCoulmnCount();
    for (int iCount = 0; iCount < iColcount; iCount++) {
      try {
        String sActualValue = table.getRow(iRowIndex).getCell(iCount)
          .getText();
        if (sExpectedValue.contains(sActualValue)) {
          return true;
        }
      } catch (Exception e) {
        Messages.errorMsg = e.getMessage();
        return false;
      }
    }
    Messages.errorMsg = 
      sExpectedValue + " value not found in the table body";
    return false;
  }
  
  public static boolean tableBodyShouldContain(WebDriver wDriver, By objLocator, String sExpectedValue)
  {
    org.openqa.selenium.WebElement TableLocator = Elements.getWebElement(wDriver, objLocator);
    if (TableLocator == null) {
      return false;
    }
    WebTable table = WebTable.getTable(TableLocator);
    
    int iRowcount = table.getRowCount();
    System.out.println("Rowcount" + iRowcount);
    for (int iCount = 0; iCount < iRowcount; iCount++) {
      int iColcount = table.getRow(iCount).getCoulmnCount();
      System.out.println("colcount" + iColcount);
      for (int jCount = 0; jCount < iColcount; jCount++) {
        String sActualValue = table.getRow(iCount).getCell(jCount)
          .getText();
        System.out.println("actual value" + sActualValue);
        if (sExpectedValue.contains(sActualValue)) {
          return true;
        }
      }
    }
    Messages.errorMsg = 
      sExpectedValue + " value not found in the table body";
    return false;
  }
  
  public static String getValueFromTableHeader(WebDriver wDriver, By objLocator, int iRowIndex, int iColIndex)
  {
    org.openqa.selenium.WebElement TableLocator = Elements.getWebElement(wDriver, objLocator);
    if (TableLocator == null) {
      return null;
    }
    WebTable table = WebTable.getTable(TableLocator);
    try {
      return table.getRow(iRowIndex).getHeaderCell(iColIndex).getText();
    } catch (Exception e) {
      Messages.errorMsg = e.getMessage();
    }
    return null;
  }
  
  public static boolean tableHeaderShouldContain(WebDriver wDriver, By objLocator, String sExpectedValue)
  {
    org.openqa.selenium.WebElement TableLocator = Elements.getWebElement(wDriver, objLocator);
    if (TableLocator == null) {
      return false;
    }
    WebTable table = WebTable.getTable(TableLocator);
    
    int iRowcount = table.getRowCount();
    System.out.println(iRowcount);
    for (int iCount = 0; iCount < iRowcount; iCount++) {
      int iColcount = table.getRow(iCount).getHeaderCoulmnCount();
      System.out.println("iColcount" + iColcount);
      for (int jCount = 0; jCount < iColcount; jCount++) {
        String sActualValue = table.getRow(iCount)
          .getHeaderCell(jCount).getText();
        if (sActualValue.contains(sExpectedValue)) {
          return true;
        }
      }
    }
    Messages.errorMsg = 
      sExpectedValue + " value not found in the table header";
    return false;
  }
  
  public static int getRowCount(WebDriver wDriver, By objLocator) {
    org.openqa.selenium.WebElement TableLocator = Elements.getWebElement(wDriver, objLocator);
    if (TableLocator == null) {
      return 0;
    }
    WebTable table = WebTable.getTable(TableLocator);
    
    int iRowcount = table.getRowCount();
    return iRowcount;
  }
}
