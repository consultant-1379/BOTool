package jsonPojo.dataProviders.Tables;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import jsonPojo.dataProviders.section.FeedExpression;
import jsonPojo.dataProviders.section.Section;

public class PageBody {

    private boolean isTaable;

    private boolean isChart;

    @JacksonXmlProperty(localName = "VTABLE")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Vtable> vtables;

    @JacksonXmlProperty(localName = "SECTION")
    private Section section;

    @JacksonXmlProperty(localName = "bId", isAttribute = true)
    private String id;


    @Override
    public String toString() {
        return "PageBody{" +
                "vtables=" + getVtables() +
                ", section=" + section +
                ", id='" + id + '\'' +
                '}';
    }

    public List<Vtable> getVtables() {
        return vtables;
    }

    public void setVtables(List<Vtable> vtables) {
        isTaable = true;
        
        if(this.vtables == null) {
            this.vtables = new ArrayList<Vtable>(vtables.size());
        }
        this.vtables.addAll(vtables);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        isChart = true;
        this.section = section;
    }

    public boolean isTaable() {
        return isTaable;
    }

    public boolean isChart() {
        return isChart;
    }
}
