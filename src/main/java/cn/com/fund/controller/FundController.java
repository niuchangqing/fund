package cn.com.fund.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.fund.model.AjaxObject;
import cn.com.fund.service.FundCodeService;
import cn.com.fund.service.HistoryService;
import cn.com.fund.service.TodayService;

@Controller
@RequestMapping(value = "/fund")
public class FundController {
	private static final Logger logger = LoggerFactory.getLogger(FundController.class);

	@Autowired
	private FundCodeService fundCodeService;

	@Autowired
	private TodayService todayService;

	@Autowired
	private HistoryService historyService;

//	@RequestMapping(value = "/update/fund/code", method = { RequestMethod.POST, RequestMethod.GET })
//	@ResponseBody
//	public AjaxObject updateFundCode(HttpServletRequest request) {
//		fundCodeService.updateFundCode();
//		return new AjaxObject("success");
//	}

	@RequestMapping(value = "/history", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public AjaxObject history(HttpServletRequest request) {
		Set<String> codes = fundCodeService.updateFundCode();
		historyService.synHistoryDatas(codes);
		return new AjaxObject("触发成功");
	}

	@RequestMapping(value = "/update/byDay", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public AjaxObject today(@RequestParam(value = "startDay", required = true) String startDay,
			@RequestParam(value = "endDay", required = true) String endDay, HttpServletRequest request) {
		logger.error("执行【" + startDay + "-" + endDay + "】数据");
		Set<String> codes = fundCodeService.updateFundCode();
		todayService.updateTodayData(startDay, endDay, codes);
		return new AjaxObject("触发成功");
	}
}
