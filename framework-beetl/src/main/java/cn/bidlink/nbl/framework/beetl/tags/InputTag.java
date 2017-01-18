package cn.bidlink.nbl.framework.beetl.tags;

import java.io.IOException;

import cn.bidlink.nbl.framework.beetl.BeetlTag;

public class InputTag extends BeetlTag {
	
	private String name;

	@Override
	public void render() {
		
		renderTemplate("tag.input.html");
	}
}