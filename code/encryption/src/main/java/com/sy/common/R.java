package com.sy.common;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class R {
	private R() {
	}

	//成功状态码
	public static final int OK = 200;
	public static final String OK_STRING = "查询成功";
	//失败状态码
	public static final int ERROR = 500;
	public static final String ERROR_STRING = "执行失败";
	//没有权限状态码
	public static final int NO_AUTH = 410;
	//登录失败状态码
	public static final int LOGIN_FAIL = 403;
	//业务失败状态码
	public static final int BUSINESS_FAIL = 100500;

	// http 状态码
	private int code;
	// 返回信息
	private String msg;
	// 返回的数据
	private Object data;

	public R(int code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	public static R ok(String msg, Object data) {
		return new R(R.OK,msg,data);
	}
	public static R ok(int code, String msg, Object data) {
		return new R(code,msg,data);
	}
	public static R error(String msg, Object data) {
		return new R(R.ERROR,msg,data);
	}
	public static R error(int code, String msg, Object data) {
		return new R(code,msg,data);
	}
}
