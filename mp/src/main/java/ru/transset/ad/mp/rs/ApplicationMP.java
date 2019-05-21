/**
 * АО Транссеть
 * 
 * http://transset.ru
 */
package ru.transset.ad.mp.rs;

import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ws.rs.core.Application;

import ru.funsys.avalanche.Avalanche;

/**
 * <p>Класс регистрации реализованных REST сервисов в сервлете Jersey (JAX-RS 2.0)
 * <b>{@code org.glassfish.jersey.servlet.ServletContainer}</b>, см web.xml, параметр
 * <b>{@code javax.ws.rs.Application}</b>. Пример:</p>
 * <pre>
 * &lt;servlet>
 *   &lt;servlet-name>jersey-serlvet&lt;/servlet-name>
 *   &lt;servlet-class>org.glassfish.jersey.servlet.ServletContainer&lt;/servlet-class>
 *     &lt;init-param>
 *       &lt;param-name>javax.ws.rs.Application&lt;/param-name>
 *       &lt;param-value>ru.transset.ad.mp.rs.ApplicationMP&lt;/param-value>
 *     &lt;/init-param>
 *     &lt;init-param>
 *       &lt;param-name>jersey.config.server.provider.packages&lt;/param-name>
 *       &lt;param-value>ru.transset.app.mp.service&lt;/param-value>
 *     &lt;/init-param>
 *   &lt;load-on-startup>2&lt;/load-on-startup>
 * &lt;/servlet>
 * </pre>
 * <p>Все классы реализованных сервисов регистрируются как "singleton" - создаются в
 * одном экземпляре. Каждый экземпляр класса обрабатывают множество вызовы в
 * параллельных потоках. Это накладывает ограничения использования полей класса
 * реализованного сервиса. Значение поля, установленное в одном потоке, может быть
 * переопределено значением, устанавливаемом в другом потоке.</p>
 * <p>
 * Для определения пакета реализаций классов REST сервисов, экземпляры которых создаются для
 * каждого вызова,  используется параметр <b>{@code jersey.config.server.provider.packages}</b>.       
 * 
 * @author Валерий Лиховских
 *
 */
public class ApplicationMP extends Application {

    private Set<Object> singletons;
    
	/**
	 * В конструкторе класса получается ссылка на экземпляр класса Avalanche по JNDI имени,
	 * которое определяется в конфигурации контекста сервера. Пример:
	 * <pre>
	 * &lt;Context docBase="mp" path="/mp" reloadable="true" source="org.eclipse.jst.jee.server:mp">
	 *   &lt;Resource auth="Container" factory="org.apache.naming.factory.BeanFactory"
	 *             name="avalanche/mp"
	 *             type="ru.funsys.avalanche.Avalanche"/>
	 * &lt;/Context>
	 * </pre> 
	 * Для WEB сервера Jakarta-Tomcat используется имя контекста JNDI <b>{@code java:comp/env}</b>,
	 * полное JNDI имя - <b>{@code java:comp/env/avalanche/mp}</b> 
	 * 
	 * @throws Exception
	 */
	public ApplicationMP() throws Exception {
		// Получить корневой контекст JNDI
		Context initContext = new InitialContext();
		// Получить ссылку на класс Avalanche по имени JNDI
		Avalanche avalanche = (Avalanche) initContext.lookup(Avalanche.getJndiContext() + '/' + "avalanche/mp");
		// Получить список классов, реализованных REST сервисов
		singletons = avalanche.getSingletons();
	}

	@Override
	public Set<Object> getSingletons() {
    		if (singletons == null) return super.getSingletons();
		else return singletons;
	}
    
}
