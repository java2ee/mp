/**
 * АО Транссеть
 * 
 * http://transset.ru
 */
package ru.transset.ad.mp.dict;


import java.util.concurrent.ConcurrentHashMap;

import ru.funsys.avalanche.AvalancheRemote;

/**
 * Адаптер функции обработки запросов справочников мобильного приложения 
 * 
 * @author Валерий Лиховских
 *
 */
public interface DictAdapter {

	/**
	 * Получить справочник мобильного приложения 
	 * 
	 * @param dictName имя справочника
	 * @param lang локализация справочника
	 * 
	 * @return справочник или возникшее при обработке запроса исключение
	 * 
	 * @see ru.trannset.ad.mp.dict.DictFunction#dict(java.lang.String, java.lang.String)

	 * @throws AvalancheRemote
	 * 
	 */
	public Dict dict(String dictName, String lang) throws AvalancheRemote;
	
	/**
	 * Получить список справочников мобильного приложения
	 * 
	 * @return список справочников или возникшее при обработке запроса исключение
	 * 
	 * @throws AvalancheRemote
	 */
	public ConcurrentHashMap<String, DictHeader> list() throws AvalancheRemote;
	
}
