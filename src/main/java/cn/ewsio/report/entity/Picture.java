package cn.ewsio.report.entity;

public class Picture {
	private String key;
	private String jsonPath;
	
	/**
	 * 0:拉伸
	 * 1：等比例
	 * 2：原始
	 */
	private Integer scaleType;

	public Picture(String jsonPath) {
		super();
		this.jsonPath = jsonPath;
		this.scaleType = 0;
	}

	public Picture(String jsonPath, Integer scaleType) {
		super();
		this.jsonPath = jsonPath;
		this.scaleType = scaleType;
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

	public Integer getScaleType() {
		return scaleType;
	}

	public void setScaleType(Integer scaleType) {
		this.scaleType = scaleType;
	}

}
