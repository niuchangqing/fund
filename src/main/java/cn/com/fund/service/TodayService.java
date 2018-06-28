package cn.com.fund.service;

import java.util.Set;

public interface TodayService {
	public void updateTodayData(String startDay, String endDay, Set<String> codes);
}
