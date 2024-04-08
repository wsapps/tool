package cn.ewsio.report.component.business;

import java.util.Map;

import org.apache.poi.xslf.usermodel.XSLFSlide;

import cn.ewsio.report.entity.Mapping;

public interface Bussiness {

	XSLFSlide handler(String templateFile, Map<String,Mapping> mappingMap, String json);
}
