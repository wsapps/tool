package cn.ewsio.report;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import cn.ewsio.report.component.functional.ppt.FunctionComponent;
import cn.ewsio.report.entity.Mapping;
import cn.ewsio.report.entity.Text;
import cn.hutool.core.io.FileUtil;

public class DemoText {
	public static void main(String[] args) throws Exception {
		String templateFile = "C:\\Users\\E\\Desktop\\template\\homePage.pptx";
		FunctionComponent bc = new FunctionComponent(templateFile);

		String json = "{'report_date':'2024年4月08日','projectName':'测试项目ABCDEFG','company':'测试公司XYZ'}";

		XSLFSlide xslfSlide = bc.handler(json, 
				new Mapping("report_date", new Text("report_date", "report_date", "")),
				new Mapping("projectName", new Text("projectName", "projectName", ""), new Text("company", "company", "")));

		XMLSlideShow ppt = new XMLSlideShow();
		XSLFSlide newXSLFSlide = ppt.createSlide();
		newXSLFSlide.importContent(xslfSlide);
		ppt.setPageSize(bc.getXMLSlideShow().getPageSize());

		ppt.writeEx(FileUtil.getOutputStream("C:\\Users\\E\\Desktop\\demo.pptx"));
		ppt.close();
		bc.close();
	}
}
