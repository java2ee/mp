/**
 * АО Транссеть
 * 
 * http://transset.ru
 */
package ru.transset.ad.mp.dict;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс декларации служебных заголовков справочника. Экземпляры класса создаются
 * по записям таблицы ADMAP.DICTS
 * 
 * @author Валерий Лиховских
 *
 */
public class DictHeader {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd'T'hhmmssZ");
	
	/**
	 * Идентификатор справочника 
	 */
	private int id;
    
	/**
	 * Имя справочника 
	 */
	private String name;
    
	/**
	 * Время модификации справочника 
	 */
	private Date updateTime;

	/**
	 * Создать заголовок справочника
	 * 
	 * @param id идентификатор справочника
	 * @param name имя справочника
         * @param updateTime временная метка модификации справочника
	 */
	public DictHeader(int id, String name, Date updateTime) {
		this.id = id;
	    	this.name = name;
	    	this.updateTime = updateTime;
	}

	/**
	 * Получить идентификатор справочника
	 * 
	 * @return идентификатор справочника
	 */
	public int getId() {
		return id;
	}

	/**
	 * Получить имя справочника
	 * 
	 * @return имя справочника
	 */
	public String getName() {
		return name;
	}

	/**
	 * Получить временную метку модификации справочника
	 * 
	 * @return временная метка модификации справочника
	 */
	public String getUpdateTime() {
		return DATE_FORMAT.format(updateTime);
	}

	/**
	 * Изменить временную метку модификации справочника
	 * 
	 * @param updateTime временная метка модификации справочника
         */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * Создать экземпляр справочника
	 *  
	 * @return экземпляр справочника
	 */
	public Dict createDict() {
    		return new Dict(id, name, updateTime); 
	}
    
}
