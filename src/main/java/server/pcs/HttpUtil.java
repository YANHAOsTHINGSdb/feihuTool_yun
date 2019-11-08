package server.pcs;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpUtil
{
    public static boolean returnResponseOfUrl(String url, OutputStream os)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try{
            response = httpclient.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if(statusLine != null && statusLine.getStatusCode() >= 300){
                throw new HttpResponseException(statusLine.getStatusCode(),
                                                statusLine.getReasonPhrase());
            }
            if(entity == null){
                throw new ClientProtocolException("response contains no content");
            }

            entity.writeTo(os);
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }finally{
            if(response != null){
                try{
                    response.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
