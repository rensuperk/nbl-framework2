package cn.bidlink.nbl.framework.dao.saas;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import cn.bidlink.framework.core.annotation.Dao;
import cn.bidlink.framework.core.utils.FastUUIDGenerator;
import cn.bidlink.nbl.user.UserContext;

@Aspect
public class DaoDefaultValueInterceptor {

	@Around(value = "execution(* *cn.bidlink..dao..impl.*DaoImpl.* (..))", argNames = "pjp")
	public Object doException(ProceedingJoinPoint pjp) throws Throwable {
		Object target = pjp.getTarget();
		Dao dao = target.getClass().getAnnotation(Dao.class);
		if(dao != null){
			Object[] args = pjp.getArgs();
			MethodSignature signature = (MethodSignature) pjp.getSignature();
			String methodName = signature.getMethod().getName();
			if(methodName.equals("findByCondition")){
				setSelectDefaultValue(args[0]);
			}else if(methodName.equals("getTotalCount")){
				setSelectDefaultValue(args[0]);
			}else if(methodName.equals("save")){
				setSaveDefaultValue(args[0]);
			}else if(methodName.equals("update")){
				setUpdateDefaultValue(args[0]);
			}else if(methodName.equals("insertBatchExp")){
				for(Object model : (List<?>) args[1]){
					setSaveDefaultValue(model);
				}
			}else if(methodName.equals("updateBatchExp")){
				for(Object model : (List<?>) args[1]){
					setUpdateDefaultValue(model);
				}
			}
		}
		return pjp.proceed();
	}
	
	private static final String ID_FIELD_NAME = "id";
	private static final String CREATE_USER_ID_FIELD_NAME = "createUserId";
	private static final String ORG_CODE_FIELD_NAME = "orgCode";
	private static final String CREATE_TIME_FIELD_NAME = "createTime";
	private static final String IS_TEST_FIELD_NAME = "isTest";
	
	/**
	 * 默认设置model.id为UUID
	 * @see cn.bidlink.framework.dao.ibatis.MyBatisBaseDao.save()
	 * */
	private void setModelId(Object model){
		Field field = FieldUtils.getDeclaredField(model.getClass(), ID_FIELD_NAME, true);
		if(field != null){
			try {
				FieldUtils.writeDeclaredField(model, ID_FIELD_NAME, FastUUIDGenerator.genUUID(), true);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 默认设置model.createUserId为当前登陆用户ID
	 * @see cn.bidlink.framework.dao.ibatis.MyBatisBaseDao.save()
	 * */
	private void setModelCreateUserId(Object model){
		Field field = FieldUtils.getDeclaredField(model.getClass(), CREATE_USER_ID_FIELD_NAME, true);
		if(field != null){
			try {
				Object value = FieldUtils.readDeclaredField(model, CREATE_USER_ID_FIELD_NAME, true);
				if(value == null){
					if(UserContext.getCurrentUser() != null){
						FieldUtils.writeDeclaredField(model, CREATE_USER_ID_FIELD_NAME, UserContext.getCurrentUser().getId(), true);
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 默认设置model.orgCode为当前登陆用户部门编号
	 * @see cn.bidlink.framework.dao.ibatis.MyBatisBaseDao.save()
	 * */
	private void setModelOrgCode(Object model){
		Field field = FieldUtils.getDeclaredField(model.getClass(), ORG_CODE_FIELD_NAME, true);
		if(field != null){
			try {
				Object value = FieldUtils.readDeclaredField(model, ORG_CODE_FIELD_NAME, true);
				if(value == null){
					if(UserContext.getCurrentUser() != null){
						FieldUtils.writeDeclaredField(model, ORG_CODE_FIELD_NAME, UserContext.getCurrentUser().getOrgCode(), true);
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 默认设置model.createTime为当前时间
	 * @see cn.bidlink.framework.dao.ibatis.MyBatisBaseDao.save()
	 * */
	private void setModelCreateTime(Object model){
		Field field = FieldUtils.getDeclaredField(model.getClass(), CREATE_TIME_FIELD_NAME, true);
		if(field != null){
			try {
				FieldUtils.writeDeclaredField(model, CREATE_TIME_FIELD_NAME, new Date(), true);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 默认设置model.isTest为当前用户的isTest
	 * @see cn.bidlink.framework.dao.ibatis.MyBatisBaseDao.save()
	 * */
	private void setModelIsTest(Object model){
		Field field = FieldUtils.getDeclaredField(model.getClass(), IS_TEST_FIELD_NAME, true);
		if(field != null){
			try {
				if(UserContext.getCurrentUser() != null){
					FieldUtils.writeDeclaredField(model, IS_TEST_FIELD_NAME, new Boolean(1 == UserContext.getCurrentUser().getIsTest()), true);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setSaveDefaultValue(Object model){
		setModelId(model);
		setModelCreateUserId(model);
		setModelOrgCode(model);
		setModelCreateTime(model);
		setModelIsTest(model);
		setModelSystemStatus(model);
	}
	
	private static final String UPDATE_USER_ID_FIELD_NAME = "updateUserId";
	private static final String UPDATE_TIME_FIELD_NAME = "updateTime";
	
	/**
	 * 默认设置model.updateTime为当前时间
	 * @see cn.bidlink.framework.dao.ibatis.MyBatisBaseDao.update()
	 * */
	private void setModelUpdateUserId(Object model){
		Field field = FieldUtils.getDeclaredField(model.getClass(), UPDATE_USER_ID_FIELD_NAME, true);
		if(field != null){
			try {
				Object value = FieldUtils.readDeclaredField(model, UPDATE_USER_ID_FIELD_NAME, true);
				if(value == null){
					if(UserContext.getCurrentUser() != null){
						FieldUtils.writeDeclaredField(model, UPDATE_USER_ID_FIELD_NAME, UserContext.getCurrentUser().getId(), true);
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 默认设置model.updateTime为当前时间
	 * @see cn.bidlink.framework.dao.ibatis.MyBatisBaseDao.update()
	 * */
	private void setModelUpdateTime(Object model){
		Field field = FieldUtils.getDeclaredField(model.getClass(), UPDATE_TIME_FIELD_NAME, true);
		if(field != null){
			try {
				FieldUtils.writeDeclaredField(model, UPDATE_TIME_FIELD_NAME, new Date(), true);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setUpdateDefaultValue(Object model){
		setModelUpdateUserId(model);
		setModelUpdateTime(model);
	}
	
	private static final String SYSTEM_STATUS_FIELD_NAME = "systemStatus";
	
	/**
	 * 默认设置model.systemStatus为1
	 * */
	private void setModelSystemStatus(Object model){
		Field field = FieldUtils.getDeclaredField(model.getClass(), SYSTEM_STATUS_FIELD_NAME, true);
		if(field != null){
			try {
				if (FieldUtils.readField(field, model) == null) {
					FieldUtils.writeDeclaredField(model, SYSTEM_STATUS_FIELD_NAME, 1, true);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setSelectDefaultValue(Object model){
		setModelIsTest(model);
		setModelSystemStatus(model);
	}
}