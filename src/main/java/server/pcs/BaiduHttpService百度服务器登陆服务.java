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

import org.apache.commons.collections.CollectionUtils;
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

	        本地服务端							     百度服务器
	------------------------------------------------------------------------

	登陆百度网盘_byUserName_passwd --->

								 <---    返回错误信息，及验证码图片地址

	登陆百度网盘_byUserName_passwd_图片验证码_图片验证密码 --->

							 	 <---    返回错误信息：需要手机/邮箱验证

	请求验证用的手机或邮箱信息       --->

							 	 <---    返回最新token及最新U（手机/邮箱验证码回收地址）

	申请发送验证码到手机或邮箱       --->

								 <---    返回操作信息：已经发送验证码

	发送从手机或邮箱取到的验证码     --->

	参考：https://github.com/iikira/Baidu-Login/
	------------------------------------------------------------------------

	花了一个礼拜时间研究某人(https://github.com/iikira/Baidu-Login/)
	的百度登陆验证代码。
	接近尾声，趁着记忆尚温留点笔记。

	总结：为什么代码要开源？--为了方便别人抄袭
	                      （无数次失败之后、你会发现读一读代码是最简单的
	     为什么要抄袭      --代价最小
	                      （无数次开发之后、你会发现。。。
	     为什么要抄他的    --别人的都不好用
	                      （无数次尝试之后、你会发现。。。
	     怎么抄袭别人的代码 --把他的语言学会、再用你的语言写一遍
	                      （无数次搜索之后、你会发现。。。
	     为什么不能直接用   --登陆只是一个功能，
	                      （其他业务不是这个语言写的，不能放一起
	     技术水平如何提高？ --锁定需求，硬憋。
	                      （要有需求和进度跟着，代码才有写完的可能
	     什么开发语言最好？ --无所谓、能解决问题就行
	                      （无数次被忽悠之后、你会发现。。。

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
		// "token=(.*?)&u=(.*?)&secstate="
		ArrayList<String> matchList_TokenAndU =  StringUtil.解析指定️字符串_by正则表达式(gotoUrl,
				"token=(.*?)&u=(.*?)&secstate=");
		String a[] = matchList_TokenAndU.get(0).split("&");
		Map<String, String> rawTokenAndUMap = StringUtil.将ArrayStr转成Map(a, "=");
		baiduLoginRes.setToken(rawTokenAndUMap.get("token"));
		// 取得最新U
		baiduLoginRes.setU(rawTokenAndUMap.get("u"));
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

	public Map<String, String> 申请发送验证码到手机或邮箱(String type, String token) {
		/**
			url := fmt.Sprintf("https://wappass.baidu.com/passport/authwidget?action=send&tpl=&type=%s&token=%s&from=&skin=&clientfrom=&adapter=2&updatessn=&bindToSmsLogin=&upsms=&finance=", verifyType, token)
			body, err := bc.Fetch("GET", url, nil, nil)
			if err != nil {
				return err.Error()
			}

			rawMsg := regexp.MustCompile(`<p class="mod-tipinfo-subtitle">\s+(.*?)\s+</p>`).FindSubmatch(body)
			if len(rawMsg) >= 1 {
				return string(rawMsg[1])
			}

			return "未知消息"
		 */
		//String.format("this is a %2$s %1$s %s %s test", "java", "C++");
		String url = String.format("https://wappass.baidu.com/passport/authwidget?action=send&tpl=&type=%s&token=%s&from=&skin=&clientfrom=&adapter=2&updatessn=&bindToSmsLogin=&upsms=&finance=",type ,token);

		// 向百度服务器取得验证相关信息（电话或邮箱）。
		String okHttpRes = null;
		try {
			okHttpRes = OkHttpUtil_HTTP客户端工具.getInstance().doGetWithJsonResult(url);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		ArrayList<String> rawMsg =  StringUtil.解析指定️字符串_by正则表达式(okHttpRes,
				"<p class=\"mod-tipinfo-subtitle\">\\s+(.*?)\\s+</p>");

		Map<String, String> msgMap = new HashMap<String, String>();
		if( !CollectionUtils.isEmpty(rawMsg)) {
			msgMap.put("msg", rawMsg.get(0));
		}else {
			msgMap.put("msg", "未知消息");
			msgMap.put("error", "未知消息");
		}
		return msgMap;
	}

	public Map 发送从手机或邮箱取到的验证码(String vcode, String token, String u, String verifyType) {
		/**
			// VerifyCode 输入 手机/邮箱 收到的验证码, 验证登录
			func (bc *BaiduClient) VerifyCode(verifyType, token, vcode, u string) (lj *LoginJSON) {
				lj = &LoginJSON{}
				header := map[string]string{
					"Connection":                "keep-alive",
					"Host":                      "wappass.baidu.com",
					"Pragma":                    "no-cache",
					"Upgrade-Insecure-Requests": "1",
				}

				timestampStr := strconv.FormatInt(time.Now().Unix(), 10) + "773_357" + "994"
				url := fmt.Sprintf("https://wappass.baidu.com/passport/authwidget?v="+timestampStr+"&vcode=%s&token=%s&u=%s&action=check&type=%s&tpl=&skin=&clientfrom=&adapter=2&updatessn=&bindToSmsLogin=&isnew=&card_no=&finance=&callback=%s", vcode, token, u, verifyType, "jsonp1")
				body, err := bc.Fetch("GET", url, nil, header)
				if err != nil {
					lj.ErrInfo.No = "-2"
					lj.ErrInfo.Msg = "网络请求错误: " + err.Error()
					return
				}

				// 去除 body 的 callback 嵌套 "jsonp1(...)"
				body = bytes.TrimPrefix(body, []byte("jsonp1("))
				body = bytes.TrimSuffix(body, []byte(")"))

				// 如果 json 解析出错, 直接输出错误信息
				if err := jsoniter.Unmarshal(body, &lj); err != nil {
					lj.ErrInfo.No = "-2"
					lj.ErrInfo.Msg = "提交手机/邮箱验证码错误: " + err.Error()
					return
				}

				// 最后一步要访问的 URL
				u = fmt.Sprintf("%s&authsid=%s&fromtype=%s&bindToSmsLogin=", u, lj.Data.AuthSID, verifyType) // url

				_, err = bc.Fetch("GET", u, nil, nil)
				if err != nil {
					lj.ErrInfo.No = "-2"
					lj.ErrInfo.Msg = "提交手机/邮箱验证码错误: " + err.Error()
					return
				}

				lj.parseCookies(u, bc.Jar.(*cookiejar.Jar))
				return lj
			}
		 */
		/*
		 * 准备headers信息
		 */
		Headers headers = new Headers.Builder()
				.add("Connection", "keep-alive")
				.add("Host", "wappass.baidu.com")
				.add("Pragma", "no-cache")
				.add("Upgrade-Insecure-Requests", "1")
				.build();
		String timestampStr = System.currentTimeMillis() / 1000 + "773_357" + "994";
		String url = String.format("https://wappass.baidu.com/passport/authwidget?v="+timestampStr+"&vcode=%s&token=%s&u=%s&action=check&type=%s&tpl=&skin=&clientfrom=&adapter=2&updatessn=&bindToSmsLogin=&isnew=&card_no=&finance=&callback=%s", vcode, token, u, verifyType, "jsonp1");
		// 向百度服务器发送Get请求
		String okHttpRes = null;
		try {
			okHttpRes = OkHttpUtil_HTTP客户端工具.getInstance().doGetWithJsonResult(url, headers);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		// 去除 body 的 callback 嵌套 "jsonp1(...)"
		/**
			jsonp1({
			    "errInfo":{
			        "no": "110001",
			        "msg": "参数错误"
			    },
			     "data": {
			        "u": "https:\/\/wappass.baidu.com\/wp\/login\/proxy?u=https%3A%2F%2Fwappass.baidu.com%2F&tpl=wi&ltoken=6db91b0d6da665068313682293d5cc83&lstr=c827s3rYKkstcDkhWQSdxx7kAJfoU8wx2Y7cPguHh9BN3MK3RrA8bLR2WpY%2F%2FAIPLZWnTGIId%2BtOCp2%2FszTF&adapter=&skin=default_v2&clientfrom=&client=&actiontype=1&traceid=1AD17503",
			        "authsid" : "",
			    	"bindToSmsLogin": "",
			        "secret": ""
			    },
			    "traceid": ""
			}
			)
		 */
		okHttpRes = okHttpRes.replace("jsonp1(", "");
		okHttpRes = okHttpRes.replace(")", "");

		// 如果 json 解析出错, 直接输出错误信息
		JSONObject json = JSONObject.parseObject(okHttpRes);
		JSONObject data = json.getJSONObject("data");
		// 最后一步要访问的 URL
		String finalurl = String.format("%s&authsid=%s&fromtype=%s&bindToSmsLogin=", data.get("u"), data.get("authsid"), verifyType); // url
		// 向百度服务器请求
		String okHttpRes2 = null;
		try {
			okHttpRes2 = OkHttpUtil_HTTP客户端工具.getInstance().doGetWithJsonResult(finalurl);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		//List<Cookie> cookieList= OkHttpUtil_HTTP客户端工具.getInstance().getCookie(HttpUrl.parse(finalurl));
		//List<Cookie> cookies = OkHttpUtil_HTTP客户端工具.getCookie(HttpUrl.parse(finalurl));


		return parseCookies(finalurl, okHttpRes2);
	}

	private Map parseCookies(String targetURL, String okHttpRes2) {
		/**
			func (lj *LoginJSON) parseCookies(targetURL string, jar *cookiejar.Jar) {
				url, _ := url.Parse(targetURL)
				cookies := jar.Cookies(url)
				for _, cookie := range cookies {
					switch cookie.Name {
					case "BDUSS":
						lj.Data.BDUSS = cookie.Value
					case "PTOKEN":
						lj.Data.PToken = cookie.Value
					case "STOKEN":
						lj.Data.SToken = cookie.Value
					}
				}
				lj.Data.CookieString = pcsutil.GetURLCookieString(targetURL, jar) // 插入 cookie 字串
			}
		 */

		/**
			<!DOCTYPE html>
			<html>
			<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


			</head>
			<body>
			<script type="text/javascript">

			//如果是ios则通过window.webkit.messageHandlers与客户端交互，同步登录信息给客户端
			var _ua = navigator.userAgent;
			var _isiOS = !!_ua.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);

			setTimeout(function () {
			    //将返回值格式化为json
				if(_isiOS && window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.h5LoginSucceed && window.webkit.messageHandlers.h5LoginSucceed.postMessage){
					var _userName = "yhwzq1981",
						_bdu = "xyNGllS0NpOHA4REc3RC1QNms4aVpYbm5WUlRnMVZzWjB3OEk3MTNycUJ6dTFkRUFBQUFBJCQAAAAAAAAAAAEAAADxbSINeWh3enExOTgxAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIFBxl2BQcZdTE",
						_ptoken = "9ad4d4aec649c811f33f28ec1135d43c" ;

					var _rspJson = {"userName":_userName,"bduss":_bdu,"ptoken":_ptoken};
					window.webkit.messageHandlers.h5LoginSucceed.postMessage(_rspJson);
				} else if (window.passUiWebLoginSucceed) {
					var rspJson = {
						'userName': 'yhwzq1981',
						'bduss': 'xyNGllS0NpOHA4REc3RC1QNms4aVpYbm5WUlRnMVZzWjB3OEk3MTNycUJ6dTFkRUFBQUFBJCQAAAAAAAAAAAEAAADxbSINeWh3enExOTgxAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIFBxl2BQcZdTE',
						'ptoken': '9ad4d4aec649c811f33f28ec1135d43c'
					};
					window.passUiWebLoginSucceed(rspJson);
				};
					var _u = "https://wappass.baidu.com/%3Fuid%3D1573273986215_521%26ssid%3D41e4fd4d42478d1f4def28c1f2a78475.3.1573273985.1.pXWXlPRpXWXl%26traceid%3D";
				document.location = decodeURIComponent(_u);
			}, 0);
			</script>
			</body>
			</html>
		 */
		BaiduDto baiduDto = new BaiduDto();

//		for (int i = 0; i < okHttpRes2.size(); i++) {
//			Cookie cookie = okHttpRes2.get(i);
//			if (cookie.name().equals("BDUSS")) {
//				baiduDto.setBduss(cookie.value());
//			}
//			if (cookie.name().equals("PTOKEN")) {
//				baiduDto.setPtoken(cookie.value());
//			}
//			if (cookie.name().equals("STOKEN")) {
//				baiduDto.setStoken(cookie.value());
//			}
//
		// 取得信息的指定位置
		int i0 = StringUtils.indexOf(okHttpRes2, "'userName':");
		int i1 = StringUtils.indexOf(okHttpRes2, "'bduss':");
		int i2 = StringUtils.indexOf(okHttpRes2, "'ptoken':");

		// 截取其中的Str信息
		String a0 = StringUtils.substring(okHttpRes2, i0+"'userName':".length(), i1-2);
		String a1 = StringUtils.substring(okHttpRes2, i1+"'bduss':".length(), i2-2);
		int i3 = StringUtils.indexOf(okHttpRes2, "};", i2);
		String a2 = StringUtils.substring(okHttpRes2, i2+"'ptoken': ".length(), i3-2);

		// 清理取到的Str
		String userName = StringUtil.清理取到的Str(a0);
		String bduss = StringUtil.清理取到的Str(a1);
		String ptoken = StringUtil.清理取到的Str(a2);

		// 返回解析到的信息
		Map m = new HashMap();
		m.put("userName", userName);
		m.put("bduss", bduss);
		m.put("ptoken", ptoken);
		m.put("errCode", "0");
		return m;
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

	public static void main(String args[]) throws Exception {
//		String imgUrl = "https://wappass.baidu.com/cgi-bin/genimage?"
//				+ "jxG7f07e2152b59c151020d15619801097b6120440615023110";
//
//		System.out.println(下载指定图片并解析_byUrl(imgUrl));

		String s ="			<!DOCTYPE html>\r\n" +
				"			<html>\r\n" +
				"			<head>\r\n" +
				"			<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n" +
				"\r\n" +
				"\r\n" +
				"			</head>\r\n" +
				"			<body>\r\n" +
				"			<script type=\"text/javascript\">\r\n" +
				"\r\n" +
				"			//如果是ios则通过window.webkit.messageHandlers与客户端交互，同步登录信息给客户端\r\n" +
				"			var _ua = navigator.userAgent;\r\n" +
				"			var _isiOS = !!_ua.match(/\\(i[^;]+;( U;)? CPU.+Mac OS X/);\r\n" +
				"\r\n" +
				"			setTimeout(function () {\r\n" +
				"			    //将返回值格式化为json\r\n" +
				"				if(_isiOS && window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.h5LoginSucceed && window.webkit.messageHandlers.h5LoginSucceed.postMessage){\r\n" +
				"					var _userName = \"yhwzq1981\",\r\n" +
				"						_bdu = \"xyNGllS0NpOHA4REc3RC1QNms4aVpYbm5WUlRnMVZzWjB3OEk3MTNycUJ6dTFkRUFBQUFBJCQAAAAAAAAAAAEAAADxbSINeWh3enExOTgxAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIFBxl2BQcZdTE\",\r\n" +
				"						_ptoken = \"9ad4d4aec649c811f33f28ec1135d43c\" ;\r\n" +
				"\r\n" +
				"					var _rspJson = {\"userName\":_userName,\"bduss\":_bdu,\"ptoken\":_ptoken};\r\n" +
				"					window.webkit.messageHandlers.h5LoginSucceed.postMessage(_rspJson);\r\n" +
				"				} else if (window.passUiWebLoginSucceed) {\r\n" +
				"					var rspJson = {\r\n" +
				"						'userName': 'yhwzq1981',\r\n" +
				"						'bduss': 'xyNGllS0NpOHA4REc3RC1QNms4aVpYbm5WUlRnMVZzWjB3OEk3MTNycUJ6dTFkRUFBQUFBJCQAAAAAAAAAAAEAAADxbSINeWh3enExOTgxAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIFBxl2BQcZdTE',\r\n" +
				"						'ptoken': '9ad4d4aec649c811f33f28ec1135d43c'\r\n" +
				"					};\r\n" +
				"					window.passUiWebLoginSucceed(rspJson);\r\n" +
				"				};\r\n" +
				"					var _u = \"https://wappass.baidu.com/%3Fuid%3D1573273986215_521%26ssid%3D41e4fd4d42478d1f4def28c1f2a78475.3.1573273985.1.pXWXlPRpXWXl%26traceid%3D\";\r\n" +
				"				document.location = decodeURIComponent(_u);\r\n" +
				"			}, 0);\r\n" +
				"			</script>\r\n" +
				"			</body>\r\n" +
				"			</html>";
//		ArrayList<String> matchList_TokenAndU =  StringUtil.解析指定️字符串_by正则表达式(s,
//				"setTimeout(function () (.*?), 0);\\r\\n\"");
		int i0 = StringUtils.indexOf(s, "'userName':");
		int i1 = StringUtils.indexOf(s, "'bduss':");
		int i2 = StringUtils.indexOf(s, "'ptoken':");

		String a0 = StringUtils.substring(s, i0+"'userName':".length(), i1-2);
		String a1 = StringUtils.substring(s, i1+"'bduss':".length(), i2-2);
		int i3 = StringUtils.indexOf(s, "};", i2);
		String a2 = StringUtils.substring(s, i2+"'ptoken': ".length(), i3-2);

		String userName = StringUtil.清理取到的Str(a0);
		String bduss = StringUtil.清理取到的Str(a1);
		String ptoken = StringUtil.清理取到的Str(a2);

		BaiduDto baiduDtoBranch = new BaiduDto();

		baiduDtoBranch.setBduss(bduss);
		baiduDtoBranch.setPtoken(ptoken);
		//baiduDtoBranch.setStoken(cookie.value());

	}

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
