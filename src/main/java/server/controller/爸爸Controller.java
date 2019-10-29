package server.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

public class 爸爸Controller {

	public 爸爸Controller() {

	}

	public void 将指定文件提供下载_byHttpServletResponse_文件全路径(HttpServletResponse response, String s文件全路径) {
	    //设置响应头和客户端保存文件名
	    response.setCharacterEncoding("utf-8");
	    response.setContentType("multipart/form-data");
	    response.setHeader("Content-Disposition", "attachment;fileName=" + s文件全路径);

	    //用于记录以完成的下载的数据量，单位是byte
        try {
        	//激活下载操作
            OutputStream output = response.getOutputStream();
            BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);

            //打开本地文件流
            BufferedInputStream fIn=new BufferedInputStream(new FileInputStream(s文件全路径));
    		int len=0;
    		byte[] buf=new byte[1024];

    		//循环写入输出流
    		int i=0;
    		while((len=fIn.read(buf))!=-1)
    		{
    			bufferedOutPut.write(buf,0,len);
    			System.out.println(++i);
    		}
            bufferedOutPut.flush();

            // 这里主要关闭。
            bufferedOutPut.close();
            fIn.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
