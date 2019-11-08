package server.pcs;

import java.io.File;
import java.io.IOException;

//import io.itit.itf.okhttp.Response;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import server.pcs.bean.BaiduDto;


public class PCSUploadUtils {

	public PCSUploadUtils() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static void main(String[] args) {

		String loaclFilePath = "/Users/haoyan/Desktop/架构说明.txt";
		String serverFilePath = "/apps/架构说明.txt";

		new PCSUploadUtils().multiFileUpload(loaclFilePath, serverFilePath);
	}

	public static final MediaType MIXED = MediaType.parse("multipart/mixed");
	public static final MediaType ALTERNATIVE = MediaType.parse("multipart/alternative");
	public static final MediaType DIGEST = MediaType.parse("multipart/digest");
	public static final MediaType PARALLEL = MediaType.parse("multipart/parallel");
	public static final MediaType FORM = MediaType.parse("multipart/form-data");
    /**
     * 使用okhttp-utils上传多个或者单个文件
     */

	/**
	 *
	 * @param loaclFilePath = "/Users/haoyan/Desktop/架构说明.txt"
	 * @param serverFilePath = "/apps/架构说明.txt"
	 */
    public void multiFileUpload(String loaclFilePath, String serverFilePath)
    {
    	String sBDUSS = 取得BDUSS();
    	File file = new File(loaclFilePath);

		String subPath = "file";
		String url = "http://pcs.baidu.com/rest/2.0/pcs/" + subPath;
		url = url + "?app_id=266719" +  "&method=upload" + "&path=" + serverFilePath;

    	OkHttpClient okHttpClient = new OkHttpClient();

    	RequestBody requestBody = new MultipartBody.Builder()
    		    .setType(FORM)
    		    .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), file))
    		    .build();

    	Request request = new Request.Builder()
    	    .url(url)
    	    .post(requestBody)
    	    .header("Cookie", "BDUSS=" + sBDUSS)
    	    .build();

    	Call call = okHttpClient.newCall(request);
    	try {
    	    Response response = call.execute();
    	    System.out.println(response.body().string());
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    }
    /**
     * 取得BDUSS
     * 信息记录在PcsLogin.txt中
     * @return
     */
	private String 取得BDUSS() {
		BaiduDto baiduDto = null;
		try {
			baiduDto =ConfigLoader.加载百度Pcs登陆Info();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return baiduDto.getBduss();
	}
}
