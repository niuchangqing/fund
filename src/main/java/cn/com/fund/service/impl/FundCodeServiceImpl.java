package cn.com.fund.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.fund.common.RedisClient;
import cn.com.fund.service.FundCodeService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@Service
public class FundCodeServiceImpl implements FundCodeService {
	private static Logger logger = LoggerFactory.getLogger(FundCodeServiceImpl.class);

	@Override
	public Set<String> updateFundCode() {
		Set<String> codes = new HashSet<String>();
		OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(2, TimeUnit.SECONDS)
				.connectTimeout(2, TimeUnit.SECONDS).build();
		final String url = "http://fund.eastmoney.com/Data/Fund_JJJZ_Data.aspx?t=1&lx=1&letter=&gsid=&text=&sort=zdf,desc&page=1,9999&feature=|&dt=1528599028532&atfc=&onlySale=0";
		try {
			Request req = new Request.Builder().url(url).header("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
					.addHeader("Accept", "*/*").addHeader("DNT", "1").addHeader("Host", "fund.eastmoney.com").build();
			Response res = client.newCall(req).execute();
			if (res.isSuccessful()) {
				String resString = res.body().string();
				String value = StringUtils.replace(resString, "var db=", "");
				JSONObject obj = JSON.parseObject(value);
				JSONArray array = obj.getJSONArray("datas");
				if (null != array && array.size() > 0) {
					for (Object object : array) {
						JSONArray parseArray = JSON.parseArray(object.toString());
						System.out.println(parseArray.get(0));
						if (StringUtils.isNumeric(parseArray.get(0).toString())) {
							String code = StringUtils.trimToEmpty(parseArray.get(0).toString());
							if (StringUtils.isNumeric(code)) {
								codes.add(code);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error("", e);
		}

		return codes;
	}

	public void updateFundCodebk() {
		OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(2, TimeUnit.SECONDS)
				.connectTimeout(2, TimeUnit.SECONDS).build();
		final String url = "http://fund.eastmoney.com/Data/Fund_JJJZ_Data.aspx?t=1&lx=1&letter=&gsid=&text=&sort=zdf,desc&page=1,9999&feature=|&dt=1528599028532&atfc=&onlySale=0";
		try {
			Request req = new Request.Builder().url(url).header("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
					.addHeader("Accept", "*/*").addHeader("DNT", "1").addHeader("Host", "fund.eastmoney.com").build();
			Response res = client.newCall(req).execute();
			if (res.isSuccessful()) {
				String resString = res.body().string();
				String value = StringUtils.replace(resString, "var db=", "");
				JSONObject obj = JSON.parseObject(value);
				JSONArray array = obj.getJSONArray("datas");
				if (null != array && array.size() > 0) {
					Jedis jedis = RedisClient.getInstance();
					Pipeline pipelined = jedis.pipelined();
					try {
						for (Object object : array) {
							JSONArray parseArray = JSON.parseArray(object.toString());
							System.out.println(parseArray.get(0));
							if (StringUtils.isNumeric(parseArray.get(0).toString())) {
								pipelined.sadd("jijin.ids", parseArray.get(0).toString());
							}
						}
						pipelined.sync();
					} catch (Exception e) {
						jedis.close();
					}
				}
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}
}
