package jsonPojo.dataProviders.section;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class FeedExpression {

	@JacksonXmlProperty(localName = "CONTENT")
	private String content;

	public String getContent() {
		return content;
	}

	@Override
	public String toString() {
		return content;
	}

	// testing
	public void setContent(String content) {
		this.content = content;
	}
}
