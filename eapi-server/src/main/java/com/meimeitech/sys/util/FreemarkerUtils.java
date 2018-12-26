package com.meimeitech.sys.util;

import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerUtils {

	private static final Logger LOG = LoggerFactory.getLogger(FreemarkerUtils.class);

	public static String renderString(String templateString, Object model) {
		try {
			Template template = new Template("default", new StringReader(templateString),
					new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
			return renderTemplate(template, model);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return null;
	}

	public static String renderTemplate(Template template, Object model) {
		try {
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return null;
	}

}
