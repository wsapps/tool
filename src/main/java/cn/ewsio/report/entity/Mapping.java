package cn.ewsio.report.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ewsio.report.constant.ShapeType;

public class Mapping {

	private String shapeName;

	private ShapeType type;

	private List<Text> texts;

	private Picture picture;

	private Chart chart;

	// private Table table;

	private Map<String, Text> textMap;
	
	public Mapping() {
		super();
	}

	public Mapping(String shapeName, Text... texts) {
		super();
		this.shapeName = shapeName;
		List<Text> list = new ArrayList<>();
		for (Text text : texts) {
			list.add(text);
		}
		
		setTexts(list);
		this.type = ShapeType.TEXT;
	}

	public String getShapeName() {
		return shapeName;
	}

	public void setShapeName(String shapeName) {
		this.shapeName = shapeName;
	}

	public List<Text> getTexts() {
		return texts;
	}

	public void setTexts(List<Text> texts) {
		this.texts = texts;

		textMap = new HashMap<>();
		for (Text text : texts) {
			textMap.put(text.getKey(), text);
		}
	}

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public Chart getChart() {
		return chart;
	}

	public void setChart(Chart chart) {
		this.chart = chart;
	}

	public ShapeType getType() {
		return type;
	}

	public void setType(ShapeType type) {
		this.type = type;
	}

	public Map<String, Text> getTextMap() {
		return textMap;
	}

}
