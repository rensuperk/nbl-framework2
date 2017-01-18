package cn.bidlink.nbl.framework.springmvc;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName: ResponseUtils
 * @Description: 响应工作 类别
 * @author <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @date 2011-11-15 下午05:50:43
 * 
 */
public class RespUtils {
	
	private static Logger logger = LoggerFactory.getLogger(RespUtils.class);

	/**
	 * 
	 * @Title: responseJson
	 * @Description: JSON 格式 响应
	 * @param @param response
	 * @param @param res 响应字符串
	 * @return void 返回类型
	 * @throws
	 */
	public static void responseJson(HttpServletResponse response, String res) {
		PrintWriter out = null;
		try {
			response.setContentType("application/json;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.write(res);
			out.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}

	}

	/**
	 * 
	 * @Title: responseScript
	 * @Description: JSON 格式 响应
	 * @param @param response
	 * @param @param res 响应字符串
	 * @return void 返回类型
	 * @throws
	 */
	public static void responseScript(HttpServletResponse response, String res) {
		PrintWriter out = null;
		try {
			response.setContentType("text/javascript;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.write(res);
			out.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 
	 * @Title: responseHtml
	 * @Description: text/html 格式 响应
	 * @param @param response
	 * @param @param res 响应字符串
	 * @return void 返回类型
	 * @throws
	 */
	public static void responseHtml(HttpServletResponse response, String res) {
		PrintWriter out = null;
		try {
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.write(res);
			out.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}