/**
 * АО Транссеть
 * 
 * http://transset.ru
 */
package ru.transset.ad.mp.api.model;

/**
 * Класс преобразования исключения в возвращаемый при ошибке ответ в формате json.
 * 
 * @author Валерий Лиховских
 *
 */
public class ServerError {

	/**
	 * Сообщение об ошибке  
	 */
	private String message;

	/**
	 * Код ошибки 
	 */
	private String code;

	/**
	 * Сообщение о причине ошибке 
	 */
	private String cause;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

}
