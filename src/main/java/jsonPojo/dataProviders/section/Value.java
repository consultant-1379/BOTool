package jsonPojo.dataProviders.section;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Value {

    @JacksonXmlProperty(localName = "FEED_EXPR")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FeedExpression> feedExpression;


    @Override
    public String toString() {
        return "Category{" +
                "feedExpression=" + getFeedExpression() +
                '}';
    }

    public List<FeedExpression> getFeedExpression() {
        return feedExpression;
    }

    public void setFeedExpression(List<FeedExpression> feedExpression) {
    	
    	if(this.feedExpression == null) {
            this.feedExpression = new ArrayList<FeedExpression>(feedExpression.size());
        }
        this.feedExpression.addAll(feedExpression);
    }

}
