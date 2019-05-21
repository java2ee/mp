/**
 * АО Транссеть 
 */
package ru.transset.ad.mp.ext;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.logging.log4j.Logger;

import ru.transset.ad.mp.api.exception.RestException;
import ru.transset.ad.mp.api.model.ServerError;
import ru.transset.ad.mp.dict.Messages;

/**
 * Вспомогательный класс  преобразования возникающих исключений в формат JSON для 
 * возврата результата мобильному клиенту
 * 
 * @author Валерий Лиховских
 */
public class RestExceptionMapper implements ExceptionMapper<RestException> {

	/**
	 * Форматирования исключение в формат JSON
	 * 
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	@Override
    public Response toResponse(RestException exception) {
        ServerError error = new ServerError();
        error.setCode(exception.getCode());
        error.setMessage(exception.getMessage());
        Throwable cause = exception.getCause();
        if (cause != null) error.setCause(cause.getMessage());

        return Response.serverError()
                .type(MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"))
                .entity(error)
                .build();
    }

	/**
	 * Сервисный метод генерации ошибки, которая возвращается мобильному клиенту. 
	 * 
	 * @param logger объект журналирования сообщений log4j, если не {@code null}, то
	 *        результат генерации сообщения записывается в журнал   
	 * @param code код ошибки
	 * @param lang локализация возвращаемого сообщения
	 * 
	 * @return возвращаемое мобильному клиенту сообщение об ошибке 
	 * <pre>
	 * {"message":"Ошибка формирования списка справочников","code":"DICT002E","cause":null}
	 * </pre>
	 */
	public static Response newRestException(Logger logger, String code, String lang) { 
		return newRestException(logger, null, code, lang, (Object[]) null);
	}

	/**
	 * Сервисный метод генерации ошибки, которая возвращается мобильному клиенту.
	 * 
	 * @param logger объект журналирования сообщений log4j, если не {@code null}, то
	 *        результат генерации сообщения записывается в журнал   
	 * @param code код ошибки
	 * @param lang локализация возвращаемого сообщения
	 * @param args массив объектов, подставляемых в сообщение об ошибке
	 * 
	 * @return возвращаемое мобильному клиенту сообщение об ошибке 
	 * <pre>
	 * {"message":"Ошибка формирования справочника: map.","code":"DICT001E","cause":null}
	 * </pre>
	 */
	public static Response newRestException(Logger logger, String code, String lang, Object... args) { 
		return newRestException(logger, null, code, lang, args);
	}

	/**
	 * Сервисный метод генерации ошибки, которая возвращается мобильному клиенту.
	 * 
	 * @param logger объект журналирования сообщений log4j, если не {@code null}, то
	 *        результат генерации сообщения записывается в журнал   
	 * @param throwable возникшее во время выполнения ошибка
	 * @param code код ошибки
	 * @param lang локализация возвращаемого сообщения
	 * @param args массив объектов, подставляемых в сообщение об ошибке
	 * 
	 * @return возвращаемое мобильному клиенту сообщение об ошибке 
	 * <pre>
	 * {"message":"Ошибка формирования справочника: map.","code":"DICT001E",
	 * "cause":"[16908@likhovskikh-vv] SYS0250E При вызове метода \"dict\" возникла ошибка.\n\t
	 * Ошибка: \"ru.transset.ad.mp.api.exception.RestException - Test Error\""}
	 * </pre>
	 */
	public static Response newRestException(Logger logger, Throwable throwable, String code, String lang, Object... args) { 
    	String message = Messages.getMessage(code, lang, args);
    	if (logger != null) {
    		if (throwable == null) logger.error(code + ' ' + message);
    		else logger.error(code + ' ' + message, throwable);
    	}
    	return new RestExceptionMapper().toResponse(new RestException(code, message, throwable));
	}

}
