/**
 * АО Транссеть
 * 
 * http://transset.ru
 */
package ru.transset.ad.mp.api.exception;

/**
 * Класс генерации возвращаемых сообщений об ошибках при обработке REST запросов
 * 
 * @author Валерий Лиховских
 *
 */
public class RestException extends Throwable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -3820776508288187732L;
	
	/**
	 * Код ошибки 
	 */
	private final String code;

	public RestException(String code, String message) {
		super(message);
		this.code = code;
	}

	public RestException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public RestException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
