package cn.bidlink.nbl.framework.dubbo.filter;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface DubboExcludeTenantId {
}
