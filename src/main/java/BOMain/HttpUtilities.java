package BOMain;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class HttpUtilities {

	private final BO_Connection connection;

	final ObjectMapper objectMapper = new ObjectMapper();

	final ObjectMapper xmlMapper = new XmlMapper();

	public HttpUtilities(BO_Connection connection) {
		this.connection = connection;
	}

	public Object getJsonResponse(final String uri, Class mapperCalss) throws Exception {
		final String response1 = connection.query("GET", uri, "application/json");
		return objectMapper.readValue(response1, mapperCalss);
	}

	public Object getJsonResponseString(final String uri, Class mapperCalss) throws Exception {
		final String jsonAsString = connection.query("GET", uri, "application/json");
		return objectMapper.readValue(jsonAsString, mapperCalss);
	}
	
	public String getJsonResponseCHILD(String uri) throws Exception {
		final String response2 = connection.query("GET", uri, "application/json");
		return response2;
	}
	
	public String getResponseforPrompts(String uri) throws Exception {
		final String response3 = connection.query("GET", uri, "application/xml");
		return response3;
	}

	public Object getXmlResponse(final String uri, Class mapperCalss) throws Exception {
		final String response4 = connection.query("GET", uri, "text/xml");
		xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return xmlMapper.readValue(response4, mapperCalss);
	}

	public String getXMLResponseTextXml(String uri) throws Exception {
		final String response5 = connection.queryMethod("GET", uri, "text/xml");
		return response5;
	}
	
	public String getResponseXml(String uri) throws Exception {
		final String response6 = connection.query("GET", uri, "text/xml");
		return response6;
	}
	
	public String putXMLResponseAppXml(String uri, String body) throws Exception {
		final String response7 = connection.queryPutMethod("PUT", uri, "application/xml", body);
		return response7;
	}
	
	public String getXMLResponseAppXml(String uri) throws Exception {
		final String response8 = connection.queryMethod("GET", uri, "application/xml");
		return response8;
	}
}
