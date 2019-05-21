/**
 * АО Транссеть
 * 
 * http://transset.ru
 */
package ru.transset.ad.mp.dict;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * Класс справочника мобильного приложения
 * 
 * @author Валерий Лиховских
 */
public class Dict {

	/**
	 * Идентификатор справочника 
	 */
	private Integer id;
    
	/**
	 * Имя справочника 
	 */
	private String name;
    
	/**
	 * Временная метка изменения справочника  
	 */
	private Date updateTime;

	/**
	 * Записи справочника
	 */
	private List<Hashtable<String, Object>> content;

	/**
	 * Конструктор создания справочника
	 * 
	 * @param id идентификатор справочника
	 * @param name имя справочника
	 * @param updateTime временная метка модификации справочника
	 */
	public Dict(int id, String name, Date updateTime) {
		this.id = id;
		this.name = name;
		this.updateTime = updateTime;
	}
	
	/**
	 * Получить идентификатор справочника
	 * 
	 * @return идентификатор справочника
	 */
	public Integer getId() {
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
	 * Получить строковое представление временной метки изменения справочника
	 * 
	 * @return строковое представление временной метки изменения справочника
	 */
	public String getUpdateTime() {
		return DictHeader.DATE_FORMAT.format(updateTime);
	}
	
	/**
	 * Получить записи справочника
	 * 
	 * @return записи справочника
	 */
	public List<Hashtable<String, Object>> getContent() {
		return content;
	}
	
	/**
	 * Установить записи справочника
	 * 
	 * @param content записи справочника
	 */
	public void setContent(List<Hashtable<String, Object>> content) {
		this.content = content;
	}
	
	/**
	 * Получить временную метку изменения справочника
	 * 
	 * @return временная метка изменения справочника
	 */
	public Date updateTime() {
		return updateTime;
	}
	
}
