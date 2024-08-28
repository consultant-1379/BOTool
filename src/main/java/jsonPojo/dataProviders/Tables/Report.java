package jsonPojo.dataProviders.Tables;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jsonPojo.dataProviders.section.Feeds;
import jsonPojo.dataProviders.section.TITLE;
import jsonPojo.dataProviders.section.Visu;
import jsonPojo.dataProviders.section.FeedExpression;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "Report")
public class Report {

	@JacksonXmlProperty(localName = "rId", isAttribute = true)
	private String id;

	@JacksonXmlProperty(localName = "name", isAttribute = true)
	private String name;

	@JacksonXmlProperty(localName = "PAGE_BODY")
	private PageBody PAGE_BODY;

	public String getFormattedString(String reportName, String viewMode, String checkSingleOrMultiplePage, String reportHeading,
			String pageHeaderElementsString, String tableHeaderList) {

		String FinalString = String.join(",", reportName.replaceAll(",", "#").toString(), id, name.replaceAll(",", "#"), tableOrChart(), viewMode,
				checkSingleOrMultiplePage, reportHeading.replaceAll(",", "#"), pageHeaderElementsString.replaceAll(",", "#"),
				tableHeaderList, getRowWithBoth());

		return FinalString;
	}

	private String getRowWithBoth() {
		if (PAGE_BODY.isTaable()) {
			List<Vtable> tableList = PAGE_BODY.getVtables();
			List<String> tableBodyValues = new ArrayList<>();

			for (Vtable table : tableList) {
				final List<RowGroup> allRows = table.getRowGroups();
				for (RowGroup row : allRows) {
					if (row.getType().equalsIgnoreCase("body")) {
						List<TdCell> tdCells = row.getTr().getTdcell();
						for (int i = 0; i < tdCells.size(); i++) {
							String cleanContent = tdCells.get(i).getContent().replaceAll("=", "").replaceAll("\\[", "'")
									.replaceAll("\\]", "'").replaceAll(",", "#").replaceAll("\"", "");
							tableBodyValues.add(cleanContent);
						}
					}
				}
			}

			String tablebody = "";
			String tablebodyfinal = "";
			for (String s : tableBodyValues) {
				tablebody = tablebody + s + "\n";
			}
			tablebodyfinal = "\"" + tablebody + "\"";

			return tablebodyfinal;

		} else if (PAGE_BODY.isChart()) {
			List<Visu> visuals = PAGE_BODY.getSection().getSbody().getVisu();
			StringBuilder stringBuilder = new StringBuilder();

			if (visuals != null) {
				for (Visu visual : visuals) {
					final TITLE title = visual.getXyChart().getValueAxis().getTitle();
					final Feeds feeds = visual.getXyChart().getFeeds();
					ArrayList<String> KPIValuesList = new ArrayList<String>();
					List<FeedExpression> feedExpsList = feeds.getValue().getFeedExpression();

					for (FeedExpression feedExp : feedExpsList) {
						String KPIValue = feedExp.getContent().replaceAll(",", "#").replaceFirst("\\[", "'")
								.replaceAll("\\]$", "'");
						KPIValuesList.add(KPIValue);
					}

					String KPIValue1 = "";
					String KPIValue1FinalString = "";

					for (String s : KPIValuesList) {
						KPIValue1 = KPIValue1 + s + "\n";
					}
					KPIValue1FinalString = "\"" + KPIValue1 + "\"";

					String title1 = title.toString().replaceAll(",", "#");
					String title2 = feeds.getCategory().getFeedExpression().getContent().replaceAll(",", "#");

					stringBuilder.append(title1 + ",");
					stringBuilder.append(title2 + ",");
					stringBuilder.append(KPIValue1FinalString + ",");
				}
				return stringBuilder.toString().replaceAll("=", "");
			}
		}
		return "UNSUPPORTED_PAGE_TYPE";
	}

	private String tableOrChart() {
		return PAGE_BODY.isTaable() ? "Table" : "Chart";
	}

	@Override
	public String toString() {
		return "Report{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", PAGE_BODY=" + PAGE_BODY + '}';
	}

	public PageBody getPAGE_BODY() {
		return PAGE_BODY;
	}

	public void setPAGE_BODY(PageBody PAGE_BODY) {
		this.PAGE_BODY = PAGE_BODY;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
