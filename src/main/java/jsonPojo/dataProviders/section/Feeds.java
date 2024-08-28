package jsonPojo.dataProviders.section;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Feeds {

    @JacksonXmlProperty(localName = "CATEGORY")
    private Category category;

    @JacksonXmlProperty(localName = "VALUE")
    private Value value;


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Feeds{" +
                "category=" + category +
                ", value=" + value +
                '}';
    }
}
