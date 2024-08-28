package jsonPojo.Query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class QueryMapper {

    @JsonProperty("dataprovider") //you are saying json mapper , get all values for dataprovider n map to query class
    private Query dpQuery;


    public Query getDpQuery() {
        return dpQuery;
    }

    @Override
    public String toString() {
        return "QueryMapper{" +
                "dpQuery=" + dpQuery +
                '}';
    }

    public void setDpQuery(Query dpQuery) {
        this.dpQuery = dpQuery;
    }
}
