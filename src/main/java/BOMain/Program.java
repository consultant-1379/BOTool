package BOMain;

import static jsonPojo.URLConstants.DOCUMENT;
import static jsonPojo.URLConstants.DOCUMENT_DETAILS_URL;
import static jsonPojo.URLConstants.DOCUMENT_DATA_PROVIDER_BYID_URL;
import static jsonPojo.URLConstants.DOCUMENT_DATA_PROVIDER_URL;
import static jsonPojo.URLConstants.DOCUMENT_REPORTS_BYID_URL;
import static jsonPojo.URLConstants.DOCUMENT_REPORTS_URL;
import static jsonPojo.URLConstants.DOCUMENT_REPORTS_URL1;
import static jsonPojo.URLConstants.DOCUMENT_REPORTS_URL2;
import static jsonPojo.URLConstants.DOCUMENT_VARIABLES_URL;
import static jsonPojo.URLConstants.DOCUMENT_VARIABLE_BYID_URL;
import static jsonPojo.URLConstants.QUERY_PROPERTY_URL;
import static jsonPojo.URLConstants.DOCUMENT_LINK_URL;
import static jsonPojo.URLConstants.DOCUMENT_LINK_BYID_URL;
import static jsonPojo.URLConstants.REPORT_ELEMENTS_URL;
import static jsonPojo.URLConstants.REPORT_ELEMENT_DETAILS_URL;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import jsonPojo.Query.Query;
import jsonPojo.Query.QueryMapper;
import jsonPojo.Reports.Reports;
import jsonPojo.Reports.ReportsMapper;
import jsonPojo.Reports.ReportsWrapper;
import jsonPojo.Variables.Formula;
import jsonPojo.Variables.FormulaMapper;
import jsonPojo.Variables.VariableMapper;
import jsonPojo.Variables.VariableWrapper;
import jsonPojo.Variables.Variables;
import jsonPojo.dataProviders.DataProviderWrapperMapper;
import jsonPojo.dataProviders.DataProviders;
import jsonPojo.dataProviders.DataProvidersMapper;
import jsonPojo.dataProviders.Tables.Report;
import jsonPojo.documents.DocumentMapper;
import jsonPojo.documents.FirstLevelDcumentMapper;
import java.text.SimpleDateFormat;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class Program {

	public static String serverURI;

	public static String report_Id;

	public static String reportName;

	BO_Connection connection;

	static HttpUtilities httputils;

	BufferedWriter queryPropWriter, queryWriter, variableWriter, reportsWriter;

	String newLine = System.getProperty("line.separator");

	public static HashMap<String, String> dimensionIdAndNameMap = new HashMap<>();
	public static ArrayList<String> measuresList = new ArrayList<String>();

	public static LinkedHashMap<String, String> oldElementIdAndTextMap = new LinkedHashMap<>();
	public static LinkedHashMap<String, String> newElementIdAndTextMap = new LinkedHashMap<>();
	public static LinkedHashMap<String, String> deltaElementIdAndTextMap = new LinkedHashMap<>();

	public Program(String serverURI, String username, String password, String report_Id, String directoryPath)
			throws Exception {
		Program.serverURI = serverURI;
		Program.report_Id = report_Id;

		connection = new BO_Connection(serverURI);
		connection.connect(username, password, "SecEnterprise");
		httputils = new HttpUtilities(connection);

		// Getting the Document/BO Web-intelligence Report Name
		final String documentDetailsUri = String.format(DOCUMENT_DETAILS_URL, serverURI, report_Id);
		
		String xmlResponseReportName = httputils.getXMLResponseAppXml(documentDetailsUri);
		String reportName = getReportName(xmlResponseReportName);

		Program.reportName = reportName;

		if (directoryPath != null) {
			reportName = reportName.replace('/','_');
			final String queryPropFilePath = directoryPath + "\\" + reportName + "_" + report_Id + "_QueryProperties_";
			final String queryFilePath = directoryPath + "\\" + reportName + "_" + report_Id + "_Query_";
			final String variablesFilePath = directoryPath + "\\" + reportName + "_" + report_Id + "_Variables_";
			final String reportsFilePath = directoryPath + "\\" + reportName + "_" + report_Id + "_Reports_";

			queryPropWriter = FileUtil.createFileAdnWriter(queryPropFilePath);
			queryWriter = FileUtil.createFileAdnWriter(queryFilePath);
			variableWriter = FileUtil.createFileAdnWriter(variablesFilePath);
			reportsWriter = FileUtil.createFileAdnWriter(reportsFilePath);

			String querypropheader = "Report Id, Data Provider Name, Data Provider ID, Query Specification, Maximum Retrieval Time In Seconds, Maximum Rows Retrieved";
			queryPropWriter.write(String.valueOf(querypropheader));
			queryPropWriter.write(System.lineSeparator());

			String queryheader = "DataProvider, TableName, SQL Query";
			queryWriter.write(String.valueOf(queryheader));
			queryWriter.write(System.lineSeparator());

			String variableheader = "Data Type, Id, ColumnName, Formula";
			variableWriter.write(String.valueOf(variableheader));
			variableWriter.write(System.lineSeparator());

			String reportheader = "Document Name, Report Id, Report Name, Report Type, View Mode, Multiple Pages in Table/Chart, Report Heading, Page Header Elements, Table Headers, Columns, Category1, Value1, Col2, Category2, Value2, Col3, Category3, Value3";
			reportsWriter.write(String.valueOf(reportheader));
			reportsWriter.write(System.lineSeparator());

		}
	}

	public Program() {
		// TODO Auto-generated constructor stub
	}

	private String getReportName(String xmlResponseReportName) {

		String reportName = "";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlResponseReportName)));
			Node node = doc.getElementsByTagName("name").item(0);
			reportName = node.getTextContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportName;
	}

	public void processQuerySpecification() throws Exception {

		final String documentUrl = String.format(DOCUMENT, getServerURI());
		final DocumentMapper documentMapper = ((FirstLevelDcumentMapper) httputils.getJsonResponse(documentUrl,
				FirstLevelDcumentMapper.class)).getMapper();

		final String dataProviderUri = String.format(DOCUMENT_DATA_PROVIDER_URL, getServerURI(), report_Id);
		final DataProvidersMapper mapper = ((DataProviderWrapperMapper) httputils.getJsonResponse(dataProviderUri,
				DataProviderWrapperMapper.class)).getDataProvidersMapper();

		for (final DataProviders dataprovider1 : mapper.getDataProvider()) {

			String dataProviderId = dataprovider1.getId();
			String dataProviderName = dataprovider1.getDataProviderName();

			String qpUrl = String.format(QUERY_PROPERTY_URL, getServerURI(), report_Id, dataProviderId);
			String xmlQPResponse = httputils.getXMLResponseTextXml(qpUrl);
			String maxRetrievalTime = getMaxRetrievalTime(xmlQPResponse);
			String maxRowsRetrieved = getMaxRowsRetrieved(xmlQPResponse);

			String finalString = report_Id + "," + dataProviderName.replaceAll(",", "#") + "," + dataProviderId + ","
					+ xmlQPResponse.replaceAll(",", "#") + "," + maxRetrievalTime.toString() + ","
					+ maxRowsRetrieved.toString();

			FileUtil.write(queryPropWriter, finalString);
		}

		System.out.println(newLine + "Query Properties File Created." + newLine);

	}

	public void processDocuments() throws Exception {

		final String dataProviderUri = String.format(DOCUMENT_DATA_PROVIDER_URL, getServerURI(), report_Id);
		final DataProvidersMapper mapper = ((DataProviderWrapperMapper) httputils.getJsonResponse(dataProviderUri,
				DataProviderWrapperMapper.class)).getDataProvidersMapper();

		final String VariableUri = String.format(DOCUMENT_VARIABLES_URL, getServerURI(), report_Id);
		final VariableMapper vmapper = ((VariableWrapper) httputils.getJsonResponse(VariableUri, VariableWrapper.class))
				.getMapper();

		for (final DataProviders dataprovider : mapper.getDataProvider()) {
			final String dataProviderByIdUri = String.format(DOCUMENT_DATA_PROVIDER_BYID_URL, getServerURI(), report_Id,
					dataprovider.getId());
			Query queryMapper = ((QueryMapper) httputils.getJsonResponse(dataProviderByIdUri, QueryMapper.class))
					.getDpQuery();
			String sql_query = queryMapper.getFormattedString();
			FileUtil.write(queryWriter, sql_query);
		}
		System.out.println("SQL query file created." + newLine);

		for (final Variables variable : vmapper.getVariables()) {
			final String variableByIdUri = String.format(DOCUMENT_VARIABLE_BYID_URL, getServerURI(), report_Id,
					variable.getId());
			//System.out.println(variableByIdUri);
			Formula formulaMapper = ((FormulaMapper) httputils.getJsonResponse(variableByIdUri, FormulaMapper.class))
					.getDpQuery();
			String formula = formulaMapper.getFormattedString();
			
			FileUtil.write(variableWriter, formula);
		}
		System.out.println("Variables and Formulas added to VariablesBO file." + newLine);
	}

	public void processReports() throws Exception {

		final String ReportUri = String.format(DOCUMENT_REPORTS_URL, getServerURI(), report_Id);

		final ReportsMapper rmapper = ((ReportsWrapper) httputils.getJsonResponse(ReportUri, ReportsWrapper.class))
				.getMapper();

		for (final Reports report : rmapper.getReports()) {

			final String reportByIdUri1 = String.format(DOCUMENT_REPORTS_URL1, getServerURI(), report_Id,
					report.getId());
			String xmlRdResponse1 = httputils.getXMLResponseAppXml(reportByIdUri1);
			String viewMode = getReportViewMode(xmlRdResponse1);

			final String reportByIdUri2 = String.format(DOCUMENT_REPORTS_URL2, getServerURI(), report_Id,
					report.getId());
			String xmlRdResponse2 = httputils.getXMLResponseTextXml(reportByIdUri2);
			String multiplePagesBoolean = checkMultipleOrSinglePage(xmlRdResponse2);

			final String reportByIdUri = String.format(DOCUMENT_REPORTS_BYID_URL, getServerURI(), report_Id,
					report.getId());
			String xmlRdResponse3 = httputils.getXMLResponseTextXml(reportByIdUri);
			ArrayList<String> pageHeaderElements = getReportHeading(xmlRdResponse3);
			String reportHeading = pageHeaderElements.get(0).replaceFirst("=", "").replaceAll("\"", "");

			String pageHeaderElementsString = "";
			String pageHeaderElementsFinalString = "";
			for (String s : pageHeaderElements) {
				pageHeaderElementsString = pageHeaderElementsString + s + "\n";
			}
			pageHeaderElementsFinalString = "\"" + pageHeaderElementsString + "\"";

			String tableHeaders = getTableHeaders(xmlRdResponse3);

			// Adding all Column Values as Arguments in the Final Formatted String Method
			Report reportXml = (Report) (httputils.getXmlResponse(reportByIdUri, Report.class));
			String finalStringReport = reportXml.getFormattedString(reportName, viewMode, multiplePagesBoolean,
					reportHeading, pageHeaderElementsFinalString, tableHeaders);
			FileUtil.write(reportsWriter, finalStringReport);
		}

		// Adding All Dimensions and Measures in Reports.csv File
		getDimensionsMeasures();
		writeDimensionsMeasures();

		System.out.println("Report structure fetched in ReportsBO file.");
	}
	
	private static ArrayList<String> getElementIdsListforGetRequest(String xmlResponse4) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String elementId = "";
		ArrayList<String> elementIdList = new ArrayList<>();

		try {
			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlResponse4)));
			NodeList nodelist = doc.getElementsByTagName("id");
			for (int i = 0; i < nodelist.getLength(); i++) {
				Node node = nodelist.item(i);
				if (!(node.getTextContent() == null)) {
					elementId = node.getTextContent();
				}
				elementIdList.add(elementId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return elementIdList;
	}

	private void getDimensionsMeasures() {
		dimensionIdAndNameMap.clear();
		measuresList.clear();
		final String dataProviderUri = String.format(DOCUMENT_DATA_PROVIDER_URL, getServerURI(), report_Id);
		try {
			String dp_response = httputils.getJsonResponseCHILD(dataProviderUri);
			JSONParser parse = new JSONParser();
			JSONObject jobj = (JSONObject) parse.parse(dp_response);
			JSONObject jobj2 = (JSONObject) jobj.get("dataproviders");
			JSONArray jsonarr = (JSONArray) jobj2.get("dataprovider");

			for (int i = 0; i < jsonarr.size(); i++) {
				JSONObject obj = (JSONObject) jsonarr.get(i);
				String dp_id = (String) obj.get("id");
				String dataProviderByIdUri = String.format(DOCUMENT_DATA_PROVIDER_BYID_URL, getServerURI(), report_Id,
						dp_id);
				String dp_desc = httputils.getJsonResponseCHILD(dataProviderByIdUri);
				jobj = (JSONObject) parse.parse(dp_desc);
				jobj2 = (JSONObject) jobj.get("dataprovider");
				JSONObject jobj3 = (JSONObject) jobj2.get("dictionary");
				JSONArray inner_jsonarr = (JSONArray) jobj3.get("expression");

				for (int j = 0; j < inner_jsonarr.size(); j++) {
					JSONObject inner_obj = (JSONObject) inner_jsonarr.get(j);
					String column_id = (String) inner_obj.get("id");
					String column_name = (String) inner_obj.get("formulaLanguageId");
					String qualification = (String) inner_obj.get("@qualification");
					if (qualification.equals("Dimension")) {
						dimensionIdAndNameMap.put(column_id, column_name);
					}
					if (qualification.equals("Measure")) {
						measuresList.add(column_name);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeDimensionsMeasures() throws IOException {

		reportsWriter.write(System.lineSeparator());

		String res;
		ArrayList<String> singleDimensionKeyList = new ArrayList<>();
		ArrayList<String> mergedDimensionKeysList = new ArrayList<String>();
		LinkedHashSet<String> mergedDimensionHeaderSet = new LinkedHashSet<String>();
		HashMap<String, String> allDimensionsMap = new HashMap<>();
		HashMap<String, String> mergeDimensionIdAndNameMap = new HashMap<String, String>();
		HashMap<String, ArrayList<String>> mergeDimenNameAndKeysIdsMap = new HashMap<>();

		allDimensionsMap.putAll(dimensionIdAndNameMap);

		final String LINKUri = String.format(DOCUMENT_LINK_URL, getServerURI(), report_Id);

		try {
			res = httputils.getJsonResponseCHILD(LINKUri);
			mergeDimensionIdAndNameMap = getMergeDimensionIdAndName(res);

			for (String id : mergeDimensionIdAndNameMap.keySet()) {
				String LINK_BYID_URL = String.format(DOCUMENT_LINK_BYID_URL, getServerURI(), report_Id, id);
				String mergeDimenResponse = "";
				try {
					mergeDimenResponse = httputils.getJsonResponseCHILD(LINK_BYID_URL);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mergeDimenNameAndKeysIdsMap.put(mergeDimensionIdAndNameMap.get(id),
						fetchMergeDimensionKeysIdDetails(mergeDimenResponse));
			}
			for (String mergeDimenName : mergeDimenNameAndKeysIdsMap.keySet()) {
				mergedDimensionHeaderSet.add(mergeDimenName);
				for (String mergeDimenKeysId : mergeDimenNameAndKeysIdsMap.get(mergeDimenName)) {
					if (allDimensionsMap.get(mergeDimenKeysId) != null) {
						String keyFromMergeDimension = allDimensionsMap.get(mergeDimenKeysId);
						String mergeDimensionNameAndKeysString = mergeDimenName + ":" + keyFromMergeDimension;
						mergedDimensionKeysList.add(mergeDimensionNameAndKeysString);
						allDimensionsMap.remove(mergeDimenKeysId);
					}
				}
			}
			for (String s : allDimensionsMap.keySet()) {
				singleDimensionKeyList.add(allDimensionsMap.get(s));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		FileUtil.write(reportsWriter, "Dimensions");
		reportsWriter.write(System.lineSeparator());
		FileUtil.write(reportsWriter, "Merged Dimesions," + "," + "," + String.join(",", mergedDimensionHeaderSet));

		ArrayList<String> allKeysList = new ArrayList<>();

		for (String s : mergedDimensionHeaderSet) {
			ArrayList<String> mergedDimensionKeysList1 = new ArrayList<String>();
			for (String value1 : mergedDimensionKeysList) {
				String[] valueArray = value1.split(":", 2);
				if (s.equals(valueArray[0])) {
					mergedDimensionKeysList1.add(valueArray[1]);
				}
			}
			Collections.sort(mergedDimensionKeysList1);
			String keysValueString = "";
			String keysValueFinalString = "";
			for (String r : mergedDimensionKeysList1) {
				keysValueString = keysValueString + r + "\n";
			}
			keysValueFinalString = "\"" + keysValueString + "\"";
			allKeysList.add(keysValueFinalString);
		}

		allKeysList.addAll(singleDimensionKeyList);

		FileUtil.write(reportsWriter, "Keys," + "," + "," + String.join(",", allKeysList));

		reportsWriter.write(System.lineSeparator());
		FileUtil.write(reportsWriter, "Measures");
		reportsWriter.write(System.lineSeparator());

		Collections.sort(measuresList);
		String measuresValueString = "";
		String measuresValueFinalString = "";
		for (String s : measuresList) {
			measuresValueString = measuresValueString + s + "\n";
		}
		measuresValueFinalString = "\"" + measuresValueString + "\"";

		FileUtil.write(reportsWriter, measuresValueFinalString);

	}

	private HashMap<String, String> getMergeDimensionIdAndName(String res) {
		HashMap<String, String> mergeDimenIdNameMap = new HashMap<>();
		String name = "";
		JSONParser parse = new JSONParser();
		try {
			JSONObject jobj = (JSONObject) parse.parse(res);
			JSONObject jsonObj = (JSONObject) jobj.get("links");
			JSONArray jsonarr = (JSONArray) jsonObj.get("link");
			for (int i = 0; i < jsonarr.size(); i++) {
				JSONObject obj = (JSONObject) jsonarr.get(i);
				name = (String) obj.get("name");
				mergeDimenIdNameMap.put((String) obj.get("id"), name);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return mergeDimenIdNameMap;
	}

	private ArrayList<String> fetchMergeDimensionKeysIdDetails(String mergeDimenResponse) {
		JSONParser parse = new JSONParser();
		ArrayList<String> subKeysId = new ArrayList<>();
		try {
			JSONObject jobj = (JSONObject) parse.parse(mergeDimenResponse);
			JSONObject jsonObj = (JSONObject) jobj.get("link");
			JSONObject jsonObj2 = (JSONObject) jsonObj.get("linkedExpressions");
			JSONArray jsonarr = (JSONArray) jsonObj2.get("linkedExpression");
			for (int i = 0; i < jsonarr.size(); i++) {
				JSONObject obj = (JSONObject) jsonarr.get(i);
				subKeysId.add((String) obj.get("@id"));
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return subKeysId;
	}

	private String getMaxRowsRetrieved(String xmlQPResponse) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String maxRowsRetrieved = "";

		try {
			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlQPResponse)));
			Node node = doc.getElementsByTagName("maxRowsRetrievedProperty").item(0);
			if (node.hasAttributes()) {
				NamedNodeMap nodeMap = node.getAttributes();
				Node tempNode = nodeMap.item(0);
				if (tempNode.getNodeName() == "value") {
					maxRowsRetrieved = tempNode.getNodeValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return maxRowsRetrieved;
	}

	private String getMaxRetrievalTime(String xmlQPResponse) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String maxRetrievalTime = "";

		try {
			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlQPResponse)));
			Node node = doc.getElementsByTagName("maxRetrievalTimeInSecondsProperty").item(0);
			if (node.hasAttributes()) {
				NamedNodeMap nodeMap = node.getAttributes();
				Node tempNode = nodeMap.item(0);
				if (tempNode.getNodeName() == "value") {
					maxRetrievalTime = tempNode.getNodeValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return maxRetrievalTime;
	}

	private String getTableHeaders(String report_response) {

		ArrayList<String> tableHeadersList = new ArrayList<String>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String tableHeaderFinalString = "";

		try {
			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(report_response)));

			boolean headerFlag = false;
			NodeList nList = doc.getElementsByTagName("ROWGROUP");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node node = nList.item(temp);
				headerFlag = false;

				if (node.hasAttributes()) {
					NamedNodeMap attrMap = node.getAttributes();
					for (int j = 0; j < attrMap.getLength(); j++) {
						Node attr = attrMap.item(j);
						if (attr.getNodeName() == "type" && attr.getTextContent().equalsIgnoreCase("header")) {
							headerFlag = true;
						}
					}
				}
				if (node.hasChildNodes()) {
					NodeList nodeMap = node.getChildNodes();

					for (int i = 0; i < nodeMap.getLength(); i++) {
						Node tempNode = nodeMap.item(i);

						if (headerFlag && tempNode.hasChildNodes()) {
							NodeList nodeMap2 = tempNode.getChildNodes();
							for (int j = 0; j < nodeMap2.getLength(); j++) {
								Node node_child = nodeMap2.item(j);
								if (node_child.getNodeName() == "TDCELL") {

									if (node_child.hasChildNodes()) {
										NodeList nodeMap3 = node_child.getChildNodes();

										for (int k = 0; k < nodeMap3.getLength(); k++) {

											Node node_child2 = nodeMap3.item(k);
											if (node_child2.getNodeName() == "CONTENT") {

												String table_details_temp = node_child2.getTextContent()
														.replaceAll(",", "#").replaceFirst("=", "").replaceAll("\"", "")
														.trim();
												tableHeadersList.add(table_details_temp);
												//System.out.println("tableHeaders:" + tableHeadersList);
											}
										}
									}
								}
							}

						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!tableHeadersList.isEmpty()) {
			String tableHeaderString = "";
			for (String s1 : tableHeadersList) {
				tableHeaderString = tableHeaderString + s1 + "\n";
			}
			tableHeaderFinalString = "\"" + tableHeaderString + "\"";
		
		} else {
			tableHeaderFinalString = "N/A";
		}

		return tableHeaderFinalString;

	}

	private String getReportViewMode(String xmlRdResponse1) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String viewmode = "";

		try {
			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlRdResponse1)));
			Node node = doc.getElementsByTagName("paginationMode").item(0);
			viewmode = node.getTextContent().toString();
			//System.out.println("viewmode:" +viewmode);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return viewmode;
	}

	private String checkMultipleOrSinglePage(String xmlRdResponse2) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String booleanResponse = "";
		ArrayList<String> ctValuesList = new ArrayList<String>();

		try {
			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlRdResponse2)));
			Node node1 = doc.getElementsByTagName("page").item(0);
			if (node1.hasChildNodes()) {
				Node node2 = node1.getFirstChild();
				NodeList nList1 = node2.getChildNodes();
				for (int i = 0; i < nList1.getLength(); i++) {
					Node node3 = nList1.item(i);
					if (node3.getNodeName().equals("cell")) {
						if (node3.hasChildNodes()) {
							NodeList nList2 = node3.getChildNodes();
							for (int j = 0; j < nList2.getLength(); j++) {
								Node node4 = nList2.item(j);
								if (node4.getNodeName().equals("ct")) {
									String ctValue = node4.getTextContent();
									ctValuesList.add(ctValue);
								}
							}
						}
					}
				}
			}

			if (!ctValuesList.isEmpty()) {
				if (ctValuesList.contains("1/1")) {
					booleanResponse = "No";
				} else
					booleanResponse = "Yes";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return booleanResponse;
	}

	private ArrayList<String> getReportHeading(String xmlRdResponse3) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String reportHeading = "";

		ArrayList<String> reportHeadersList = new ArrayList<String>();

		try {

			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlRdResponse3)));
			Node node1 = doc.getElementsByTagName("PAGE_HEADER").item(0);
			if (node1.hasChildNodes()) {
				NodeList nList1 = node1.getChildNodes();
				for (int i = 0; i < nList1.getLength(); i++) {
					Node node2 = nList1.item(i);
					if (node2.getNodeName().equals("CELL")) {
						if (node2.hasChildNodes()) {
							NodeList nList2 = node2.getChildNodes();
							for (int j = 0; j < nList2.getLength(); j++) {
								Node node3 = nList2.item(j);
								if (node3.getNodeName().equals("CONTENT")) {
									if (!node3.getTextContent().toString().equals("=\"\"")) {
										reportHeading = node3.getTextContent().replaceAll(",", "#");
										reportHeading = reportHeading.replaceFirst("=", "").replaceAll("\"", "");
										reportHeadersList.add(reportHeading);
										//System.out.println("report::" + reportHeadersList);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportHeadersList;
	}

	public ArrayList<String> getDPNames() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String dpName = "";
		ArrayList<String> dpNamesList = new ArrayList<>();

		try {
			String dataProviderUriLink = String.format(DOCUMENT_DATA_PROVIDER_URL, getServerURI(), report_Id);
			String dp_response1 = httputils.getXMLResponseAppXml(dataProviderUriLink);

			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(dp_response1)));
			NodeList nodelist = doc.getElementsByTagName("name");
			for (int i = 0; i < nodelist.getLength(); i++) {
				Node node = nodelist.item(i);
				dpName = node.getTextContent();
				dpNamesList.add(dpName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dpNamesList;
	}

	public LinkedHashMap<String, Integer> getTabNamesAndId() throws Exception {

		LinkedHashMap<String, Integer> tabNameIdMap = new LinkedHashMap<>();

		final String ReportUri = String.format(DOCUMENT_REPORTS_URL, getServerURI(), report_Id);

		final ReportsMapper rmapper = ((ReportsWrapper) httputils.getJsonResponse(ReportUri, ReportsWrapper.class))
				.getMapper();

		for (final Reports report : rmapper.getReports()) {

			String reportName = report.getName();
			int reportId = report.getId();
			tabNameIdMap.put(reportName, reportId);
		}

		return tabNameIdMap;
	}

	public static void getElementDataInExcelFile(String excelOutputPath, String selectedItem, int tabId)
			throws Exception {

		final String excelDataFilePath = excelOutputPath + "\\" + reportName + "_" + selectedItem + "_ElementsData_"
				+ (new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())) + ".xls";

		final File excelFile = new File(excelDataFilePath);
		WritableWorkbook writableWorkbook = null;
		if (!excelFile.exists() && !excelFile.isFile()) {
			writableWorkbook = Workbook.createWorkbook(excelFile);
		} else {
			final Workbook workbook = Workbook.getWorkbook(excelFile);
			writableWorkbook = Workbook.createWorkbook(excelFile, workbook);
		}
		final WritableSheet writableSheet = writableWorkbook.createSheet("Elements Data", 0);

		final Label HeadingLabel = new Label(0, 0, "Element Data of Tab " + selectedItem + " of Report " + reportName);
		final Label NoteLabel = new Label(0, 2,
				"Note: For Updation of Report, Follow these Steps:-  1)Make the changes in Element Text Column. 2)Save and Close the Excel File. 3)Re-Upload the file through UI of the Tool ");
		final Label label1 = new Label(0, 4, "Element Id");
		final Label label2 = new Label(1, 4, "Element Text");
		writableSheet.addCell(HeadingLabel);
		writableSheet.addCell(NoteLabel);
		writableSheet.addCell(label1);
		writableSheet.addCell(label2);

		final String reportElementUri = String.format(REPORT_ELEMENTS_URL, getServerURI(), report_Id, tabId);
		//System.out.println("elementuri:"  + reportElementUri);
		String xmlResponse4 = httputils.getXMLResponseAppXml(reportElementUri);
		//System.out.println("xmll:" + xmlResponse4);
		ArrayList<String> elementIdsList = getElementIdsListforGetRequest(xmlResponse4);
		//System.out.println("id:" + elementIdsList);

		int k = 6;

		for (String elementId : elementIdsList) {

			final String reportElementIdUri = String.format(REPORT_ELEMENT_DETAILS_URL, getServerURI(), report_Id,
					tabId, elementId);
			String xmlResponse5 = httputils.getXMLResponseAppXml(reportElementIdUri);
			String formulaText = getFormulaText(xmlResponse5);

			if (!formulaText.equals("**N/A**")) {
				Label label3 = new Label(0, k, elementId);
				Label label4 = new Label(1, k, formulaText);
				writableSheet.addCell(label3);
				writableSheet.addCell(label4);
				oldElementIdAndTextMap.put(elementId, formulaText);
				k++;
			}
		}

		writableWorkbook.write();
		writableWorkbook.close();
		System.out.println("Elements Data Sheet is created");

		JOptionPane.showMessageDialog(null, "The Excel output file available at : " + excelOutputPath,
				"BO Reports Tool", JOptionPane.PLAIN_MESSAGE);
	}

	private static String getFormulaText(String xmlRdResponse4) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String formulaText = "";

		try {
			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlRdResponse4)));
			NodeList nl = doc.getElementsByTagName("formula");
			if (nl.getLength() > 0) {
				Node node = nl.item(0);
				if (!(node.getTextContent() == null)) {
					formulaText = node.getTextContent();
				}
			}
			else {
			formulaText = "**N/A**";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return formulaText;
	}

	public static void putUpdatedElementDataInFile(String uploadExcelFilePath, String selectedItem, int tabId)
			throws Exception {

		File uploadExcelFile = new File(uploadExcelFilePath);
		FileInputStream inputStream1 = null;

		if (uploadExcelFile.toString().toLowerCase().endsWith(".xlsx")
				|| uploadExcelFile.toString().toLowerCase().endsWith(".xls")) {

			org.apache.poi.ss.usermodel.Workbook workbook1 = null;
			try {
				inputStream1 = new FileInputStream(uploadExcelFile);
			} catch (FileNotFoundException e) {
				System.out.println("\tException while accessing input model-t file: " + uploadExcelFile.toString()
						+ "\n" + e + "\n");
				e.printStackTrace();
			}
			try {
				workbook1 = WorkbookFactory.create(inputStream1);
			} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
				System.out.println("Exception while loading model-t excel: " + e + "\n");
				e.printStackTrace();
			}

			Sheet elementsDataSheet = workbook1.getSheet("Elements Data");

			if (elementsDataSheet != null) {

				String columnValueElementId = "", columnValueFormulaText = "";

				for (Row row : elementsDataSheet) {

					if (row.getRowNum() > 5) {

						Cell cellElementId = row.getCell(0);
						Cell cellFormulaText = row.getCell(1);
						columnValueElementId = getColumnValueWithoutConvertInt(cellElementId);
						columnValueFormulaText = getColumnValueWithoutConvertInt(cellFormulaText);
						newElementIdAndTextMap.put(columnValueElementId, columnValueFormulaText);

					}
				}
			}

			// Getting the Delta Element Data to be Updated in the Report
			deltaElementIdAndTextMap = getDeltaElementData(oldElementIdAndTextMap, newElementIdAndTextMap);

			// Putting Request for the changed Element Data

			for (Entry<String, String> putResponseMap : deltaElementIdAndTextMap.entrySet()) {

				String elementId = putResponseMap.getKey();
				String updatedText = putResponseMap.getValue();

				String reportElementIdDynamicUri = String.format(REPORT_ELEMENT_DETAILS_URL, getServerURI(), report_Id,
						tabId, elementId);

				String xmlResponse6 = httputils.getXMLResponseAppXml(reportElementIdDynamicUri);
				String updatedXmlResponse = updateXmlResponse(xmlResponse6, updatedText);

				String xmlResponse7 = httputils.putXMLResponseAppXml(reportElementIdDynamicUri, updatedXmlResponse);

				String FinalStatus = "Put Request Element " + elementId + " Status:- " + xmlResponse7;

				System.out.println(FinalStatus);
			}

			final String documentUri = String.format(DOCUMENT_DETAILS_URL, getServerURI(), report_Id);
			String xmlResponseDocDetails = httputils.putXMLResponseAppXml(documentUri, null);
			System.out.println("Put Request Report Status:- " + xmlResponseDocDetails);
			JOptionPane.showMessageDialog(null, xmlResponseDocDetails, "BO Reports Tool", JOptionPane.PLAIN_MESSAGE);
		}
	}

	private static String updateXmlResponse(String xmlResponse6, String updatedText) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String xmlString = "";
		

		try {
			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlResponse6)));
			Node node = doc.getElementsByTagName("formula").item(0);
			node.setTextContent(updatedText);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource src = new DOMSource(doc);
			StringWriter xmlwriter = new StringWriter();
			StreamResult res = new StreamResult(xmlwriter);
			transformer.transform(src, res);
			StringBuffer sb = xmlwriter.getBuffer(); 
			xmlString = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlString;
	}

	private static LinkedHashMap<String, String> getDeltaElementData(
			LinkedHashMap<String, String> oldElementIdAndTextMap2,
			LinkedHashMap<String, String> newElementIdAndTextMap2) {

		LinkedHashMap<String, String> deltaElementsMap = new LinkedHashMap<>();

		for (Entry<String, String> entry1 : oldElementIdAndTextMap2.entrySet()) {
			for (Entry<String, String> entry2 : newElementIdAndTextMap2.entrySet()) {
				String oldElementId = entry1.getKey();
				String oldTextContent = entry1.getValue();

				String newElementId = entry2.getKey();
				String newTextContent = entry2.getValue();

				if (oldElementId.equals(newElementId)) {
					if (!oldTextContent.equals(newTextContent)) {
						deltaElementsMap.put(newElementId, newTextContent);
					}
				}
			}
		}
		return deltaElementsMap;
	}

	public static String getColumnValueWithoutConvertInt(Cell cell) {
		String s = null;
		Double d = null;
		if (!(cell == null || String.valueOf(cell).isEmpty())) {
			switch (cell.getCellType()) {
			case 0:
				d = cell.getNumericCellValue();
				s = String.valueOf(d);
				break;
			case 1:
				s = cell.getStringCellValue();
				break;
			default:
				break;
			}
		}
		return s;
	}

	public void closeAllFiles() throws IOException {
		FileUtil.close(queryPropWriter);
		FileUtil.close(queryWriter);
		FileUtil.close(variableWriter);
		FileUtil.close(reportsWriter);
	}

	public static void main(String[] args) throws Exception {
		
		final Program bOption=new Program();
		final String s = bOption.selectParsingType();

		 if (s == "Select option") {
	            System.out.println("Required Option is not selected");
	            bOption.ShowErrorMessage();
	            //System.exit(0);
	        }
		 
	    if (s == "Generate Report") {
		final guiOption gui = new guiOption();
		gui.setSize(new Dimension(925,550));
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - gui.getWidth()) / 2;
		final int y = (screenSize.height - gui.getHeight()) / 2;
		gui.setLocation(x, y);
		gui.setTitle("BO Reports Tool");
		gui.setVisible(true);
		}
	    
	    if(s=="Get Delta")
	    {
	    	System.out.println("Get Delta is selected");
            final deltaGui fd = new deltaGui();
            fd.setSize(new Dimension(800, 575));
            final Toolkit toolkit = Toolkit.getDefaultToolkit();
            final Dimension screenSize = toolkit.getScreenSize();
            final int x = (screenSize.width - fd.getWidth()) / 2;
            final int y = (screenSize.height - fd.getHeight()) / 2;
            fd.setLocation(x, y);
            fd.setTitle("BO Delta Report");
            fd.setVisible(true);
	    }

	}
	private String ShowErrorMessage() {
		 final JOptionPane jp = new JOptionPane();
	        jp.showMessageDialog(null, "Please select the Report Format", "BO Analyzer", JOptionPane.ERROR_MESSAGE);
	        return null;
	}

	public String selectParsingType() {
        final JOptionPane jp = new JOptionPane();
        final String option[] = { "Select option", "Generate Report", "Get Delta"};
        
        final String s = (String) jp.showInputDialog(null, "Select the Option", "BO Tool", JOptionPane.PLAIN_MESSAGE, null, option,
                option[0]);
        return s;
    }

	public static String getServerURI() {
		return serverURI;
	}
}
