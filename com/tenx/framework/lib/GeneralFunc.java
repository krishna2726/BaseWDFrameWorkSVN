package com.tenx.framework.lib;

public class GeneralFunc {
  public GeneralFunc() {}
  
  public static boolean CompareTwoArrays(String[] sExptArray, String[] sActArray) {
    String sMisMatchItems = "";
    int iExpArrLength = sExptArray.length;
    int iActArrLength = sActArray.length;
    if (iExpArrLength != iActArrLength) {
      Messages.errorMsg = 
        "Expected Array length: " + iExpArrLength + " Actual Lengths: " + iActArrLength + " are not equal ";
      return false;
    }
    for (int iCount = 0; iCount <= sExptArray.length - 1; iCount++) {
      boolean isFound = false;
      for (int jCount = 0; jCount <= sActArray.length - 1; jCount++)
      {
        if (sExptArray[iCount].trim().equalsIgnoreCase(sActArray[jCount].trim())) {
          isFound = true;
          break;
        }
      }
      if (!isFound) {
        sMisMatchItems = sMisMatchItems + " , " + sExptArray[iCount];
      }
    }
    if (sMisMatchItems.length() != 0) {
      Messages.errorMsg = sMisMatchItems + " are the mismatched items";
      return false;
    }
    return true;
  }
}
