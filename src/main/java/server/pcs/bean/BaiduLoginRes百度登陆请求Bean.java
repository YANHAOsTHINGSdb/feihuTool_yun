package server.pcs.bean;

import lombok.Data;

/**
 * 百度登陆请求信息
 * @author haoyan
 *
 */
/**
type LoginJSON struct {
	ErrInfo struct {
		No  string `json:"no"`
		Msg string `json:"msg"`
	} `json:"errInfo"`
	Data struct {
		CodeString   string `json:"codeString"`
		GotoURL      string `json:"gotoUrl"`
		Token        string `json:"token"`
		U            string `json:"u"`
		AuthSID      string `json:"authsid"`
		Phone        string `json:"phone"`
		Email        string `json:"email"`
		BDUSS        string `json:"bduss"`
		PToken       string `json:"ptoken"`
		SToken       string `json:"stoken"`
		CookieString string `json:"cookieString"`
	} `json:"data"`
}
 *
 */
@Data
public class BaiduLoginRes百度登陆请求Bean {

	String serverTime;

	String codeString;
	String gotoUrl;
	String token;
	String u;
	String authSID;


	String checkPhone; // 验证用手机号
	String checkEmail; // 验证用Email
	String bduss;
	String ptoken;
	String stoken;
	String cookieString;

	String bcsn;
	String bcsync;
	String bcchecksum;
	String bctime;
	String phone;	//普通登录，手机登录值为1
	String userid;

	String appealurl;
	String second_u;
	String ppU;
	String realnameswitch;
	String errCode;
	String errMsg;

	public BaiduLoginRes百度登陆请求Bean(){

	}

	public BaiduLoginRes百度登陆请求Bean(String errCode,String errMsg){
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public BaiduLoginRes百度登陆请求Bean(BaiduDto baiduDto){
		this.errCode = "0";
		this.bduss = baiduDto.getBduss();
		this.ptoken = baiduDto.getPtoken();
		this.userid = baiduDto.getUID();
	}


	@Override
	public String toString() {
		return "BaiduLoginRes [u=" + u + ", serverTime=" + serverTime + ", codeString=" + codeString + ", bduss="
				+ bduss + ", ptoken=" + ptoken + ", bcsn=" + bcsn + ", bcsync=" + bcsync + ", bcchecksum=" + bcchecksum
				+ ", bctime=" + bctime + ", gotoUrl=" + gotoUrl + ", userid=" + userid + ", phone=" + phone
				+ ", appealurl=" + appealurl + ", second_u=" + second_u + ", ppU=" + ppU + ", realnameswitch="
				+ realnameswitch + ", errCode=" + errCode + ", errMsg=" + errMsg + "]";
	}
}
