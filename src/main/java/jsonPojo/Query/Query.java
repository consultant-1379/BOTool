package jsonPojo.Query;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Query {
	private String id;

	private String name;

	private String query;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) throws IOException {
		this.query = query;
	}
	
	public String getFormattedString() {
		return id + "," + name + "," + "\"" + query + "\"";
	}
	

	@Override
	public String toString() {
	
		return "Query{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", query='" + query + '\'' + '}';
	}
}
