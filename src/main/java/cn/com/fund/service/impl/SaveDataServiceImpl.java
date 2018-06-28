package cn.com.fund.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.com.fund.common.DataSourceUtils;
import cn.com.fund.model.DetailModel;

@Service
public class SaveDataServiceImpl implements SaveDataService {
	private static Logger logger = LoggerFactory.getLogger(SaveDataServiceImpl.class);

	@Override
	public void save(List<DetailModel> details) {
		if (null == details || details.size() == 0) {
			return;
		}
		String SQL = "insert into t_jijin (FSRQ,DWJZ,LJJZ,SDATE,ACTUALSYI,NAVTYPE,JZZZL,SGZT,SHZT,FHFCZ,FHFCBZ,DTYPE,FHSP,code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DataSourceUtils.getConnection();
			pstmt = conn.prepareStatement(SQL);
			for (DetailModel detailModel : details) {
				String code = detailModel.getCode();
				String FSRQ = StringUtils.trimToEmpty(detailModel.getFSRQ());
				String DWJZ = StringUtils.trimToEmpty(detailModel.getDWJZ());
				String LJJZ = StringUtils.trimToEmpty(detailModel.getLJJZ());
				String SDATE = StringUtils.trimToEmpty(detailModel.getSDATE());
				String ACTUALSYI = StringUtils.trimToEmpty(detailModel.getACTUALSYI());
				String NAVTYPE = StringUtils.trimToEmpty(detailModel.getNAVTYPE());
				String JZZZL = StringUtils.trimToEmpty(detailModel.getJZZZL());
				String SGZT = StringUtils.trimToEmpty(detailModel.getSGZT());
				String SHZT = StringUtils.trimToEmpty(detailModel.getSHZT());
				String FHFCZ = StringUtils.trimToEmpty(detailModel.getFHFCZ());
				String FHFCBZ = StringUtils.trimToEmpty(detailModel.getFHFCBZ());
				String DTYPE = StringUtils.trimToEmpty(detailModel.getDTYPE());
				String FHSP = StringUtils.trimToEmpty(detailModel.getFHSP());

				pstmt.setString(1, FSRQ);
				pstmt.setString(2, DWJZ);
				pstmt.setString(3, LJJZ);
				pstmt.setString(4, SDATE);
				pstmt.setString(5, ACTUALSYI);
				pstmt.setString(6, NAVTYPE);
				pstmt.setString(7, JZZZL);
				pstmt.setString(8, SGZT);
				pstmt.setString(9, SHZT);
				pstmt.setString(10, FHFCZ);
				pstmt.setString(11, FHFCBZ);
				pstmt.setString(12, DTYPE);
				pstmt.setString(13, FHSP);
				pstmt.setString(14, code);
				// Add it to the batch
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			logger.error("save [" + details.size() + "] successfully");
		} catch (Exception e) {
			logger.error("保存数据异常", e);
		} finally {
			DataSourceUtils.release(pstmt, conn);
		}
	}
}
