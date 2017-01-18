package cn.bidlink.nbl.framework.beetl;

import org.beetl.core.GeneralVarTagBinding;
import org.beetl.core.GroupTemplate;

public class BeetlTag extends GeneralVarTagBinding {

	private GroupTemplate gt;
	
	@Override
	public void render() {
		
	}
	
	protected void renderTemplate(String path){
		gt.getTemplate(path).renderTo(ctx.byteWriter);
	}
}