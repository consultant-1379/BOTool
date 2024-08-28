package jsonPojo.dataProviders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataProvidersMapper {

	@Override
	public String toString() {
		return "DataProvidersMapper{" + "dataProvider=" + dataProvider + '}';
	}

	@JsonProperty("dataprovider")
	private List<DataProviders> dataProvider = new ArrayList<DataProviders>();

	public List<DataProviders> getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(List<DataProviders> dataProvider) {
		this.dataProvider = dataProvider;
	}
}