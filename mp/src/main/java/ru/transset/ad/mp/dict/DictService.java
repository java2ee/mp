/**
 * АО Транссеть
 * 
 * http://transset.ru
 */
package ru.transset.ad.mp.dict;

import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ru.funsys.avalanche.Application;
import ru.transset.ad.mp.ext.RestExceptionMapper;
import ru.transset.ad.mp.param.DateParam;

/**
 * Реализация REST сервисов - загрузки справочников приложения. В конфигурации системы у экземпляра данного
 * класса должен быть установлен атрибут <b>{@code service}</b> в значение <b>{@code true}</b>.
 * 
 * @author Валерий Лиховских
 *
 */
@Path("/dict")
public class DictService extends Application {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2855654618608034642L;

	private static final String UTF_8 = "utf8"; // NOI18N 
	
	private static final String DICT001E = "DICT001E"; // NOI18N 
	private static final String DICT002E = "DICT002E"; // NOI18N
	private static final String DICT003E = "DICT003E"; // NOI18N
	private static final String DICT004E = "DICT004E"; // NOI18N
	private static final String DICT005E = "DICT005E"; // NOI18N

	private DictAdapter dictAdapter;

	/**
	 * @param name имя справочника
	 * @param date временная метка справочника мобильного клиента, полученное в предыдущем запросе
	 * @param lang локализация справочника
	 * 
	 * @return json - справочник или сообщение об ошибке. Пример возвращаемых данных
	 * <pre>
	 * {"id":1,"name":"road","updateTime":"20181231T112554+0300",
	 *  "content":[{"description":"М4 \"Дон\"","version_id":1,"language":"ru","pos":1,"id":1},
	 *             {"description":"М3 \"Украина\"","version_id":5,"language":"ru","pos":2,"id":3},
	 *             {"description":"М11 \"Москва - Санкт-Петербург\"","version_id":3,"language":"ru","pos":3,"id":2}]}
	 * </pre>
	 * или
	 * <pre>
	 * {"message":"Ошибка формирования справочника: map.","code":"DICT001E",
	 * "cause":"[16908@likhovskikh-vv] SYS0250E При вызове метода \"dict\" возникла ошибка.\n\t
	 * Ошибка: \"ru.transset.ad.mp.api.exception.RestException - Test Error\""}
	 * </pre>
	 * 
	 *  
	 */
	@GET
	@Path("/{name}")
	@Produces( "application/json" )
	public Response dict(@PathParam("name") String name,
                             @QueryParam("updateTime") DateParam date,
                             @QueryParam("lang") @DefaultValue("ru") String lang) {
	        if (name == null) {
        		return RestExceptionMapper.newRestException(logger, DICT003E, lang);
	        }
        	Response response;
	        try {
	        	Dict dict = dictAdapter.dict(name, lang);
			if (dict == null) {
		            	response = RestExceptionMapper.newRestException(logger, DICT005E, lang, name);
			} else {
		                if (date != null && date.getDate().compareTo(dict.updateTime()) >= 0) {
                			// Справчник не изменился
		                	response = Response.notModified().type(MediaType.APPLICATION_JSON_TYPE.withCharset(UTF_8)).build();
		                } else { 
		                	// Справчник
                			response = Response.ok(dict).type(MediaType.APPLICATION_JSON_TYPE.withCharset(UTF_8)).build();
		                }
			}
	        } catch (Throwable throwable) {
        		response = RestExceptionMapper.newRestException(logger, throwable, DICT001E, lang, name);
	        }
        	return response;
	}

	/**
	 * Получить список справочников мобильного приложения
	 * 
	 * @param lang локализация возвращаемых данных 
	 * 
	 * @return список справочников мобильного приложения. Пример возвращаемых данных
	 * <pre>
	 * [{"id":18,"name":"direct_tsup","update_time":"20181017T120000+0300"},
	 *  {"id":11,"name":"surface","update_time":"20181017T120000+0300"},
	 *  {"id":2,"name":"road_operator","update_time":"20181017T120000+0300"},
	 *  {"id":7,"name":"part_of_day","update_time":"20181231T120000+0300"},
	 *  {"id":19,"name":"locality","update_time":"20190415T120000+0300"},
	 *  {"id":9,"name":"wind_direction","update_time":"20181017T120000+0300"},
	 *  {"id":14,"name":"language","update_time":"20181017T120000+0300"},
	 *  {"id":1,"name":"road","update_time":"20181231T120000+0300"},
	 *  {"id":4,"name":"vehicle_class","update_time":"20181231T120000+0300"},
	 *  {"id":16,"name":"road_tsup","update_time":"20181017T120000+0300"},
	 *  {"id":10,"name":"weather","update_time":"20181017T120000+0300"},
	 *  {"id":5,"name":"transponder_available","update_time":"20181017T120000+0300"},
	 *  {"id":8,"name":"poi_type","update_time":"20190329T120000+0300"},
	 *  {"id":15,"name":"notification_topic","update_time":"20181231T120000+0300"},
	 *  {"id":17,"name":"payment_source","update_time":"20181017T120000+0300"},
	 *  {"id":3,"name":"road_segment","update_time":"20190220T120000+0300"},
	 *  {"id":12,"name":"warn_type","update_time":"20181017T120000+0300"},
	 *  {"id":13,"name":"feedback_type","update_time":"20181231T120000+0300"},
	 *  {"id":6,"name":"day_of_week","update_time":"20181017T120000+0300"}]	  
	 * </pre>
	 * или сообщение об ошибке
	 * <pre>
	 * {"message":"Список справочников пуст","code":"DICT002E"}
	 */
	@GET
	@Path("/list")
	@Produces( "application/json" )
	public Response list(@QueryParam("lang") @DefaultValue("ru") String lang) {
    		try {
    			ConcurrentHashMap<String, DictHeader> list = dictAdapter.list();
	    		if (list.size() == 0) {
    				// Список справочников пуст
    				return RestExceptionMapper.newRestException(logger, DICT002E, lang);
	    		} else {
    				// Список справочников
    				return Response.ok(list.elements()).type(MediaType.APPLICATION_JSON_TYPE.withCharset(UTF_8)).build();
	    		}
	        } catch (Throwable throwable) {
        		return RestExceptionMapper.newRestException(logger, throwable, DICT004E, lang);
		} 
	}

}
