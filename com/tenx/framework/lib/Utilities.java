package com.tenx.framework.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Utilities
{
  private static Map<String, String> objMap = null;
  private static boolean bCreateFile = false;
  private static String sTestResultFolderPath = "XMLMessages";
  private static String sPathSeparatorChar = File.separator;
  public static String sTransactionsXMLFilePath = "";
  private static Document doc;
  private static Element rootElement = null;
  private static Element MainMsgElement;
  private static Element MsgDtsElement;
  public static String sSetTransactionsFileName = "";
  
  public Utilities() {}
  
  public static Map<String, String> readTestData(String sFilePath, String sSheetName, String sTestCaseName) throws java.io.IOException {
    String sKey = null;
    String sValue = null;
    try {
      objMap = new HashMap();
      Workbook objWorkbook = Workbook.getWorkbook(new File(sFilePath));
      Sheet objSheet = objWorkbook.getSheet(sSheetName);
      int iRowCount = objSheet.getRows();
      int iColCount = objSheet.getColumns();
      for (int iRowCounter = 0; iRowCounter < iRowCount; iRowCounter++) {
        String sCurTestCaseName = objSheet.getCell(0, iRowCounter)
          .getContents();
        if (sCurTestCaseName.equalsIgnoreCase(sTestCaseName)) {
          for (int iColCounter = 0; iColCounter < iColCount; iColCounter++) {
            sKey = objSheet.getCell(iColCounter, 0).getContents();
            System.out.println(sKey);
            
            sValue = objSheet.getCell(iColCounter, iRowCounter)
              .getContents();
            System.out.println(sValue);
            sValue = getDate(sValue);
            sValue = getTestDataUniqueValue(sValue);
            if ((!sValue.equalsIgnoreCase("Null")) && 
              (sValue.trim().length() != 0)) {
              objMap.put(sKey, sValue);
            }
          }
          break;
        }
      }
    } catch (BiffException e) {
      Messages.errorMsg = "Exception occured.." + e.getMessage();
    }
    return objMap;
  }
  
  public static Map<String, Map<String, String>> readMultipleTestData(String sFilePath, String sSheetName, String sTestCaseName)
    throws Exception
  {
    Map<String, Map<String, String>> objTestData = new HashMap();
    
    String sPreviousTestCaseName = "";
    int iRowNo = 1;
    System.out.println(sFilePath);
    Workbook objWorkbook = Workbook.getWorkbook(new File(sFilePath));
    
    Sheet objSheet = objWorkbook.getSheet(sSheetName);
    System.out.println(sSheetName);
    int iRowCount = objSheet.getRows();
    int iColCount = objSheet.getColumns();
    for (int iRowCounter = 1; iRowCounter < iRowCount; iRowCounter++) {
      Map<String, String> objRowData = new HashMap();
      String sCurTestCaseName = objSheet.getCell(0, iRowCounter)
        .getContents();
      if ((sPreviousTestCaseName.length() != 0) && 
        (sCurTestCaseName != sPreviousTestCaseName) && 
        (sTestCaseName.trim().length() > 0)) {
        break;
      }
      if (sCurTestCaseName.equalsIgnoreCase(sTestCaseName)) {
        sPreviousTestCaseName = sCurTestCaseName;
        for (int iColCounter = 0; iColCounter < iColCount; iColCounter++) {
          String sKey = objSheet.getCell(iColCounter, 0)
            .getContents();
          sKey = sKey.trim();
          String sValue = objSheet.getCell(iColCounter, iRowCounter)
            .getContents();
          sValue = sValue.trim();
          sValue = getDate(sValue);
          sValue = getTestDataUniqueValue(sValue);
          sValue = getTestDataMsgRef(sValue);
          System.out.println(sValue);
          if ((!sValue.equalsIgnoreCase(null)) && 
            (sValue.trim().length() != 0)) {
            objRowData.put(sKey, sValue);
          }
        }
        objTestData.put("Row" + iRowNo, objRowData);
        
        objRowData = null;
        
        iRowNo++;
      }
    }
    return objTestData;
  }
  
  public static String getDate(String sValue) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String dt = sValue;
    if (sValue.trim().equalsIgnoreCase("Today")) {
      dt = sdf.format(cal.getTime());
    }
    if (sValue.trim().equalsIgnoreCase("TodayTZFormat"))
    {
      dt = convertDateToTZFormat();
    }
    if (sValue.trim().equalsIgnoreCase("TodayGenFormat"))
    {
      dt = convertDateToGenFormat();
    }
    if (sValue.trim().equalsIgnoreCase("TodayTFormat"))
    {
      dt = convertDateToTFormat();
    }
    if (sValue.trim().equalsIgnoreCase("TodayRev")) {
      dt = sdf.format(cal.getTime());
      dt = convertDateToMQFormat(dt);
    }
    if (sValue.trim().contains("TodayRev_")) {
      String[] arrValues = sValue.split("_");
      int iDays = Integer.parseInt(arrValues[1]);
      cal.add(5, iDays);
      dt = sdf.format(cal.getTime());
      dt = convertDateToMQFormat(dt);
    }
    if (sValue.trim().contains("Today_")) {
      String[] arrValues = sValue.split("_");
      int iDays = Integer.parseInt(arrValues[1]);
      cal.add(5, iDays);
      
      dt = sdf.format(cal.getTime());
    }
    if (sValue.trim().contains("Today#")) {
      String[] arrValues = sValue.split("#");
      int iDays = Integer.parseInt(arrValues[1]);
      
      cal.add(5, iDays);
      
      dt = sdf.format(cal.getTime());
    }
    return dt;
  }
  
  private static String getTestDataUniqueValue(String sValue)
  {
    String sTemp = sValue.toUpperCase();
    if (sTemp.contains("RTGSMSGID"))
    {
      sTemp = get22DigitUniqueName(sTemp);
      sTemp = sTemp.replace("RTGSMSGID", "");
      System.out.println(sTemp);
      return sTemp;
    }
    if (sTemp.contains("UNIQUE")) {
      sTemp = getUniqueName(sTemp);
      sTemp = sTemp.replace("UNIQUE", "");
      System.out.println(sTemp);
      return sTemp;
    }
    return sValue;
  }
  
  private static String get22DigitUniqueName(String sValue)
  {
    String sName = getUniqueName(sValue);
    Random rnd = new Random();
    int randomNo = 100000 + rnd.nextInt(900000);
    System.out.println("random id : " + randomNo);
    sName = sName + randomNo;
    return sName;
  }
  
  private static String getTestDataMsgRef(String sValue) {
    String sTemp = sValue.toUpperCase();
    if (sTemp.contains("MSGREF")) {
      sTemp = getUniqueName(sTemp);
      
      sTemp = sTemp.replace("MSGREF", "");
      System.out.println(sTemp);
      return sTemp;
    }
    return sValue;
  }
  
  private static String getUniqueName(String sName) {
    try {
      Thread.sleep(1000L);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
    Calendar rightNow = Calendar.getInstance();
    if (sName == "") {
      return sName;
    }
    String sNewName = sName + rightNow.get(1) + (rightNow.get(2) + 1) + 
      rightNow.get(5) + rightNow.get(10) + rightNow.get(12) + 
      rightNow.get(13);
    return sNewName;
  }
  
  public static void writeMessagesToXML(Map<String, String> objMQMainMap, Map<String, Map<String, String>> objMQMsgDtsMap) throws java.io.IOException
  {
    createResultfolder();
    openXMLFile();
    addMainMessageDetails(objMQMainMap);
    addTransactionData(objMQMsgDtsMap);
  }
  
  private static void addTransactionData(Map<String, Map<String, String>> objMQMsgDtsMap)
  {
    Map<String, String> objTransactionsMap = new HashMap();
    for (int iCount = 0; iCount < objMQMsgDtsMap.size(); iCount++) {
      objTransactionsMap = (Map)objMQMsgDtsMap.get("Row" + (iCount + 1));
      try {
        MsgDtsElement = doc.createElement("MsgDts");
        MainMsgElement.appendChild(MsgDtsElement);
        































        for (String sKey : objTransactionsMap.keySet())
        {
          MsgDtsElement.setAttribute(sKey, (String)objTransactionsMap.get(sKey));
        }
        



        TransformerFactory transformerFactory = 
          TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(
          sTransactionsXMLFilePath));
        transformer.transform(source, result);

      }
      catch (Exception e)
      {

        e.printStackTrace();
      }
    }
  }
  
  private static void addMainMessageDetails(Map<String, String> objMQMainMap)
  {
    try {
      MainMsgElement = doc.createElement("MainMsg");
      rootElement.appendChild(MainMsgElement);
      

      MainMsgElement.setAttribute("TCName", 
        (String)objMQMainMap.get("TestCaseName"));
      MainMsgElement.setAttribute("TechRefNumber", 
        (String)objMQMainMap.get("TechRefNumber"));
      MainMsgElement.setAttribute("BatchTime", 
        (String)objMQMainMap.get("Batch Time"));
      MainMsgElement.setAttribute("NoOfTransactions", 
        (String)objMQMainMap.get("NoOfTransactions"));
      MainMsgElement.setAttribute("TotalAmount", 
        (String)objMQMainMap.get("TotalAmount"));
      MainMsgElement.setAttribute("MsgStatus", "False");
      

      TransformerFactory transformerFactory = 
        TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(sTransactionsXMLFilePath));
      transformer.transform(source, result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private static void createResultfolder() {
    if (new File(sTestResultFolderPath).exists()) {
      return;
    }
    new File(sTestResultFolderPath).mkdir();
  }
  
  private static void openXMLFile() {
    if (!bCreateFile) {
      bCreateFile = true;
      String sMessageFileName = sSetTransactionsFileName + now() + ".xml";
      sMessageFileName = sMessageFileName.replace(":", "");
      sMessageFileName = sMessageFileName.replace(" ", "");
      sTransactionsXMLFilePath = sTestResultFolderPath + sPathSeparatorChar;
      sTransactionsXMLFilePath += sMessageFileName;
      createXMLFile();
    }
  }
  
  private static String now() {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    return sdf.format(cal.getTime());
  }
  
  private static void createXMLFile() {
    try {
      DocumentBuilderFactory docFactory = 
        DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      
      doc = docBuilder.newDocument();
      rootElement = doc.createElement("Messages");
      doc.appendChild(rootElement);
      
      TransformerFactory transformerFactory = 
        TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(sTransactionsXMLFilePath));
      transformer.transform(source, result);
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  

  public static Map<String, String> readXMLTestData(String sXMLFilePath, String sTestcaseName)
  {
    Map<String, String> objXMLMQMainMap = new HashMap();
    
    try
    {
      FileInputStream file = new FileInputStream(new File(sXMLFilePath));
      DocumentBuilderFactory builderFactory = 
        DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      Document xmlDocument = builder.parse(file);
      XPath xPath = XPathFactory.newInstance().newXPath();
      String expression = "//Messages/MainMsg[@TCName='" + sTestcaseName + 
        "']";
      NodeList nodeList = (NodeList)xPath.compile(expression).evaluate(
        xmlDocument, XPathConstants.NODE);
      Element element = (Element)nodeList;
      
      objXMLMQMainMap.put("TechRefNumber", 
        element.getAttribute("TechRefNumber"));
      objXMLMQMainMap.put("BatchTime", element.getAttribute("BatchTime"));
      objXMLMQMainMap.put("NoOfTransactions", 
        element.getAttribute("NoOfTransactions"));
      objXMLMQMainMap.put("TotalAmount", 
        element.getAttribute("TotalAmount"));
    }
    catch (Exception localException) {}
    return objXMLMQMainMap;
  }
  


  public static Map<String, Map<String, String>> readXMLMultipleTestData(String sXMLFilePath, String sTestcaseName)
  {
    Map<String, Map<String, String>> objXMLMQMsgDtsMap = new HashMap();
    Map<String, String> objTempMap = new HashMap();
    

    try
    {
      FileInputStream file = new FileInputStream(new File(sXMLFilePath));
      DocumentBuilderFactory builderFactory = 
        DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      Document xmlDocument = builder.parse(file);
      XPath xPath = XPathFactory.newInstance().newXPath();
      String sExpression = "//Messages/MainMsg[@TCName='" + sTestcaseName + 
        "']/MsgDts";
      NodeList nodeList = (NodeList)xPath.compile(sExpression).evaluate(
        xmlDocument, XPathConstants.NODESET);
      int iTransactionsCount = nodeList.getLength();
      for (int iCounter = 0; iCounter < iTransactionsCount; iCounter++) {
        sExpression = 
          "//Messages/MainMsg[@TCName='" + sTestcaseName + "']/MsgDts[" + (iCounter + 1) + "]";
        nodeList = (NodeList)xPath.compile(sExpression).evaluate(
          xmlDocument, XPathConstants.NODE);
        Element element = (Element)nodeList;
        objTempMap
          .put("MessageRef", element.getAttribute("MessageRef"));
        for (int iNode = 0; iNode < nodeList.getLength(); iNode++) {
          String sChildNodeName = nodeList.item(iNode).getNodeName();
          String sChildNodeText = nodeList.item(iNode)
            .getTextContent();
          objTempMap.put(sChildNodeName, sChildNodeText);
        }
        objXMLMQMsgDtsMap.put("Row" + (iCounter + 1), objTempMap);
        objTempMap.clear();
      }
    }
    catch (Exception localException) {}
    return objXMLMQMsgDtsMap;
  }
  
  public static String convertDateToMQFormat(String sDate) {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    String dt = "";
    
    try
    {
      Date date = formatter.parse(sDate);
      formatter.format(date);
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      dt = sdf.format(date);
    }
    catch (Exception localException) {}
    return dt;
  }
  
  public static String convertDateToTZFormat()
  {
    String dt = "";
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      dt = sdf.format(new Date());
    } catch (Exception localException) {}
    return dt;
  }
  

  public static String convertDateToTFormat()
  {
    String dt = "";
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      dt = sdf.format(new Date());
    } catch (Exception localException) {}
    return dt;
  }
  

  public static String convertDateToGenFormat()
  {
    String dt = "";
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      dt = sdf.format(new Date());
    } catch (Exception localException) {}
    return dt;
  }
}
