/**
 * АО Транссеть
 * 
 * http://transset.ru
 */
package ru.transset.ad.mp.param;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by KudinovAM on 07.08.2015.
 * 
 * Миграция из проекта admap. Преобразует строковое представление даты в экземпляр класса Date
 * 
 * @author Валерий Лиховских 
 * 
 */
public class DateParam {

	private Date date;
	
	public DateParam(String dateStr) throws WebApplicationException {
        	if (dateStr == null || dateStr.length() == 0) {
			this.date = null;
			return;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'H:mm:ss");
		try {
			this.date = dateFormat.parse(dateStr);
		} catch (ParseException e1) {
			dateFormat = new SimpleDateFormat("yyyyMMddHmmss");
			try {
				this.date = dateFormat.parse(dateStr);
			} catch (ParseException e2) {
				dateFormat = new SimpleDateFormat("yyyyMMdd");
				try {
					this.date = dateFormat.parse(dateStr);
		                } catch (ParseException e3) {
					throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
						.entity("Couldn't parse date string: " + e3.getMessage())
						.build());
				}
			}
		}
	}

	public Date getDate() {
		return date;
	}

}