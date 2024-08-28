package jsonPojo;

public class URLConstants {

	public static String LOGIN = "http://%s:6405/biprws";
	public static String DOCUMENT = "http://%s:6405/biprws/raylight/v1/documents";
	public static String DOCUMENT_DETAILS_URL = "http://%s:6405/biprws/raylight/v1/documents/%s";
	public static String DOCUMENT_DATA_PROVIDER_URL = "http://%s:6405/biprws/raylight/v1/documents/%s/dataproviders";
	public static String DOCUMENT_DATA_PROVIDER_BYID_URL = "http://%s:6405/biprws/raylight/v1/documents/%s/dataproviders/%s";
	
	public static String QUERY_PROPERTY_URL = "http://%s:6405/biprws/raylight/v1/documents/%s/dataproviders/%s/specification";

	public static String DOCUMENT_VARIABLES_URL = "http://%s:6405/biprws/raylight/v1/documents/%s/variables";
	public static String DOCUMENT_VARIABLE_BYID_URL = "http://%s:6405/biprws/raylight/v1/documents/%s/variables/%s";

	public static String DOCUMENT_REPORTS_URL = "http://%s:6405/biprws/raylight/v1/documents/%s/reports";
	public static String DOCUMENT_REPORTS_URL1 = "http://%s:6405/biprws/raylight/v1/documents/%s/reports/%s";
	public static String DOCUMENT_REPORTS_URL2 = "http://%s:6405/biprws/raylight/v1/documents/%s/reports/%s/pages";
	public static String DOCUMENT_REPORTS_BYID_URL = "http://%s:6405/biprws/raylight/v1/documents/%s/reports/%s/specification";
	
	public static String DOCUMENT_LINK_URL = "http://%s:6405/biprws/raylight/v1/documents/%s/links";
	public static String DOCUMENT_LINK_BYID_URL = "http://%s:6405/biprws/raylight/v1/documents/%s/links/%s";
	
    public static String REPORT_ELEMENTS_URL = "http://%s:6405/biprws/raylight/v1/documents/%s/reports/%s/elements";
    public static String REPORT_ELEMENT_DETAILS_URL = "http://%s:6405/biprws/raylight/v1/documents/%s/reports/%s/elements/%s";
    
}
