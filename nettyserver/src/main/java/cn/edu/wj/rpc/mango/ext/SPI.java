package cn.edu.wj.rpc.mango.ext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Service Provider Interface,服务提供接口
 * @author jwu
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

	String value() default ""; //服务接口名称,默认空
	
	Scope scope() default Scope.SINGLETON; //默认单例实例化
}
