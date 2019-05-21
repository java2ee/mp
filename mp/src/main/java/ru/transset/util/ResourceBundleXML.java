/**
 * АО Транссеть
 * 
 * http://www.transset.ru
 */
package ru.transset.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Класс чтения локализованных сообщений из XML файлов
 * 
 * @author Валерий Лиховских
 * @version 1.00, 16 мая 2019.
 * @since   JDK 1.8
 *
 */
public class ResourceBundleXML extends ResourceBundle {

	private Properties properties;

	ResourceBundleXML(InputStream stream) throws IOException {
		properties = new Properties();
		properties.loadFromXML(stream);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Enumeration getKeys() {
		return properties.keys();
	}

	@Override
	protected Object handleGetObject(String key) {
		return properties.getProperty(key);
	}

	public static class Control extends ResourceBundle.Control {

		public List<String> getFormats(String baseName) {
			if (baseName == null)
				throw new NullPointerException();
			return Arrays.asList("xml");
		}

		public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader,
				boolean reload) throws IllegalAccessException, InstantiationException, IOException {
			if (baseName == null || locale == null || format == null || loader == null)
				throw new NullPointerException();
			ResourceBundle bundle = null;
			if (format.equals("xml")) {
				String bundleName = toBundleName(baseName, locale);
				String resourceName = toResourceName(bundleName, format);
				InputStream stream = null;
				if (reload) {
					URL url = loader.getResource(resourceName);
					if (url != null) {
						URLConnection connection = url.openConnection();
						if (connection != null) {
							connection.setUseCaches(reload);
							stream = connection.getInputStream();
						}
					}
				} else {
					stream = loader.getResourceAsStream(resourceName);
				}
				if (stream != null) {
					BufferedInputStream bis = new BufferedInputStream(stream);
					bundle = new ResourceBundleXML(bis);
					bis.close();
				}
			}
			return bundle;
		}
	}
	
}
