package com.tenx.framework.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class Reporting
{
	public static String Functionality = "NoName";
	public static String Testcasename = "NoName";
	public static int Iterator = 0;

	private static String sPathSeparatorChar = File.separator;
	private static String sTestResultFolderPath = "logs";
	private static Document doc;
	private static String sResultXMLFilePath = "";
	private static String sXMLCurrentScriptName;
	private static int sXMLCurrentIterator;
	private static Element rootElement;
	private static Element IteratorElement;
	private static Element tsElement;
	private static Element FuncElement;
	private static boolean bCreateFile = false;
	private static String sXMLCurrentFunctionality;
	private static int testCasesCount = 0;
	private static int testcasesFailCount = 0;
	private static int testcasesPassCount = 0;
	private static DocumentBuilderFactory docFactory;
	private static DocumentBuilder docBuilder;
	private static FileInputStream file;

	public Reporting() {}

	public static void logResults(String sStatus, String sStepdescription, String sStepResult) { sXMLCurrentFunctionality = Functionality;
	sXMLCurrentScriptName = Testcasename;
	sXMLCurrentIterator = Iterator;


	createResultfolder();


	openXMLFile();


	addOrUpdateFunctionalityNode();


	addOrUpdateTestScriptNode();


	addOrUpdateIteratorNode();


	addOrUpdateStepNode(sStatus, sStepdescription, sStepResult);


	if (sStatus.equalsIgnoreCase("Fail")) {
		updateFailStatus();
	}

	testCasesCount = getTotalTestCaseCount();
	testcasesFailCount = getFailTestCaseCount();
	testcasesPassCount = getPassTestCaseCount();

	rootElement.setAttribute("TP", testcasesPassCount);
	rootElement.setAttribute("TF", testcasesFailCount);
	rootElement.setAttribute("TotalTestCases", testCasesCount);

	try
	{
		TransformerFactory transformerFactory = 
				TransformerFactory.newInstance();

		Transformer transformer = transformerFactory.newTransformer();

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(sResultXMLFilePath));

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
			String sResultFileName = "ResultFile" + now() + ".xml";
			sResultFileName = sResultFileName.replace(":", "");
			sResultFileName = sResultFileName.replace(" ", "");
			sResultXMLFilePath = sTestResultFolderPath + sPathSeparatorChar;
			sResultXMLFilePath += sResultFileName;
			createXMLFile();
		}
	}

	private static void createXMLFile() {
		try {
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();

			Document xmlDocument = docBuilder.newDocument();
			xmlDocument.insertBefore(xmlDocument.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"Result.xsl\""), 
					xmlDocument.getDocumentElement());


			rootElement = xmlDocument.createElement("TestSuite");
			xmlDocument.appendChild(rootElement);


			rootElement.setAttribute("StartTime", now());
			rootElement.setAttribute("EndTime", now());
			rootElement.setAttribute("TotalTestCases", testCasesCount);

			rootElement.setAttribute("TF", testcasesFailCount);
			rootElement.setAttribute("TP", testcasesPassCount);


			TransformerFactory transformerFactory = 
					TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(xmlDocument);
			StreamResult result = new StreamResult(new File(sResultXMLFilePath));
			transformer.transform(source, result);
			changeToEditMode();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		return sdf.format(cal.getTime());
	}

	private static void changeToEditMode() {
		try {
			file = new FileInputStream(new File(sResultXMLFilePath));
			doc = docBuilder.parse(file);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite";
			NodeList nodeList_ele = (NodeList)xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			rootElement = (Element)nodeList_ele;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addOrUpdateFunctionalityNode()
	{
		if (!checkFunctionalNodeAvailable()) {
			testcasesFailCount = 0;
			testcasesPassCount = 0;
			Element ele1 = doc.createElement("Functionality");
			rootElement.appendChild(ele1);
			ele1.setAttribute("name", sXMLCurrentFunctionality);
			FuncElement = ele1;
		}
	}

	private static void addOrUpdateTestScriptNode()
	{
		if (!checkTestScriptNodeAvailable()) {
			Element ele = doc.createElement("TestScript");
			FuncElement.appendChild(ele);
			ele.setAttribute("name", sXMLCurrentScriptName);
			ele.setAttribute("StartTime", now());
			ele.setAttribute("EndTime", now());
			ele.setAttribute("TC_Status", "Pass");
			tsElement = ele;
		}
	}

	private static void addOrUpdateIteratorNode() {
		if (!checkIteratorNodeAvailable()) {
			Element ele = doc.createElement("Iterator");
			tsElement.appendChild(ele);
			ele.setAttribute("no", Integer.toString(Iterator));
			IteratorElement = ele;
		}
	}

	private static void addOrUpdateStepNode(String sStatus, String sStepdescription, String sStepResult)
	{
		NodeList nl = IteratorElement.getChildNodes();
		int iStepNo = nl.getLength();

		Element el = doc.createElement("step");
		IteratorElement.appendChild(el);
		el.setAttribute("no", Integer.toString(iStepNo + 1));
		Node eStep = IteratorElement.getLastChild();

		el = doc.createElement("status");
		Text txt = doc.createTextNode(sStatus);
		el.appendChild(txt);
		eStep.appendChild(el);

		el = doc.createElement("stepname");
		txt = doc.createTextNode(sStepdescription);
		el.appendChild(txt);
		eStep.appendChild(el);

		el = doc.createElement("Description");
		txt = doc.createTextNode(sStepResult);
		el.appendChild(txt);
		eStep.appendChild(el);

		el = doc.createElement("timestamp");
		txt = doc.createTextNode(now());
		el.appendChild(txt);
		eStep.appendChild(el);

		tsElement.setAttribute("EndTime", now());
		rootElement.setAttribute("EndTime", now());
		try
		{
			TransformerFactory transformerFactory = 
					TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sResultXMLFilePath));

			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean checkFunctionalNodeAvailable() {
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite/Functionality[@name='" + 
					sXMLCurrentFunctionality + "']";
			NodeList nodeList = (NodeList)xPath.compile(expression).evaluate(
					doc, XPathConstants.NODESET);
			NodeList nodeListElement = (NodeList)xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			if (nodeList.getLength() > 0) {
				FuncElement = (Element)nodeListElement;
				return true;
			}
			return false;
		} catch (Exception e) {}
		return false;
	}

	private static boolean checkTestScriptNodeAvailable()
	{
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//Functionality[@name='" + 
					sXMLCurrentFunctionality + "']/TestScript[@name='" + 
					sXMLCurrentScriptName + "']";
			NodeList nodeList = (NodeList)xPath.compile(expression).evaluate(
					doc, XPathConstants.NODESET);
			NodeList nodeListElement = (NodeList)xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			if (nodeList.getLength() > 0) {
				tsElement = (Element)nodeListElement;
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage()); }
		return false;
	}

	private static boolean checkIteratorNodeAvailable()
	{
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//Functionality[@name='" + 
					sXMLCurrentFunctionality + "']/TestScript[@name='" + 
					sXMLCurrentScriptName + "']/Iterator[@no='" + 
					sXMLCurrentIterator + "']";
			NodeList nodeList = (NodeList)xPath.compile(expression).evaluate(
					doc, XPathConstants.NODESET);
			NodeList nodeListElement = (NodeList)xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			if (nodeList.getLength() > 0) {
				IteratorElement = (Element)nodeListElement;
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage()); }
		return false;
	}

	private static int getFailTestCaseCount()
	{
		NodeList nodeList = null;
		int iSum = 0;
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite/Functionality";
			nodeList = (NodeList)xPath.compile(expression).evaluate(doc, 
					XPathConstants.NODESET);
			for (int iCount = 1; iCount <= nodeList.getLength(); iCount++) {
				String sFailExpression = "//TestSuite/Functionality[" + iCount + 
						"]/TestScript[@TC_Status='Fail']";
				NodeList TCFailnodeList = (NodeList)xPath.compile(
						sFailExpression).evaluate(doc, XPathConstants.NODESET);
				iSum += TCFailnodeList.getLength();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return iSum;
	}

	private static int getPassTestCaseCount() {
		NodeList nodeList = null;
		int iSum = 0;
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite/Functionality";
			nodeList = (NodeList)xPath.compile(expression).evaluate(doc, 
					XPathConstants.NODESET);
			for (int iCount = 1; iCount <= nodeList.getLength(); iCount++) {
				String sPassExpression = "//TestSuite/Functionality[" + iCount + 
						"]/TestScript[@TC_Status='Pass']";
				NodeList TCPassNodeList = (NodeList)xPath.compile(
						sPassExpression).evaluate(doc, XPathConstants.NODESET);
				iSum += TCPassNodeList.getLength();
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return iSum;
	}

	private static int getTotalTestCaseCount() {
		NodeList nodeList = null;
		int iSum = 0;
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite/Functionality";
			nodeList = (NodeList)xPath.compile(expression).evaluate(doc, 
					XPathConstants.NODESET);
			for (int iCount = 1; iCount <= nodeList.getLength(); iCount++) {
				String sTotalExpression = "//TestSuite/Functionality[" + iCount + 
						"]/TestScript";
				NodeList TCnodeList = (NodeList)xPath
						.compile(sTotalExpression).evaluate(doc, 
								XPathConstants.NODESET);
				iSum += TCnodeList.getLength();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return iSum;
	}

	private static void updateFailStatus() {
		tsElement.setAttribute("TC_Status", "Fail");
		try {
			TransformerFactory transformerFactory = 
					TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sResultXMLFilePath));

			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
