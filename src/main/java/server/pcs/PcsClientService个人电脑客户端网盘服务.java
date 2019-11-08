package server.pcs;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import server.pcs.bean.BaiduClientStore;
import server.pcs.bean.BaiduDto;
import server.pcs.util.OkHttpUtil_HTTP客户端工具;

/**
 * 个人电脑客户端文件服务
 * @author haoyan
 *
 */
public class PcsClientService个人电脑客户端网盘服务 {
	public static final Boolean isHTTPS = false;
	public static final int defaultAppID = 266719;
	public static String cookieStr = "";
	static Charset UTF_8 = Charset.forName("UTF-8");

	/**
	 * 取得PCS路径下信息_byPath
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public String 取得PCS路径下信息_byPath(String path) throws IOException {
		if ("".equals(path)) {
			path = "/";
		}

		String subPath = "file";
		String method = "list";
		String url = "://pcs.baidu.com/rest/2.0/pcs/" + subPath;
		if (isHTTPS) {
			url = "https" + url;
		} else {
			url = "http" + url;
		}
		url = url + "?app_id=" + defaultAppID + "&method=" + method + "&path=" + path + "&by=" + "name" + "&order="
				+ "asc" + "&limit=" + "0-2147483647";
		String okHttpRes = OkHttpUtil_HTTP客户端工具.getInstance().doGetWithJsonResult(url);
		return okHttpRes;
	}

//	public void downloadFile(String path) {
//		if ("".equals(path)) {
//			return;
//		}
//		String fileName = path.substring(path.lastIndexOf("/")+1);
//		if(StringUtil.isEmpty(fileName))return;
//		String subPath = "file";
//		String method = "download";
//		String url = "://pcs.baidu.com/rest/2.0/pcs/" + subPath;
//		if (isHTTPS) {
//			url = "https" + url;
//		} else {
//			url = "http" + url;
//		}
//		url = url + "?app_id=" + defaultAppID + "&method=" + method + "&path=" + path;
//		try {
//			// 准备多线程下载
//			SiteInfoBean bean = new SiteInfoBean(url,Constant.localDownloadPath, fileName, Constant.maxDownloadThread);
//			SiteFileFetch fileFetch = new SiteFileFetch(bean);
//			fileFetch.run();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public SiteFileFetchInter downloadFile(String path, String localPath) {
//		if ("".equals(path)) {
//			return null;
//		}
//		String fileName = localPath.substring(localPath.lastIndexOf("/")+1);
//		localPath = localPath.substring(0, localPath.lastIndexOf("/"));
//		if(StringUtil.isEmpty(fileName))return null;
//		String subPath = "file";
//		String method = "download";
//		String url = "://pcs.baidu.com/rest/2.0/pcs/" + subPath;
//		if (isHTTPS) {
//			url = "https" + url;
//		} else {
//			url = "http" + url;
//		}
//		url = url + "?app_id=" + defaultAppID + "&method=" + method + "&path=" + path;
//		try {
//			SiteInfoBean bean = new SiteInfoBean(url,localPath, fileName, Constant.maxDownloadThread);
//			SiteFileFetchThread fileFetch = new SiteFileFetchThread(bean,true);
//			fileFetch.start();
//			return fileFetch;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public List<PcsFileDto个人电脑客户端文件信息> filesDirectoriesMeta(String path) {
//		if ("".equals(path)) {
//			path = "/";
//		}
//
//		String subPath = "file";
//		String method = "meta";
//		String url = "://pcs.baidu.com/rest/2.0/pcs/" + subPath;
//		if (isHTTPS) {
//			url = "https" + url;
//		} else {
//			url = "http" + url;
//		}
//		url = url + "?app_id=" + defaultAppID + "&method=" + method;
//		RequestBody formBody = null;
//
//		Map<String, String> body = new HashMap<String, String>();
//		body.put("path", path);
//		FormBody.Builder formEncodingBuilder = new FormBody.Builder(UTF_8);
//		for (String key : body.keySet()) {
//			formEncodingBuilder.add(key, body.get(key));
//		}
//		formBody = formEncodingBuilder.build();
//		String okHttpRes = "";
//		try {
//			okHttpRes = OkHttpUtil_HTTP客户端工具.getInstance().doPostWithBodyAndHeader(url, formBody);
//		} catch (Exception e) {
//			return null;
//		}
//		JSONObject json = JSONObject.parseObject(okHttpRes);
//		if (json == null)
//			return null;
//		List<PcsFileDto个人电脑客户端文件信息> pcsFileDtos = new ArrayList<PcsFileDto个人电脑客户端文件信息>();
//		JSONArray ja = json.getJSONArray("list");
//		for (Object tmp : ja) {
//			JSONObject jobj = (JSONObject) tmp;
//			PcsFileDto个人电脑客户端文件信息 pcsFileDto = (PcsFileDto个人电脑客户端文件信息) JSONObject.toJavaObject(jobj, PcsFileDto个人电脑客户端文件信息.class);
//			pcsFileDtos.add(pcsFileDto);
//		}
//		return pcsFileDtos;
//	}

	// 填写cookie
	public void init(BaiduDto baiduDto) {
		Map<String, String> cookie = new HashMap<String, String>();
		cookie.put("BDUSS", baiduDto.getBduss());
		OkHttpUtil_HTTP客户端工具.addCookie("http://pcs.baidu.com", cookie);
		OkHttpUtil_HTTP客户端工具.addCookie("http://pan.baidu.com", cookie);
	}


	public void 做成OkHttpClient_byBDUSS(BaiduDto baiduDto) {
		//  填写表格
		BaiduClientStore.bdClients.put(baiduDto.getUID(), baiduDto);
		BaiduClientStore.currentActiveUid = baiduDto.getUID();
		BaiduClientStore.currentActiveBaiduDto = baiduDto;
		// 填写cookie
		init(baiduDto);
	}

}
