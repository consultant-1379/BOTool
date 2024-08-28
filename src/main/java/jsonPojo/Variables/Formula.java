
package jsonPojo.Variables;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Formula {

	private String dataType;

	private String qualification;

	private String id;

	private String name;

	private String formulaLanguageId;

	private String definition;

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

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

	public String getFormulaLanguageId() {
		return formulaLanguageId;
	}

	public void setFormulaLanguageId(String formulaLanguageId) {
		this.formulaLanguageId = formulaLanguageId;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getFormattedString(){
        String def = definition.substring(1,definition.length());
        def = def.replaceAll("\n","");
        return dataType + "," + id + "," + name + "," + def;
    }
	
	@Override
	public String toString() {

		return "Formula{" + "dataType='" + dataType + '\'' + ", qualification='" + qualification + '\'' + ", id='" + id
				+ '\'' + ", name='" + name + '\'' + ", formulaLanguageId='" + formulaLanguageId + '\''
				+ ", definition='" + definition + '\'' + '}';
	}

}

