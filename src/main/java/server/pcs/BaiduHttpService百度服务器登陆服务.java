package server.pcs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import server.pcs.bean.BaiduClient百度服务器验证Bean;
import server.pcs.bean.BaiduDto;
import server.pcs.bean.BaiduLoginRes百度登陆请求Bean;
import server.pcs.util.OkHttpUtil_HTTP客户端工具;
import server.pcs.util.RsaUtil;
import server.pcs.util.StringUtil;
import server.pcs.util.SystemUtil;

/**
 *
 * @author haoyan
 *
 */
public class BaiduHttpService百度服务器登陆服务 {
	static Charset UTF_8 = Charset.forName("UTF-8");
	BaiduClient百度服务器验证Bean baiduClient百度服务器验证信息 = null;

	public BaiduHttpService百度服务器登陆服务() {
		baiduClient百度服务器验证信息 = new BaiduClient百度服务器验证Bean();
	}

	public BaiduHttpService百度服务器登陆服务(String codeString,String code) {
		baiduClient百度服务器验证信息 = new BaiduClient百度服务器验证Bean();
		baiduClient百度服务器验证信息.setVerifycode(code);
		baiduClient百度服务器验证信息.setVcodestr(codeString);
	}

	public BaiduDto 登陆() {
		BaiduDto baiduDto = 登陆百度网盘();
		// BaiduHttpService百度服务器登陆服务.set百度用户信息(baiduDto);
		WriteStringToFile(Constant.localPath + "PcsLogin.txt", baiduDto);

		return baiduDto;
	}

	public void WriteStringToFile(String filePath, BaiduDto baiduDto) {
		try {
			File file = new File(filePath);
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println("bduss=" + baiduDto.getBduss());// 往文件里写入字符串
			ps.println("ptoken=" + baiduDto.getPtoken());
			ps.println("stoken=" + baiduDto.getStoken());
			ps.println("uid=" + baiduDto.getUID());
			ps.println("name=" + baiduDto.getName());
			ps.println("nameshow=" + baiduDto.getNameShow());
			ps.println("workdir=" + baiduDto.getWorkdir());
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void writeUserInfoToFile(String filePath, String userName,String passwd) {
		try {
			File file = new File(filePath);
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println("userName=" + userName);// 往文件里写入字符串
			ps.println("passwd=" + passwd);
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 被 LoginApi.doLogin() 调用
	 * @param userName
	 * @param passwd
	 * @return
	 */
	public BaiduLoginRes百度登陆请求Bean 登陆百度网盘_byUserName_passwd(String userName, String passwd) {
		BaiduLoginRes百度登陆请求Bean baiduLoginRes = null;
		try {
			String enPasswd = RsaUtil.encrypt(
					StringUtil.stringReverse(passwd + baiduClient百度服务器验证信息.getServerTime()),
					baiduClient百度服务器验证信息.getRsaPublicKeyModulus());
			String timestampStr = System.currentTimeMillis() / 1000 + "773_357";
			Map<String, String> body = new HashMap<String, String>();
			body.put("username", userName);								// 用户名
			body.put("password", enPasswd);								// 密码
			body.put("verifycode", baiduClient百度服务器验证信息.getVerifycode());		// 验证码
			body.put("vcodestr", baiduClient百度服务器验证信息.getVcodestr());			// 验证码
			body.put("isphone", "0");									// 是否手机
			body.put("loginmerge", "1");								//
			body.put("action", "login");
			body.put("uid", timestampStr);
			body.put("skin", "default_v2");
			body.put("connect", "0");
			body.put("dv",
					"tk0.408376350146535171516806245342@oov0QqrkqfOuwaCIxUELn3oYlSOI8f51tbnGy-nk3crkqfOuwaCIxUou2iobENoYBf51tb4Gy-nk3cuv0ounk5vrkBynGyvn1QzruvN6z3drLJi6LsdFIe3rkt~4Lyz5ktfn1Qlrk5v5D5fOuwaCIxUobJWOI3~rkt~4Lyi5kBfni0vrk8~n15fOuwaCIxUobJWOI3~rkt~4Lyz5DQfn1oxrk0v5k5eruvN6z3drLneFYeVEmy-nk3c-qq6Cqw3h7CChwvi5-y-rkFizvmEufyr1By4k5bn15e5k0~n18inD0b5D8vn1Tyn1t~nD5~5T__ivmCpA~op5gr-wbFLhyFLnirYsSCIAerYnNOGcfEIlQ6I6VOYJQIvh515f51tf5DBv5-yln15f5DFy5myl5kqf5DFy5myvnktxrkT-5T__Hv0nq5myv5myv4my-nWy-4my~n-yz5myz4Gyx4myv5k0f5Dqirk0ynWyv5iTf5DB~rk0z5Gyv4kTf5DQxrkty5Gy-5iQf51B-rkt~4B__");
			body.put("getpassUrl", "/passport/getpass?clientfrom=&adapter=0&ssid=&from=&authsite=&bd_page_type=&uid="
					+ timestampStr + "&pu=&tpl=wimn&u=https://m.baidu.com/usrprofile%3Fuid%3D" + timestampStr
					+ "%23logined&type=&bdcm=060d5ffd462309f7e5529822720e0cf3d7cad665&tn=&regist_mode=&login_share_strategy=&subpro=wimn&skin=default_v2&client=&connect=0&smsLoginLink=1&loginLink=&bindToSmsLogin=&overseas=&is_voice_sms=&subpro=wimn&hideSLogin=&forcesetpwd=&regdomestic=");
			body.put("mobilenum", "undefined");
			body.put("servertime", baiduClient百度服务器验证信息.getServerTime());
			body.put("gid", "DA7C3AE-AF1F-48C0-AF9C-F1882CA37CD5");
			body.put("logLoginType", "wap_loginTouch");
			body.put("FP_UID", "0b58c206c9faa8349576163341ef1321");
			body.put("traceid", baiduClient百度服务器验证信息.getTraceid());

			RequestBody formBody = null;

			FormBody.Builder formEncodingBuilder = new FormBody.Builder(UTF_8);
			for (String key : body.keySet()) {
				formEncodingBuilder.add(key, body.get(key));
			}
			/*
			 * 准备formBody信息
			 */
			formBody = formEncodingBuilder.build();
			/*
			 * 准备headers信息
			 */
			Headers headers = new Headers.Builder().add("Content-Type", "application/x-www-form-urlencoded")
					.add("Accept", "application/json").add("Referer", "https://wappass.baidu.com/")
					.add("X-Requested-With", "XMLHttpRequest").add("Connection", "keep-alive").build();
			/*
			 * 在此login
			 */
			String okHttpRes = OkHttpUtil_HTTP客户端工具.getInstance().doPostWithBodyAndHeader(Constant.BAIDU_LOGIN_URL, formBody,
					headers);

			/*
			 * 取得登陆反馈信息
			 */
			JSONObject json = JSONObject.parseObject(okHttpRes);
			JSONObject errorInfo = json.getJSONObject("errInfo");
			String errNo = (String) errorInfo.get("no");
			JSONObject data = json.getJSONObject("data");
			//UiLog.getLog(BaiduHttpService百度服务器登陆服务.class).info(data.toJSONString());

			switch (errNo) {
			case "400023":
			case "400101": // 需要验证手机或邮箱
				baiduLoginRes  = new BaiduLoginRes百度登陆请求Bean();
				baiduLoginRes.setErrCode(errNo);
				baiduLoginRes.setErrMsg(errorInfo.getString("msg"));
				//parsePhoneAndEmail(json);
				请求验证用的手机或邮箱信息(data, baiduLoginRes);
				return baiduLoginRes;
			case "500001":
			case "500002":// 需要图片验证码
				baiduLoginRes  = new BaiduLoginRes百度登陆请求Bean();
				baiduLoginRes.setErrCode(errNo);
				baiduLoginRes.setErrMsg(errorInfo.getString("msg"));
				String imgUrl = "https://wappass.baidu.com/cgi-bin/genimage?" + data.getString("codeString");
				baiduLoginRes.setGotoUrl(imgUrl);
				baiduLoginRes.setCodeString(data.getString("codeString"));
				return baiduLoginRes;
			case "0":
				baiduLoginRes = (BaiduLoginRes百度登陆请求Bean) JSONObject.toJavaObject(data, BaiduLoginRes百度登陆请求Bean.class);
				baiduLoginRes.setErrCode(errNo);
				return baiduLoginRes;

			case "400408": // 应国家相关法律要求，自6月1日起使用信息发布、即时通讯等互联网服务需进行身份信息验证。为保障您对相关服务功能的正常使用，建议您尽快完成手机号验证，感谢您的理解和支持。
			default:
				baiduLoginRes  = new BaiduLoginRes百度登陆请求Bean();
				baiduLoginRes.setErrCode(errNo);
				baiduLoginRes.setErrMsg(errorInfo.getString("msg"));
				System.out.println("错误码：" + errNo + "___错误信息：" + errorInfo.get("msg"));
				break;
			}
			return baiduLoginRes;
		} catch (IOException e) {
			e.printStackTrace();
			baiduLoginRes  = new BaiduLoginRes百度登陆请求Bean();
			baiduLoginRes.setErrCode("1111");
			baiduLoginRes.setErrMsg(e.getMessage());
			//UiLog.getLog(BaiduHttpService百度服务器登陆服务.class).info(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			baiduLoginRes  = new BaiduLoginRes百度登陆请求Bean();
			baiduLoginRes.setErrCode("1111");
			baiduLoginRes.setErrMsg(e.getMessage());
			//UiLog.getLog(BaiduHttpService百度服务器登陆服务.class).info(e.getMessage());
		}
		return baiduLoginRes;
	}

	/**
	 * parsePhoneAndEmail
	 * @param data
	 * @param baiduLoginRes
	 */
	private void 请求验证用的手机或邮箱信息(JSONObject data, BaiduLoginRes百度登陆请求Bean baiduLoginRes) {
		String gotoUrl = data.getString("gotoUrl");
		if(StringUtils.isEmpty(gotoUrl)) return;

		// 向百度服务器取得验证相关信息（电话或邮箱）。
		String okHttpRes = null;
		try {
			okHttpRes = OkHttpUtil_HTTP客户端工具.getInstance().doGetWithJsonResult(gotoUrl);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		// 取得的验证用电话或邮箱。
		ArrayList<String> matchList_手机 =  StringUtil.解析指定️字符串_by正则表达式(okHttpRes,
				"<p class=\"verify-type-li-tiptop\">(.*?)</p>\\s+<p class=\"verify-type-li-tipbottom\">通过手机验证码验证身份</p>");
		baiduLoginRes.setCheckPhone(matchList_手机.get(0));
		ArrayList<String> matchList_邮箱 =  StringUtil.解析指定️字符串_by正则表达式(okHttpRes,
				"<p class=\"verify-type-li-tiptop\">(.*?)</p>\\s+<p class=\"verify-type-li-tipbottom\">通过邮箱验证码验证身份</p>");
		baiduLoginRes.setCheckEmail(matchList_邮箱.get(0));

		// 取得最新token
		ArrayList<String> matchList_TokenAndU =  StringUtil.解析指定️字符串_by正则表达式(gotoUrl,
				"token=(.*?)&u=(.*?)&secstate=");
		baiduLoginRes.setToken(matchList_TokenAndU.get(0));
		// 取得最新U
		baiduLoginRes.setU(matchList_TokenAndU.get(1));
	}

	public BaiduDto 登陆百度网盘() {

		try {
			String enPasswd = RsaUtil.encrypt(
					StringUtil.stringReverse(Constant.passwd.trim() + baiduClient百度服务器验证信息.getServerTime()),
					baiduClient百度服务器验证信息.getRsaPublicKeyModulus());
			String timestampStr = System.currentTimeMillis() / 1000 + "773_357";
			Map<String, String> body = new HashMap<String, String>();
			body.put("username", Constant.userName);
			body.put("password", enPasswd);
			body.put("verifycode", baiduClient百度服务器验证信息.getVerifycode());
			body.put("vcodestr", baiduClient百度服务器验证信息.getVcodestr());
			body.put("isphone", "0");
			body.put("loginmerge", "1");
			body.put("action", "login");
			body.put("uid", timestampStr);
			body.put("skin", "default_v2");
			body.put("connect", "0");
			body.put("dv",
					"tk0.408376350146535171516806245342@oov0QqrkqfOuwaCIxUELn3oYlSOI8f51tbnGy-nk3crkqfOuwaCIxUou2iobENoYBf51tb4Gy-nk3cuv0ounk5vrkBynGyvn1QzruvN6z3drLJi6LsdFIe3rkt~4Lyz5ktfn1Qlrk5v5D5fOuwaCIxUobJWOI3~rkt~4Lyi5kBfni0vrk8~n15fOuwaCIxUobJWOI3~rkt~4Lyz5DQfn1oxrk0v5k5eruvN6z3drLneFYeVEmy-nk3c-qq6Cqw3h7CChwvi5-y-rkFizvmEufyr1By4k5bn15e5k0~n18inD0b5D8vn1Tyn1t~nD5~5T__ivmCpA~op5gr-wbFLhyFLnirYsSCIAerYnNOGcfEIlQ6I6VOYJQIvh515f51tf5DBv5-yln15f5DFy5myl5kqf5DFy5myvnktxrkT-5T__Hv0nq5myv5myv4my-nWy-4my~n-yz5myz4Gyx4myv5k0f5Dqirk0ynWyv5iTf5DB~rk0z5Gyv4kTf5DQxrkty5Gy-5iQf51B-rkt~4B__");
			body.put("getpassUrl", "/passport/getpass?clientfrom=&adapter=0&ssid=&from=&authsite=&bd_page_type=&uid="
					+ timestampStr + "&pu=&tpl=wimn&u=https://m.baidu.com/usrprofile%3Fuid%3D" + timestampStr
					+ "%23logined&type=&bdcm=060d5ffd462309f7e5529822720e0cf3d7cad665&tn=&regist_mode=&login_share_strategy=&subpro=wimn&skin=default_v2&client=&connect=0&smsLoginLink=1&loginLink=&bindToSmsLogin=&overseas=&is_voice_sms=&subpro=wimn&hideSLogin=&forcesetpwd=&regdomestic=");
			body.put("mobilenum", "undefined");
			body.put("servertime", baiduClient百度服务器验证信息.getServerTime());
			body.put("gid", "DA7C3AE-AF1F-48C0-AF9C-F1882CA37CD5");
			body.put("logLoginType", "wap_loginTouch");
			body.put("FP_UID", "0b58c206c9faa8349576163341ef1321");
			body.put("traceid", baiduClient百度服务器验证信息.getTraceid());

			RequestBody formBody = null;

			FormBody.Builder formEncodingBuilder = new FormBody.Builder(UTF_8);
			for (String key : body.keySet()) {
				formEncodingBuilder.add(key, body.get(key));
			}
			formBody = formEncodingBuilder.build();

			Headers headers = new Headers.Builder().add("Content-Type", "application/x-www-form-urlencoded")
					.add("Accept", "application/json").add("Referer", "https://wappass.baidu.com/")
					.add("X-Requested-With", "XMLHttpRequest").add("Connection", "keep-alive").build();

			String okHttpRes = OkHttpUtil_HTTP客户端工具.getInstance().doPostWithBodyAndHeader(Constant.BAIDU_LOGIN_URL, formBody,
					headers);
			JSONObject json = JSONObject.parseObject(okHttpRes);
			JSONObject errorInfo = json.getJSONObject("errInfo");
			String errNo = (String) errorInfo.get("no");
			JSONObject data = json.getJSONObject("data");

			switch (errNo) {
			case "500001":
			case "500002":
				System.out.println("需要输入验证码^_^");
				String imgUrl = "https://wappass.baidu.com/cgi-bin/genimage?" + data.getString("codeString");
				System.out.println("验证码链接：" + imgUrl);
//				String testCode = 下载指定图片并解析_byUrl(imgUrl);
//				System.out.println("使用Tesseract猜测验证码为：" + testCode);
				baiduClient百度服务器验证信息.setVcodestr(data.getString("codeString"));
				String verifycode = SystemUtil.getJlineIn("验证码：");
				baiduClient百度服务器验证信息.setVerifycode(verifycode.trim());
				return 登陆百度网盘();
			case "0":
				// BaiduLoginRes百度登陆请求信息 baiduLoginRes百度登陆请求信息 = (BaiduLoginRes百度登陆请求信息) JSONObject.toJavaObject(data, BaiduLoginRes百度登陆请求信息.class);
				List<Cookie> cookies = OkHttpUtil_HTTP客户端工具.getCookie(HttpUrl.parse(Constant.BAIDU_LOGIN_URL));
				BaiduDto baiduDto = new BaiduDto();
				for (int i = 0; i < cookies.size(); i++) {
					Cookie cookie = cookies.get(i);
					if (cookie.name().equals("BDUSS")) {
						baiduDto.setBduss(cookie.value());
					}
					if (cookie.name().equals("PTOKEN")) {
						baiduDto.setPtoken(cookie.value());
					}
					if (cookie.name().equals("STOKEN")) {
						baiduDto.setStoken(cookie.value());
					}
				}
				return baiduDto;
			default:
				System.out.println("错误码：" + errNo + "___错误信息：" + errorInfo.get("msg"));
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *  
	 * @param url
	 * @return
	 * @throws Exception
	 */
//	private static String 下载指定图片并解析_byUrl(String url) throws Exception {
//		String fileName = "imgcode.png";
//		File imgFile = new File(fileName);
//		FileOutputStream fo = new FileOutputStream(imgFile);
//		byte[] b = new byte[1024];
//		int nRead;
//		InputStream input = OkHttpUtil_HTTP客户端工具.getSimpleInstance().doGetWithStream(url, null);
//		while ((nRead = input.read(b, 0, 1024)) > 0) {
//			fo.write(b, 0, nRead);
//		}
//		fo.close();
//		String result = TesseractIdentify.scan(imgFile);
//		System.out.println("验证码本地路径为：" + imgFile.getAbsolutePath());
//		return result;
//	}

//	public static void main(String args[]) throws Exception {
//		String imgUrl = "https://wappass.baidu.com/cgi-bin/genimage?"
//				+ "jxG7f07e2152b59c151020d15619801097b6120440615023110";
//
//		System.out.println(下载指定图片并解析_byUrl(imgUrl));
//	}

//	public static void set百度用户信息(BaiduDto baidu) {
//		try {
//			BaiduDto baiduDto = get贴吧用户信息_byBaiduDto(baidu);
//			baidu.setUID(baiduDto.getUID());
//			baidu.setAge(baiduDto.getAge());
//			baidu.setName(baiduDto.getName());
//			baidu.setNameShow(baiduDto.getNameShow());
//			baidu.setSex(baiduDto.getSex());
//			BaiduClientStore.bdClients.put(baiduDto.getUID(), baiduDto);
//			BaiduClientStore.currentActiveUid = baiduDto.getUID();
//			BaiduClientStore.currentActiveBaiduDto = baiduDto;
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//
//	}

//	private static BaiduDto get贴吧用户信息_byBaiduDto(BaiduDto baidu) throws NoSuchAlgorithmException, IOException {
//		TiebaDto tiebaDto = getUserInfoByBDUSS(baidu);
//		flushUserInfo(tiebaDto);
//		return tiebaDto.getBaidu();
//	}

//	public static void flushUserInfo(TiebaDto tiebaDto) throws NoSuchAlgorithmException, IOException {
//		String uid = tiebaDto.getBaidu().getUID();
//		String name = tiebaDto.getBaidu().getName();
//		if (!StringUtil.isEmpty(uid)) {
//			getUserInfoByUID(tiebaDto);
//		} else if (!StringUtil.isEmpty(name)) {
//			getUserInfoByName(tiebaDto);
//		} else {
//			System.err.println("Baidu uid and name are null");
//		}
//	}

//	private static void getUserInfoByName(TiebaDto tiebaDto) throws NoSuchAlgorithmException, IOException {
//		String name = tiebaDto.getBaidu().getName();
//		String urlStr = "http://tieba.baidu.com/home/get/panel?un=" + name;
//		String okHttpRes = OkHttpUtil_HTTP客户端工具.getInstance().doGetWithJsonResult(urlStr);
//		JSONObject json = JSONObject.parseObject(okHttpRes);
//		JSONObject data = json.getJSONObject("data");
//		if (data.get("id") != null && !"".equals(data.getString("id"))) {
//			tiebaDto.getBaidu().setUID(data.getString("id"));
//			getUserInfoByUID(tiebaDto);
//		}
//	}

//	private static void getUserInfoByUID(TiebaDto tiebaDto) throws NoSuchAlgorithmException, IOException {
//		String uid = tiebaDto.getBaidu().getUID();
//		String rawQuery = "has_plist=0&need_post_count=1&rn=1&uid=" + uid;
//		String signRaw = rawQuery.replace("&", "");
//		signRaw += "tiebaclient!!!";
//		rawQuery += "&sign=";
//		rawQuery += Md5Util_MD5加密工具.encodeByMd5(signRaw.toString()).toUpperCase();
//
//		String urlStr = "http://c.tieba.baidu.com/c/u/user/profile?" + rawQuery;
//		String okHttpRes = OkHttpUtil_HTTP客户端工具.getInstance().doGetWithJsonResult(urlStr);
//		JSONObject json = JSONObject.parseObject(okHttpRes);
//		JSONObject user = json.getJSONObject("user");
//		tiebaDto.getBaidu().setName(user.getString("name"));
//		tiebaDto.getBaidu().setNameShow(user.getString("name_show"));
//		tiebaDto.getBaidu().setAge((int) (Float.parseFloat(user.getString("tb_age"))));
//		tiebaDto.getBaidu().setSex(user.getString("sex"));
//		tiebaDto.setLikeForumNum(Integer.parseInt(user.getString("like_forum_num")));
//		tiebaDto.setPostNum(Integer.parseInt(user.getString("post_num")));
//	}

//	public static TiebaDto getUserInfoByBDUSS(BaiduDto baidu) throws NoSuchAlgorithmException, IOException {
//		String timestampStr = System.currentTimeMillis() / 1000 + "922";
//		Map<String, String> body = new HashMap<String, String>();
//		body.put("bdusstoken", baidu.getBduss() + "|null");
//		body.put("channel_id", "");
//		body.put("channel_uid", "");
//		body.put("stErrorNums", "0");
//		body.put("subapp_type", "mini");
//		body.put("timestamp", timestampStr);
//
//		String model = getPhoneModel(baidu.getBduss());
//		String imei = PhoneModelDict.genCode();
//
//		body.put("_client_type", "2");
//		body.put("_client_version", "7.0.0.0");
//		body.put("_phone_imei", imei);
//		body.put("from", "mini_ad_wandoujia");
//		body.put("model", model);
//
//		String rawData = baidu.getBduss() + "_" + body.get("_client_version") + "_" + imei + "_" + body.get("from");
//		String cuid = Md5Util_MD5加密工具.encodeByMd5(rawData).toUpperCase() + "|" + StringUtil.stringReverse(imei);
//		body.put("cuid", cuid);
//
//		List<String> keys = new ArrayList<String>();
//		for (String key : body.keySet()) {
//			keys.add(key);
//		}
//		Collections.sort(keys);
//
//		StringBuilder signRaw = new StringBuilder();
//		for (int i = 0; i < keys.size(); i++) {
//			String key = keys.get(i);
//			signRaw.append(key + "=" + body.get(key));
//		}
//		signRaw.append("tiebaclient!!!");
//		body.put("sign", Md5Util_MD5加密工具.encodeByMd5(signRaw.toString()).toUpperCase());
//
//		String timestamp = System.currentTimeMillis() / 1000 + "416";
//		Headers headers = new Headers.Builder().add("Content-Type", "application/x-www-form-urlencoded")
//				.add("Cookie", "ka=open").add("net", "1").add("User-Agent", "bdtb for Android 6.9.2.1")
//				.add("client_logid", timestamp).add("Connection", "keep-alive").build();
//
//		RequestBody formBody = null;
//		FormBody.Builder formEncodingBuilder = new FormBody.Builder(UTF_8);
//		for (String key : body.keySet()) {
//			formEncodingBuilder.add(key, body.get(key));
//		}
//
//		formBody = formEncodingBuilder.build();
//
//		String result = OkHttpUtil_HTTP客户端工具.getInstance().doPostWithBodyAndHeader(Constant.BAIDU_TIEBA_LOGIN_URL, formBody,
//				headers);
//		JSONObject json = JSONObject.parseObject(result);
//		JSONObject user = json.getJSONObject("user");
//		JSONObject anti = json.getJSONObject("anti");
//
//		BaiduDto baiduDto = new BaiduDto();
//		baiduDto.setName(user.get("name") == null ? "" : user.getString("name"));
//		baiduDto.setUID(user.get("id") == null ? "" : user.getString("id"));
//		baiduDto.setBduss(user.getString("BDUSS"));
//		TiebaDto tiebaDto = new TiebaDto();
//		tiebaDto.setBaidu(baiduDto);
//		tiebaDto.setTbs(anti.getString("tbs"));
//		return tiebaDto;
//	}

//	public static String getPhoneModel(String bduss) {
//		if (PhoneModelDict.phoneModelDataBase.length <= 0)
//			return "S3";
//		int len = PhoneModelDict.phoneModelDataBase.length;
//		BigInteger hash = BigInteger.valueOf(2134);
//		for (int i = 0; i < bduss.length(); i++) {
//			hash = hash.add(hash.shiftLeft(4).add(BigInteger.valueOf((int) bduss.charAt(i))));
//		}
//		int loc = hash.mod(BigInteger.valueOf(len)).intValue();
//		return PhoneModelDict.phoneModelDataBase[loc];
//	}
}
