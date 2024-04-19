package cn.ewsio.report.component.functional.ppt;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import cn.ewsio.report.constant.ShapeType;
import cn.ewsio.report.entity.Mapping;
import cn.ewsio.report.entity.Picture;
import cn.ewsio.report.entity.Text;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class FunctionComponent implements Closeable {

	private static final Logger LOG = LogManager.getLogger();

	private String templateFile;
	private XMLSlideShow pptx;

	public FunctionComponent(String templateFile) {
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

	public XSLFSlide handler(String json, Mapping... mappings) {
		XSLFSlide xslfSlide = null;
		if (null != mappings && mappings.length > 0) {
			Map<String, Mapping> mappingMap = new HashMap<>();
			for (Mapping m : mappings) {
				mappingMap.put(m.getShapeName(), m);
			}
			xslfSlide = handler(mappingMap, json);
		}

		return xslfSlide;
	}

	public XSLFSlide handler(Map<String, Mapping> mappingMap, String json) {
		XSLFSlide xslfSlide = null;
		JSONObject jsonObject = JSONUtil.parseObj(json);
		xslfSlide = pptx.getSlides().get(0);
		List<XSLFShape> shapes = xslfSlide.getShapes();
		Map<String, XSLFShape> picShapeMap = new HashMap<>();

		for (XSLFShape xslfShape : shapes) {
			String shapeName = xslfShape.getShapeName();
			if (mappingMap.containsKey(shapeName)) {
				Mapping mapping = mappingMap.get(shapeName);
				if (mapping.getType().equals(ShapeType.TEXT)) {
					Map<String, Text> textMappingMap = mapping.getTextMap();
					text(xslfShape, textMappingMap, jsonObject);
				} else if (mapping.getType().equals(ShapeType.PICTURE)) {
					picShapeMap.put(shapeName, xslfShape);
				} else if (mapping.getType().equals(ShapeType.TABLE)) {
					// TODO TABLE
				} else if (mapping.getType().equals(ShapeType.CHART)) {
					// TODO CHART
				}
			}
		}

		// picture
		for (Entry<String, XSLFShape> e : picShapeMap.entrySet()) {
			Mapping mapping = mappingMap.get(e.getKey());
			Picture picture = mapping.getPicture();
			String jsonPath = picture.getJsonPath();
			String jsonVal = jsonObject.getByPath(jsonPath, String.class);
			try {
				picture(xslfSlide, e.getValue(), jsonVal, picture.getScaleType());
			} catch (IOException e1) {
				LOG.error("load picture error, fileUrl:" + jsonVal, e);
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

	private void picture(XSLFSlide xslfSlide, XSLFShape xslfShape, String filePath, int scaleType) throws IOException {
		if (null != filePath && !filePath.isBlank()) {
			Rectangle2D rectangle2d = xslfShape.getAnchor();

			BufferedImage bufferedImage = ImageIO.read(URLUtil.getStream(URI.create(filePath).toURL()));
			byte[] bs = ImgUtil.toBytes(bufferedImage, "png");
			int imgWidth = bufferedImage.getWidth();
			int imgHeight = bufferedImage.getHeight();
			double xslfShapeWidth = rectangle2d.getWidth();
			double xslfShapeHeight = rectangle2d.getHeight();

			double width = xslfShapeWidth;
			double height = xslfShapeHeight;
			Double scale = imgWidth * 1.0 / imgHeight;

			if (scaleType == 0) {

			} else if (scaleType == 1) {
				if (xslfShapeWidth / xslfShapeHeight <= imgWidth / imgHeight) {
					height = xslfShapeWidth / scale;
				} else {
					width = xslfShapeHeight * scale;
				}
			} else if (scaleType == 2) {
				width = imgWidth;
				height = imgHeight;
			}

			XSLFPictureData xslfPictureData = pptx.addPicture(bs, PictureType.PNG);
			XSLFPictureShape xslfPictureShape = xslfSlide.createPicture(xslfPictureData);
			rectangle2d.setRect(rectangle2d.getX(), rectangle2d.getY(), width, height);
			xslfPictureShape.setAnchor(rectangle2d);
			xslfSlide.removeShape(xslfShape);
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
