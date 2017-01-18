package cn.bidlink.nbl.framework.beetl.tags;

import java.io.IOException;

import org.beetl.core.GeneralVarTagBinding;

public class FormTag extends GeneralVarTagBinding {
	
	@Override
	public void render() {
		try {
			System.out.println(getAttributeValue("name"));
			ctx.byteWriter.writeString("1");
			doBodyRender();
			ctx.byteWriter.writeString("1");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}