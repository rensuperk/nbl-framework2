//package cn.bidlink.nbl.framework.auth.config;
//
//import java.io.InputStream;
//import java.util.Properties;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//
//@Component(value="authConfigManager")
//public class AuthConfigManager implements InitializingBean{
//	private final static Logger LOGGER = LoggerFactory.getLogger(AuthConfigManager.class); 
//	private volatile Properties authProperties ;
//	@Autowired
//	private AuthConfig authConfig ; 
//	
//	public String getCasServiceURl(){
//		return authProperties.getProperty("auth.cas.server.url");
//	}
//	public String getCasClientLoginURl(){
//		return getAuthConfig().getClientUrl()+"/auth/cas/login";
//	}
//	public String getCasClientLogoutURl(){
//		return getAuthConfig().getClientUrl()+"/auth/cas/logout";
//	}
//	/**
//	 * 招标机构登录页
//	 * @return
//	 */
////	public String getLoginPageURL(){
////		return getAuthProperties().getProperty("auth.tender.login.page.url");
////	}
//	/**
//	 * 默认登录页
//	 * @return
//	 */
//	public String getCommonLoginPageURL(){
//		return getAuthProperties().getProperty("auth.common.login.page.url");
//	}
//	public String getRedisConfig(){
//		return getAuthProperties().getProperty("auth.redis.connection.config");
//	}	
//	public String getIndexPageURL(){
//		return getAuthProperties().getProperty("auth.index.page.url");
//	}
//	public String getProductUpgradeUrl(){
//		return getAuthProperties().getProperty("auth.product.upgrade.url");
//	}
//	public String getRedisHost(){
//		String[] redisConfig= getRedisConfig().split(":");
//		try {
//			return redisConfig[0];
//		} catch (Exception e) {
//			return "";
//		}
//	}
//	public String getRedisPost(){
//		String[] redisConfig= getRedisConfig().split(":");
//		try {
//			return redisConfig[1];
//		} catch (Exception e) {
//			return "";
//		}
//	}
//	public String getRedisPassword(){
//		String[] redisConfig= getRedisConfig().split(":");
//		try {
//			return redisConfig[2];
//		} catch (Exception e) {
//			return "";
//		}
//	}
//	
//	public AuthConfig getAuthConfig() {
//		return authConfig;
//	} 
//	public void setAuthConfig(AuthConfig authConfig) {
//		this.authConfig = authConfig;
//	}
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		try {
//			String zkPath = StringUtils.trim( getAuthConfig().getZkPath() );
//			InputStream propInputStream = null ;
//			if( StringUtils.equals(zkPath, "/bidconf/demo/nbl/framework-auth/v1/prop/auth-prop.properties")){
//				propInputStream = AuthConfigManager.class.getResourceAsStream("/nbl-framework-auth/dev.properties");
//				
//			}else if( StringUtils.equals(zkPath, "/bidconf/test/nbl/framework-auth/v1/prop/auth-prop.properties") ){
//				propInputStream = AuthConfigManager.class.getResourceAsStream("/nbl-framework-auth/test.properties");
//				
//			}else if( StringUtils.equals(zkPath, "/bidconf/product/nbl/framework-user/v1/prop/auth-prop.properties") ){
//				propInputStream = AuthConfigManager.class.getResourceAsStream("/nbl-framework-auth/product.properties");
//			}else{
//				throw new RuntimeException("zkpath error");
//			} 
//			authProperties = new Properties();
//			authProperties.load( propInputStream );
//			propInputStream.close();
//		} catch (Exception e) {
//			LOGGER.error("read zookeeper config error" , e );
//		}
//	}
//	public Properties getAuthProperties() {
//		return authProperties;
//	}
//}
