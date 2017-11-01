package com.tenx.framework.lib;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Browser
{
  static org.openqa.selenium.support.ui.WebDriverWait wait;
  
  public Browser() {}
  
  static WebDriver driver = null;
  
  public static WebDriver openBrowser(String sURL) {
    return Browser.driver = openFirefoxBrowser(driver, sURL);
  }
  
  public static WebDriver openBrowser(String sBrowserName, String sURL, String sPathOfDriver)
  {
    if (sBrowserName.equalsIgnoreCase("ie")) {
      return Browser.driver = openIEBrowser(driver, sURL, sPathOfDriver);
    }
    if (sBrowserName.equalsIgnoreCase("chrome")) {
      return Browser.driver = openChromeBrowser(driver, sURL, 
        sPathOfDriver);
    }
    Messages.errorMsg = "No browser drivers found";
    return driver;
  }
  
  private static WebDriver openFirefoxBrowser(WebDriver wDriver, String surl) {
    if (wDriver == null) {
      try {
        wDriver = new org.openqa.selenium.firefox.FirefoxDriver();
        wDriver.get(surl);
        wDriver.manage().window().maximize();
      } catch (Exception e) {
        Messages.errorMsg = e.getMessage();
      }
    }
    return wDriver;
  }
  
  private static WebDriver openChromeBrowser(WebDriver wDriver, String sUrl, String sPathOfDriver)
  {
    if (wDriver == null) {
      try {
        System.setProperty("webdriver.chrome.driver", sPathOfDriver);
        wDriver = new org.openqa.selenium.chrome.ChromeDriver();
        wDriver.get(sUrl);
        wDriver.manage().window().maximize();
      } catch (Exception e) {
        Messages.errorMsg = e.getMessage();
      }
    }
    return wDriver;
  }
  
  private static WebDriver openIEBrowser(WebDriver wDriver, String sUrl, String sPathOfdriver)
  {
    try {
      System.setProperty("webdriver.ie.driver", sPathOfdriver);
      DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
      dc.setCapability("nativeEvents", false);
      wDriver = new org.openqa.selenium.ie.InternetExplorerDriver(dc);
      wDriver.manage().deleteAllCookies();
      wDriver.get(sUrl);
      wDriver.manage().window().maximize();
    } catch (Exception e) {
      Messages.errorMsg = e.getMessage();
    }
    return wDriver;
  }
  
  public static boolean closeAllBrowsers(WebDriver wDriver) {
    try {
      if (wDriver != null) {
        wDriver.quit();
        Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
        Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
        return true;
      }
      return false;
    } catch (Exception e) {
      Messages.errorMsg = e.getMessage(); }
    return false;
  }
  
  public static boolean closeCurrentBrowser(WebDriver wDriver)
  {
    try {
      if (wDriver != null) {
        String sFocusedWindow = wDriver.getWindowHandle();
        java.util.Set<String> windows = wDriver.getWindowHandles();
        int iSize = windows.size();
        if (iSize > 1) {
          wDriver.close();
          for (String handle : windows) {
            if (!sFocusedWindow.equalsIgnoreCase(handle)) {
              wDriver.switchTo().window(handle);
            }
          }
        }
        wDriver.close();
        Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
        Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
        return true;
      }
      return false;
    } catch (Exception e) {
      Messages.errorMsg = e.getMessage();
    }
    return false;
  }
  
  public static void navigateBack(WebDriver wDriver) {
    wDriver.navigate().back();
  }
  
  public static void navigateForward(WebDriver wDriver) {
    wDriver.navigate().forward();
  }
  
  public static void reloadPage(WebDriver wDriver) {
    wDriver.navigate().refresh();
  }
  
  public static void deleteAllCookies(WebDriver wDriver) {
    wDriver.manage().deleteAllCookies();
  }
  
  public static void deleteCookie(WebDriver wDriver, String sCookieName) {
    wDriver.manage().deleteCookieNamed(sCookieName);
  }
}
