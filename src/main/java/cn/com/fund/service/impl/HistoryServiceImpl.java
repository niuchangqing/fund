package cn.com.fund.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.com.fund.common.ThreadPool;
import cn.com.fund.model.DetailModel;
import cn.com.fund.model.Model;
import cn.com.fund.service.HistoryService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class HistoryServiceImpl implements HistoryService {
	private static Logger logger = LoggerFactory.getLogger(FundCodeServiceImpl.class);
	private static OkHttpClient client = new OkHttpClient();
	private static AtomicInteger count = new AtomicInteger();

	@Autowired
	private SaveDataService saveDataService;

	@Override
	public void synHistoryDatas(Set<String> codes) {
		if (null != codes && codes.size() > 0) {
			count.set(codes.size());
			for (final String id : codes) {
				ThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						logger.error("添加任务" + id);
						detail(id);
					}
				});
			}
		}
	}

	public void detail(String id) {
		final String url = "http://api.fund.eastmoney.com/f10/lsjz?fundCode=%s&pageIndex=%s&pageSize=30&startDate=&endDate=&_=1528601899541";

		for (int i = 0; i < 3; i++) {
			if (i > 0) {
				logger.error("重试......." + id);
			}
			try {
				List<DetailModel> details = new ArrayList<DetailModel>();
				int index = 0;
				while (true) {
					String localUrl = String.format(url, id, index);
					Request req = new Request.Builder().url(localUrl).header("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
							.addHeader("Accept", "*/*").addHeader("DNT", "1")
							.addHeader("Host", "api.fund.eastmoney.com")
							.addHeader("Referer", "http://fund.eastmoney.com/f10/jjjz_003407.html").build();
					Response res = client.newCall(req).execute();
					String resString = "";
					if (res.isSuccessful()) {
						resString = res.body().string();
					} else {
						throw new RuntimeException();
					}
					Model model = JSON.toJavaObject(JSON.parseObject(resString), Model.class);
					List<DetailModel> detail = model.getData().getLSJZList();

					if (detail.isEmpty()) {
						break;
					} else {
						details.addAll(detail);
					}
					index++;
				}
				for (DetailModel detailModel : details) {
					detailModel.setCode(id);
				}
				saveDataService.save(details);
				int decrementAndGet = count.decrementAndGet();
				logger.error("id-" + id + " success ! count = " + decrementAndGet);
				break;
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}
}
