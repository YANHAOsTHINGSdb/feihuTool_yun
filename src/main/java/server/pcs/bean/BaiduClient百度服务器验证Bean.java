package server.pcs.bean;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import server.pcs.Constant;
import server.pcs.util.OkHttpUtil_HTTP客户端工具;
import server.pcs.util.StringUtil;

/**
 * 百度服务器验证信息
 * @author haoyan
 *
 */
@Data
public class BaiduClient百度服务器验证Bean {
	String serverTime; // 百度服务器时间, 形如 "e362bacbae"
	String rsaPublicKeyModulus;
	String fpUID;
	String traceid;
	String serverTimeUrl = "";
	String vcodestr = "";
	String verifycode = "";

	/**
	 * OkHttpClient是用来完成android 客户端对服务端请求的工具。
	 */
	private static OkHttpClient client;

	public BaiduClient百度服务器验证Bean() {
		client = OkHttpUtil_HTTP客户端工具.getInstance().getClient();
		try {
			取得百度服务器时间();
			取得百度公钥密码之RSA密码();
			取得追踪ID();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void 取得百度服务器时间() throws IOException {
		String rs = OkHttpUtil_HTTP客户端工具.getInstance().doGetWithJsonResult(Constant.BAIDU_SERVERTIME_URL);
		JSONObject json = JSONObject.parseObject(rs);
		serverTime = json.getString("time");
		if (StringUtil.isEmpty(serverTime))
			serverTime = "e362bacbae";
	}

	private void 取得百度公钥密码之RSA密码() throws IOException {
		String rs = OkHttpUtil_HTTP客户端工具.getInstance().doGetWithJsonResult(Constant.BAIDU_RSA_URL);
		Pattern pattern = Pattern.compile(",rsa:\"(.*?)\",error:");
		Matcher matcher = pattern.matcher(rs);
		if (matcher.find()) {
			rsaPublicKeyModulus = matcher.group(1);
		} else {
			rsaPublicKeyModulus = "B3C61EBBA4659C4CE3639287EE871F1F48F7930EA977991C7AFE3CC442FEA49643212E7D570C853F368065CC57A2014666DA8AE7D493FD47D171C0D894EEE3ED7F99F6798B7FFD7B5873227038AD23E3197631A8CB642213B9F27D4901AB0D92BFA27542AE890855396ED92775255C977F5C302F1E7ED4B1E369C12CB6B1822F";
		}

	}

	private void 取得追踪ID() throws IOException {
		Response response = OkHttpUtil_HTTP客户端工具.getInstance().doGetWithResponse(Constant.BAIDU_TRACEID_URL);
		traceid = response.header("Trace-Id");
		response.close();
	}


}
