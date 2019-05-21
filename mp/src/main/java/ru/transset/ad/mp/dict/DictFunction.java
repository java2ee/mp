/**
 * АО Транссеть 
 * 
 * http://transset.ru
 */
package ru.transset.ad.mp.dict;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import ru.funsys.avalanche.Application;
import ru.funsys.avalanche.Parameter;
import ru.funsys.avalanche.sql.Adapter;
import ru.transset.ad.mp.api.exception.RestException;

/**
 * Класс реализации методов формирования данных REST сервиса {@code dict}
 * 
 * @author Валерий Лиховских
 *
 */
public class DictFunction extends Application {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6347387494049320487L;

	/**
	 * Адаптер источника данных. Для инициализации этого поля в файле конфигурации должен быть 
	 * определен дочерний элкемент <b>{@code <adapter>}</b> с атрибутом <b>{@code name}</b>
	 * с установленным значением <b>{@code admap}</b>. 
	 * 
	 * <pre>
	 * &lt;adapter class="ru.funsys.avalanche.sql.Adapter" name="admap" uri="admap" /&gt;	
	 * </pre> 
	 */
	private Adapter admap;
	
	/**
	 * Справочники приложения 
	 */
	private ConcurrentHashMap<String, DictHeader> dicts = new ConcurrentHashMap<String, DictHeader>();
    
	/**
	 * Временная метка загрузки справочников из таблиц 
	 */
	private long lastRefresh = 0L;

	/**
	 * Период проверки обновления данных в справочнике 
	 */
	private long period = 300000L;
    
	/**
	 * Установить период контроля изменения таблиц справочников, в минутах.
	 * Допустимый диапазон от 2 до 60 минут.
	 * 
	 * @param minute период контроля изменения справочников, мин.
	 */
	public void setPeriod(String minute) {
    		int value = Integer.parseInt(minute);
	    	if (value > 1 && 61 > value) {
    			period = 60000L * value;
	    	}
	}
    
	/**
	 * Обновляет заголовки справочников с заданной периодичностью 
	 */
	public void refreshDictMap() {
		long currentTime = System.currentTimeMillis(); 
        	if (currentTime - lastRefresh > period) {
			try {
        	        	ResultSet rs = admap.select("select  id, name, updated_at as updateTime from dicts");
	                	while (rs.next()) {
        	        		DictHeader dictHeader = new DictHeader(rs.getInt("id"), rs.getString("name"), rs.getDate("updateTime"));
	        	           	dicts.put(dictHeader.getName(), dictHeader);
        	        	}
    				lastRefresh = currentTime;
    			} catch (Exception e) {
	    			lastRefresh = 0L;
    				logger.error(e);
    			}
	        }
	}

	/**
	 * Получить записи справочника. Справочник формируется из пары таблиц <b>ИМЯ_СПРАВОЧНИКА</b> и <b>ИМЯ_СПРАВОЧНИКА_version</b>.
	 * Например, для справочника <b>day_of_week</b> будут использоваться две таблицы: <b>ADMAP.DAY_OF_WEEK</b> и
	 * <b>ADMAP.DAY_OF_WEEK_VERSION</b>. Значения полей справочника <b>{@code id, name и updateTime}</b> формируются на
	 * основании таблицы <b>ADMAP.DICTS</b>.
	 * В параметрах класса (дочерние элементы <b>{@code <parameter>}</b> в конфигурационном файле) можно
	 * определить произвольный SQL запрос для формирования справочника, атрибут <b>{@code name}</b> которого
	 * должен иметь префикс <b>{@code "dictionary."}</b>. Пример, для справочника <b>locality</b>
	 * <pre>
	 * &lt;parameter name="dictionary.locality" value="SELECT * FROM admap.locality WHERE language = ? ORDER BY name_city ASC"/&gt;
	 * </pre>
	 * 
	 * @param dictName имя справочника
	 * @param lang локализация справочника
	 * 
	 * @return экземпляр класса Dict или {@code null}, если справочник не найден
	 * 
	 * @throws Exception исключение, которое может возникнуть при обработке запроса
	 */
	public Dict dict(String dictName, String lang) throws Exception {
        	refreshDictMap();
	        DictHeader dictHeader = dicts.get(dictName);
	        Dict dict;
	        if (dictHeader == null) {
        		dict = null;
	        } else {
        		dict = dictHeader.createDict(); 
        		String sql;
	        	Parameter parameter = getParameter("dictionary." + dictName);
        		if (parameter == null) {
        			// генерировать SQL запрос чтения справочника
        			StringBuilder builder = new StringBuilder(
        				"SELECT dict.*, dict_version.id version_id, dict_version.description, dict_version.image, dict_version.language\r\n  FROM ");
	        		builder.append(dictName).append(" dict\r\n  JOIN ").append(dictName).append("_version dict_version ON dict_version.").append(dictName).append("_id = dict.id\r\n");
       				builder.append("  WHERE dict_version.language = ?\r\n");  // параметр запроса - lang
    				builder.append("  ORDER BY dict.pos, dict.id");
        			sql = builder.toString();
	        	} else {
        			// Выполнить чтение справочника с использованием SQL запроса, определенного в параметре
        			// В запросе должно быть определено условие "WHERE language = ?" 
   				sql = parameter.getValue();
	        	}
        		ResultSet rs = admap.select(sql, lang);
        		ResultSetMetaData md = rs.getMetaData();
	        	List<Hashtable<String, Object>> contentList = new ArrayList<Hashtable<String, Object>>();
        		int columnCount = md.getColumnCount();
			while (rs.next()) {
		            	Hashtable<String, Object> row = new Hashtable<String, Object>();
		                for (int index = 1; index <= columnCount; ++index) {
					String column = md.getColumnLabel(index);
					Object value = rs.getObject(index);
					if (value != null) row.put(column, value);
		                }
		            	contentList.add(row);
			}
			dict.setContent(contentList);
	        }
        	return dict;
	}
	
	/**
	 * Получить список справочников мобильного приложения
	 * 
	 * @return список справочников
	 * 
	 */
	public ConcurrentHashMap<String, DictHeader> list() throws RestException {
	        refreshDictMap();
        	return dicts;
	}

	/**
	 * Инициализация функции обработки пользовательских запросов справочников.
	 * 
	 * @see ru.funsys.avalanche.Application#init()
	 */
	@Override
	public void init() {
		super.init();
		refreshDictMap();
	}

	
}
