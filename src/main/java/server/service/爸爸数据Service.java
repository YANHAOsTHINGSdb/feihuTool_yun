package server.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import server.pcs.PCSUploadUtils;

public class 爸爸数据Service {

	 爸爸数据Service() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	 public String 取得系统时间_文件名() {
		// 取得系统时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");//设置日期格式
		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		return df.format(new Date());
	}

	 public void 存储到百度网盘(String s存储对象文件全路径, String s网盘出力文件全路径) {

		new PCSUploadUtils().multiFileUpload(s存储对象文件全路径, s网盘出力文件全路径);
	}
}
