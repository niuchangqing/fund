package cn.com.fund.common;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {
	private static ExecutorService es = null;
	static {
		Properties properties = LoadConfigProperties.getInstance();
		es = Executors.newFixedThreadPool(Integer.valueOf(properties.getProperty("threadPoolSize")));
	}

	public static void execute(Runnable r) {
		es.execute(r);
	}

	public static Future<Boolean> submit(Callable<Boolean> c) {
		return es.submit(c);
	}
}
