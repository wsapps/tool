package cn.ewsio.report.entity;

public class Text {

	private String key;
	private String jsonPath;
	private String defaultValue;

	public Text() {
		super();
	}

	public Text(String key, String jsonPath) {
		super();
		this.key = key;
		this.jsonPath = jsonPath;
	}
	
	public Text(String key, String jsonPath, String defaultValue) {
		super();
		this.key = key;
		this.jsonPath = jsonPath;
		this.defaultValue = defaultValue;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
