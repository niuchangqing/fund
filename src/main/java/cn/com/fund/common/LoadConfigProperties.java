package cn.com.fund.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadConfigProperties {
	private static Logger logger = LoggerFactory.getLogger(LoadConfigProperties.class);
	private static Properties properties = new Properties();

	static {
		try {
			InputStream in = DataSourceUtils.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(in);
			in.close();
		} catch (IOException e) {
			logger.error("load properties 异常", e);
		}
	}

	public static Properties getInstance() {
		return properties;
	}

}
