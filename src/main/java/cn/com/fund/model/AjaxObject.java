package cn.com.fund.model;

public class AjaxObject {
	private Integer retCode;
	private String retMsg;
	private Object data;

	public AjaxObject() {

	}
	
	public AjaxObject(Object data) {
		this.retCode = 200;
		this.retMsg = "success";
		this.data = data;
	}

	public AjaxObject(Integer retCode, String retMsg, Object data) {
		this.retCode = retCode;
		this.retMsg = retMsg;
		this.data = data;
	}

	public Integer getRetCode() {
		return retCode;
	}

	public void setRetCode(Integer retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
