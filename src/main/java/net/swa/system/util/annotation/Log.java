package net.swa.system.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 日志注解
 * 参数：description(可选)，指定表中的字段名，如果没有指定该属性，将使用被注解的属性名称为字段名
 * 例如：<br>
 * <code>
 * 	@Column(name="user_name")<br>
 * 	private Sting userName
 * </code>
 * 
 * @author Chen Xiaowei[xiaowei_8868@126.com]
 * @since JDK 1.5
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
public @interface Log {
	/**
	 * 描述方法的业务意义
	 * @return
	 */
	public abstract String description();
}
