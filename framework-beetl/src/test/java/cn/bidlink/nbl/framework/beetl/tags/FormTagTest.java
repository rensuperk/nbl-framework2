package cn.bidlink.nbl.framework.beetl.tags;

import java.io.IOException;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.junit.Before;
import org.junit.Test;

public class FormTagTest {
	
	private GroupTemplate gt;
	
	@Before
	public void aa(){
		StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
		try {
			Configuration cfg = Configuration.defaultConfiguration();
			this.gt = new GroupTemplate(resourceLoader, cfg);
			this.gt.registerTag("form", FormTag.class);
			this.gt.registerTag("input", InputTag.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFormTag(){
		Template t = gt.getTemplate("<#form name=\"A\"><#input></#input></#form>");
		t.binding("name", "beetl");
		String str = t.render();
		System.out.println(str);
	}
}