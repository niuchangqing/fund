package cn.com.fund.service.task;

import java.util.Set;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.fund.service.FundCodeService;
import cn.com.fund.service.TodayService;

//@Component
//@EnableScheduling
public class ScheduledService {
	private static Logger logger = LoggerFactory.getLogger(ScheduledService.class);

	@Autowired
	private FundCodeService fundCodeService;

	@Autowired
	private TodayService todayService;

	@Scheduled(cron = "0 06 21 * * ?")
	public void scheduled() {
		String day = DateTime.now().toString("yyyy-MM-dd");
		logger.error("定时任务启动：  {}", day);
		Set<String> codes = fundCodeService.updateFundCode();

		todayService.updateTodayData(day, day, codes);
	}
}
