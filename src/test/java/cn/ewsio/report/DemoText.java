package cn.ewsio.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.sl.usermodel.MasterSheet;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;

import cn.ewsio.report.component.business.BussinessComponent;
import cn.ewsio.report.entity.Mapping;
import cn.ewsio.report.entity.Text;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;

public class DemoText {
	public static void main(String[] args) throws Exception {
		String templateFile = "C:\\Users\\E\\Desktop\\template\\homePage.pptx";
		String rootPath = "C:/Users/E/Desktop/template2/";
		BussinessComponent bc = new BussinessComponent(templateFile);

		Map<String, Mapping> mappingMap = new HashMap<>();
		mappingMap.put("report_date", new Mapping("report_date", new Text("report_date", "report_date", "")));
		mappingMap.put("projectName", new Mapping("projectName", new Text("projectName", "projectName", ""), new Text("company", "company", "")));

		String json = "{'report_date':'2024年3月28日','projectName':'测试项目ABCDEFG','company':'测试公司XYZ'}";

		XSLFSlide xslfSlide = bc.handler(mappingMap, json);

		XMLSlideShow ppt = new XMLSlideShow();
//		XSLFSlideLayout slideLayout = xslfSlide.getSlideLayout();

//		XSLFSlideMaster sourceMaster = xslfSlide.getSlideMaster();
//		XSLFSlideLayout sourceMasterSheet = xslfSlide.getMasterSheet();
		XSLFSlideLayout sourceSlideLayout = xslfSlide.getSlideLayout();
//		newMasterSheet.importContent(sourceMasterSheet);
//		
		XSLFSlide newXSLFSlide = ppt.createSlide(sourceSlideLayout);
		newXSLFSlide.importContent(xslfSlide);
		ppt.setPageSize(bc.getXMLSlideShow().getPageSize());
//		OPCPackage opcPackage = ppt.getPackage();
//		ArrayList<PackagePart> parts = opcPackage.getParts();
//		for (PackagePart part : parts) {
//			if (part.isRelationshipPart()) {
//				continue;
//			}
//
//			PackagePartName ppn = part.getPartName();
//
//			System.out.println(ppn.getName());
//			try {
//				FileUtil.writeFromStream(part.getInputStream(), rootPath + ppn.getName());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}

		ppt.writeEx(FileUtil.getOutputStream("C:\\Users\\E\\Desktop\\demo.pptx"));
		ppt.close();
		bc.close();
	}
}
