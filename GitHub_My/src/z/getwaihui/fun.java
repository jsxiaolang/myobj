/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.getwaihui;

import java.io.BufferedInputStream;
import z.pro.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
 *
 * @author Administrator
 */
public class fun {

    public static List _list_main_key = new ArrayList();

    public static boolean isOnline() {
        boolean bs = false;
        URL url = null;
        try {
            url = new URL("http://www.baidu.com/");
            InputStream in = url.openStream();
            bs = true;
            in.close();
        } catch (Exception e) {

        }
        return bs;
    }

    public static String getURLContent(String url, String encoding) {
        StringBuffer content = new StringBuffer();
        try {
            // 新建URL对象
            URL u = new URL(url);
            InputStream in = new BufferedInputStream(u.openStream());
            InputStreamReader theHTML = new InputStreamReader(in, encoding);
            int c;
            while ((c = theHTML.read()) != -1) {
                content.append((char) c);
            }
        } // 处理异常
        catch (Exception e) {
            System.err.println(e);
        }
        return content.toString();
    }

    // 从URL中去掉"www"
    public static String removeWwwFromUrl(String url) {
        int index = url.indexOf("://www.");
        if (index != -1) {
            return url.substring(0, index + 3)
                    + url.substring(index + 7);
        }
        return (url);
    }

    public static class news {

        public String title = "";
        public String url = "";
    }

    public static boolean guolv(news _news) {
        boolean _bs = true;

        if (_list_main_key.size() > 0) {
            for (int i = 0; i < _list_main_key.size(); i++) {
                String key = _list_main_key.get(i).toString();
                if (_news.title.indexOf(key) != -1) {
                    _bs = false;
                    break;
                }
            }
        }

        if (_news.title.length() < 10) {
            _bs = false;
        }

        if (_news.title.length() == 0 || _news.url.length() == 0) {
            _bs = false;
        }

        return _bs;
    }

    public static void mainkey() {
        _list_main_key.add("<img");
        _list_main_key.add("ICP证");
        _list_main_key.add("经营许可证");
        _list_main_key.add("举报热线");
        _list_main_key.add("减肥");
        _list_main_key.add("诚信网站");
        _list_main_key.add("\"");
    }


    private static void open_ie(util.GetFile.txt _txt, String mes) {

        String file_path = "D:\\getwaihui.html";
        boolean _bs = _txt.create_file(file_path);
        if (_bs) {
            _txt.write_file(file_path, mes.toString());
            try {
                java.awt.Desktop.getDesktop().open(new File(file_path));
            } catch (Exception ex) {
                System.out.println("调用默认程序打开程序失败:" + ex.getMessage());
            }
        }

    }

    public static String getWebData(String strurl, String code) {
        // 初始化，此处构造函数就与3.1中不同  
        String charSet = "";

        //创建HttpClientBuilder  
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //HttpClient  
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

        HttpGet httpGet = new HttpGet(strurl);
        try {
            //执行get请求  
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            //获取响应消息实体  
            HttpEntity entity = httpResponse.getEntity();
            //响应状态  
            //System.out.println("status:" + httpResponse.getStatusLine());
            //判断响应实体是否为空  
            if (entity != null) {
                //System.out.println("contentEncoding:" + entity.getContentEncoding());
                //System.out.println("response content:" + EntityUtils.toString(entity));
                byte[] bytes = EntityUtils.toByteArray(entity);
                charSet = new String(bytes, code);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                closeableHttpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //System.out.println(charSet);
        return charSet;
    }

}
