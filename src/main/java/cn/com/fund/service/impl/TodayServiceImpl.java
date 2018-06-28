package cn.com.fund.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.com.fund.common.ThreadPool;
import cn.com.fund.model.DetailModel;
import cn.com.fund.model.Model;
import cn.com.fund.service.TodayService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class TodayServiceImpl implements TodayService {
	private static Logger logger = LoggerFactory.getLogger(FundCodeServiceImpl.class);
	private static OkHttpClient client = new OkHttpClient();
	@Autowired
	private SaveDataService saveDataService;

	@Override
	public void updateTodayData(String startDay, String endDay, Set<String> codes) {
		final String start = startDay;
		final String end = endDay;
		if (null != codes && codes.size() > 0) {
			for (final String id : codes) {
				ThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						detail(id, start, end);
					}
				});
			}
		}
	}

	public void detail(String id, String startDay, String endDay) {
		for (int i = 0; i < 3; i++) {
			final String url = "http://api.fund.eastmoney.com/f10/lsjz?fundCode=%s&pageIndex=1&pageSize=20&startDate=%s&endDate=%s&_=1530173522815";
			try {
				List<DetailModel> details = null;
				String localUrl = String.format(url, id, startDay, endDay);
				Request req = new Request.Builder().url(localUrl).header("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
						.addHeader("Accept", "*/*").addHeader("DNT", "1").addHeader("Host", "api.fund.eastmoney.com")
						.addHeader("Referer", "http://fund.eastmoney.com/f10/jjjz_003407.html").build();
				Response res = client.newCall(req).execute();
				String resString = "";
				if (res.isSuccessful()) {
					resString = res.body().string();
				} else {
					throw new RuntimeException();
				}
				Model model = JSON.toJavaObject(JSON.parseObject(resString), Model.class);
				details = model.getData().getLSJZList();

				List<DetailModel> list = new ArrayList<DetailModel>();
				if (null != details && details.size() > 0) {
					for (DetailModel detailModel : details) {
						logger.error(detailModel.toString());
						detailModel.setCode(id);
						list.add(detailModel);
					}
				}

				saveDataService.save(list);
				logger.error("id-" + id + " success !");
				if (i > 0) {
					logger.error("id-" + id + " 重试success !");
				}
				break;
			} catch (Exception e) {
				logger.error("", e);
			}

			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
