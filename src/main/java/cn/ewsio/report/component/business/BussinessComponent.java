package cn.ewsio.report.component.business;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import cn.ewsio.report.constant.ShapeType;
import cn.ewsio.report.entity.Mapping;
import cn.ewsio.report.entity.Text;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class BussinessComponent implements Closeable {

	private static final Logger LOG = LogManager.getLogger();
	
	private String templateFile;
	private XMLSlideShow pptx;
	
	public BussinessComponent(String templateFile) {
		super();
		this.templateFile = templateFile;
		BufferedInputStream bis = FileUtil.getInputStream(this.templateFile);
		try {
			pptx = new XMLSlideShow(bis);
		} catch (IOException e) {
			LOG.error("open ppt error, file:" + templateFile, e);
		}
	}
	
	public XMLSlideShow getXMLSlideShow() {
		return pptx;
	}

	public XSLFSlide handler(Map<String, Mapping> mappingMap, String json) {
		XSLFSlide xslfSlide = null;
		JSONObject jsonObject = JSONUtil.parseObj(json);
		xslfSlide = pptx.getSlides().get(0);
		List<XSLFShape> shapes = xslfSlide.getShapes();

		for (XSLFShape xslfShape : shapes) {
			String shapeName = xslfShape.getShapeName();
			if (mappingMap.containsKey(shapeName)) {
				Mapping mapping = mappingMap.get(shapeName);
				if (mapping.getType().equals(ShapeType.TEXT)) {
					Map<String, Text> textMappingMap = mapping.getTextMap();
					text(xslfShape, textMappingMap, jsonObject);

				} else if (mapping.getType().equals(ShapeType.PICTURE)) {

				} else if (mapping.getType().equals(ShapeType.TABLE)) {

				} else if (mapping.getType().equals(ShapeType.Chart)) {

				}
			}
		}

		return xslfSlide;
	}
	
	public void close() throws IOException {
		if (null != pptx) {
			try {
				pptx.close();
			} catch (IOException e) {
				LOG.error("open ppt close, file:" + templateFile, e);
			}
		}
	}

	private void text(XSLFShape xslfShape, Map<String, Text> textMappingMap, JSONObject jsonObject) {
		XSLFTextShape textShape = (XSLFTextShape) xslfShape;
		List<XSLFTextParagraph> xslfTextParagraphs = textShape.getTextParagraphs();

		for (XSLFTextParagraph xslfTextParagraph : xslfTextParagraphs) {
			List<XSLFTextRun> xslfTextRuns = xslfTextParagraph.getTextRuns();
			for (XSLFTextRun xslfTextRun : xslfTextRuns) {
				String txt = xslfTextRun.getRawText();
				List<String> list = ReUtil.findAllGroup0("\\{\\{\\w+}}", txt);
				for (String rawTextKey : list) {
					String key = rawTextKey.replace("{{", "").replace("}}", "");
					if (textMappingMap.containsKey(key)) {
						String jsonPath = textMappingMap.get(key).getJsonPath();
						String defaultValue = textMappingMap.get(key).getDefaultValue();
						String jsonVal = jsonObject.getByPath(jsonPath, String.class);
						String value = null == jsonVal || jsonVal.isBlank() ? defaultValue : jsonVal;
						
						txt = txt.replace(rawTextKey, value);
						xslfTextRun.setText(txt);
					}
				}
			}
		}

	}
}
