package cn.bidlink.nbl.framework.auth.utils;
/**
 * 用于根据此类的静态变量定义，获取相关值
 * @version : Ver 1.0
 * @author	: <a href="mailto:yijundai@ebnew.com">yijundai</a>
 * @date	: 2015年9月18日 上午9:41:28 
 */
public class AuthContent {
	/**
	 * 获取 Cas TGT 令牌
	 */
	public static final String TGT_KEY = AuthContent.class.getName()+"_tgt";
	/**
	 * 获取登录用户
	 * {@link cn.bind.framework.auth.authc.CasLoginHandler}
	 */
	public static final String USER_LOGIN = AuthContent.class.getName()+"_user_login";
	/**
	 * 获取登录用户所对应主用户
	 *  {@link cn.bind.framework.auth.authc.CasLoginHandler}
	 */
	public static final String USER_TENANT = AuthContent.class.getName()+"_user_tenant";
	/**
	 * 获取审批发起人，针对 审批请求
	 *  {@link cn.bind.framework.auth.authc.CasLoginHandler}
	 */
	public static final String USER_APPLY = AuthContent.class.getName()+"_applyUserId";
	/**
	 * 用于存储当Ajax请求时，遇到未登录时，保存当前ajax所属页面
	 * {@link cn.bind.framework.auth.authc.CasLoginHandler}
	 */
	public static final String PRE_REQUEST_REFERER = "pre_request_referer";
	
	public static final String AUTH_CONDITION =AuthContent.class.getName()+"AUTH_CONDITION_SQL"; 
	 
}
