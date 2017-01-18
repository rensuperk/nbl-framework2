package cn.bidlink.nbl.framework.dubbo.filter;

import cn.bidlink.framework.core.utils.DateUtils;
import cn.bidlink.framework.core.utils.json.jackson2.JsonUtils;
import cn.bidlink.framework.dao.adapter.CommonColumns;
import cn.bidlink.framework.dao.adapter.DbColumn;
import cn.bidlink.framework.dao.adapter.DsAdapterContext;
import cn.bidlink.framework.dao.adapter.DsExpendColumnContext;
import cn.bidlink.framework.dao.commons.DsConstants;
import cn.bidlink.nbl.framework.core.constants.NblConstants;
import cn.bidlink.nbl.framework.core.context.TenantContext;
import cn.bidlink.nbl.framework.dubbo.filter.DubboExcludeTenantId;
import cn.bidlink.nbl.user.UserContext;
import cn.bidlink.nbl.user.model.User;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//@Activate(group = {Constants.CONSUMER, Constants.PROVIDER})
public class TenantIdFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TenantIdFilter.class);
    private static List<String> excludeServiceList = new ArrayList<>();

    static {
        excludeServiceList.add("cn.bidlink.nbl.user.service.OrganizationRemoteService");
        excludeServiceList.add("cn.bidlink.nbl.authority.service.MidRoleAuthorityRemoteService");
        excludeServiceList.add("cn.bidlink.nbl.authority.service.MidRoleOrganizationRemoteService");
        excludeServiceList.add("cn.bidlink.nbl.authority.service.MidUserRoleRemoteService");
        excludeServiceList.add("cn.bidlink.nbl.authority.service.AuthorityRemoteService");
        excludeServiceList.add("cn.bidlink.nbl.user.service.UserRemoteService");
        excludeServiceList.add("cn.bidlink.nbl.authority.service.ResourceRemoteService");
        excludeServiceList.add("cn.bidlink.nbl.authority.service.MidAuthorityResourceRemoteService");
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {        String fullName = invoker.getInterface().getName();
        if (excludeServiceList.contains(fullName)) {
            return invoker.invoke(invocation);
        }
        DubboExcludeTenantId dubboExcludeTenantId = AnnotationUtils.findAnnotation(invoker.getInterface(), DubboExcludeTenantId.class);
        if (dubboExcludeTenantId == null) {
            Method method = ReflectionUtils.findMethod(invoker.getInterface(), invocation.getMethodName(), invocation.getParameterTypes());
            dubboExcludeTenantId = AnnotationUtils.findAnnotation(method, DubboExcludeTenantId.class);
        }
        if (RpcContext.getContext().isProviderSide()) {
            //只在服务端设置数据路由
            DsAdapterContext.set(NblConstants.dsRouterKey + "_" + DsConstants.getAppNo());
        }
        if (dubboExcludeTenantId != null) {
            return invoker.invoke(invocation);
        }
        // 服务端，获取租户id
        if (RpcContext.getContext().isProviderSide()) {
            User user = JsonUtils.json2Object(RpcContext.getContext().getAttachment("userJson"), User.class, DateUtils.DATE_HH_MM_SS);
            if (user != null) {
                UserContext.setCurrentUser(user);
                String tenantId = RpcContext.getContext().getAttachment("tenantId");
                if (user.getType() != 13) {
                /* 当user不是供应商的时候才设置租户id */
                    DbColumn dc = new DbColumn().setClassType(String.class).setColumnName("TENANT_ID").setColumnValue(tenantId);
                    DsExpendColumnContext.set(new CommonColumns().setDefaultColumn(dc));
                    TenantContext.setTenantId(user.getTenantId());
                }
                //logger.debug("service : {} method : {} params : {} get tenantId : {}", invoker.getInterface(), invocation.getMethodName(), invocation.getParameterTypes().toString(), tenantId);

                /* 对于SaasBaseDao的支持 */
                TenantContext.setIsTest(user.getIsTest() == 1);
            }

            Result invokeResult = invoker.invoke(invocation);
            /* 服务端调用完成后, 清空线程变量 */
            DsExpendColumnContext.remove();

            return invokeResult;
        } else if (RpcContext.getContext().isConsumerSide()) {
            // 消费端，存入租户id
            User user = UserContext.getCurrentUser();
            String userJson = JsonUtils.toJson(user, null, null, DateUtils.DATE_HH_MM_SS);
            RpcContext.getContext().setAttachment("userJson", userJson);
            String tenantId = UserContext.getCurrentUser().getTenantId();
            RpcContext.getContext().setAttachment("tenantId", tenantId);
            //logger.debug("service : {} method : {} params : {} put tenantId : {}", invoker.getInterface(), invocation.getMethodName(), invocation.getParameterTypes().toString(), tenantId);
        }
        return invoker.invoke(invocation);
    }
}