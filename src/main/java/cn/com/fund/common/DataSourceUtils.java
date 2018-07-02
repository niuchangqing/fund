package cn.com.fund.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DataSourceUtils {
	private static Logger logger = LoggerFactory.getLogger(DataSourceUtils.class);

	private static DataSource dataSource = null;

	static {
		try {
			Properties properties = LoadConfigProperties.getInstance();

			logger.error("初始化连接池");
			dataSource = DruidDataSourceFactory.createDataSource(properties);
			logger.error("初始化完毕");
		} catch (Exception e) {
			logger.error("创建连接池异常", e);
		}
	}

	public static Connection getConnection() throws SQLException {
		Connection conn = dataSource.getConnection();
		conn.setAutoCommit(false);
		return conn;
	}

	public static void release(PreparedStatement pst, Connection conn) {
		try {
			pst.close();
			conn.close();
		} catch (Exception e) {
			logger.error("close连接异常", e);
		}
	}
}
