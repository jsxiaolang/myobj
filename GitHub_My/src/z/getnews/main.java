/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.getnews;

//import farmers.getTools;
//import farmers.getmail;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import z.pro.fun;

/**
 *
 * @author yangzhen
 */
public class main extends Thread {

    private static util.GetTools.tools _tools = new util.GetTools.tools();
    private static util.GetFile.txt _txt = new util.GetFile.txt();
    public static List _list = new ArrayList();
    public static List _list_main_key = new ArrayList();

    public void run() {
        try {

            ini();
            //加载关键字过滤
            mainkey();
            //处理数据
            jx_url(_list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void jx_url(List _list) {

        StringBuffer _str = new StringBuffer();
        _str.append("<html><head><meta charset=\"utf-8\"></head><body>");
        String _body = "";
        if (_list.size() > 0) {
            news _news = new news();
            for (int i = 0; i < _list.size(); i++) {
                String _url_mes = _list.get(i).toString();
                String[] _ll = _url_mes.split("\\$\\$");

                String _url = _ll[0].toString();
                String _bm = _ll[1].toString();

                _str.append("<br/><br/><br/>########网站：" + _url + "  " + "<br/>");
                if (_url.length() > 0) {
                    _url = removeWwwFromUrl(_url);

                    String pageContents = "";
                    try {
                        //获取网页代码
                        pageContents = getWebData(_url, _bm);
                        //pageContents = getURLContent(_url, _bm);
                    } catch (Exception e) {
                        continue;
                    }

                    if (pageContents.length() == 0) {
                        continue;
                    }

                    List _listnews = getURLContentFromPattern(pageContents);
                    if (_listnews.size() > 0) {

                        for (int i2 = 0; i2 < _listnews.size(); i2++) {
                            _news = new news();
                            _news = (news) _listnews.get(i2);
                            if (_news.title.toString().length() < 20) {
                                continue;
                            }
                            _str.append(_url + "[" + (i2 + 1) + "]" + ":  " + "<a href=\"" + _news.url.toString() + "\" target=\"_blank\" >" + _news.title.toString() + "</a><br/>");
                        }
                    }
                }
            }
        }

        if (_str.length() > 0) {

            _str.append("</body></html>");
            _body = _str.toString();

            String file_path = "D:\\getnews.html";
            boolean _bs = _txt.create_file(file_path);
            if (_bs) {
                _txt.write_file(file_path, _str.toString());
                try {
                    java.awt.Desktop.getDesktop().open(new File(file_path));
                } catch (Exception ex) {
                    System.out.println("调用默认程序打开程序失败:" + ex.getMessage());
                }
            }

        }
    }

    private static List getURLContentFromPattern(String contents) {
        List _listnews = new ArrayList();
        contents = contents.replace("  ", " ");
        //System.out.println(contents);
        String[] messages = contents.split("<a");

        for (int i = 1, m = messages.length - 1; i < m; i++) {
            news _news = new news();
            String kk = messages[i].toString();
            String hh = "<a " + kk;
            String[] messages2 = hh.split("</a>");
            String jj = messages2[0].toString() + "</a>";

            if (jj.indexOf("href=") == -1) {
                continue;
            }
            jj = jj.replace("\\", "").replace("'", "\"");

            //解析URL
            String[] jx1 = jj.split("href=\"");
            if (jx1.length >= 2) {
                String jx1_1 = jx1[1].toString();
                String[] jx2 = jx1_1.split("\"");
                if (jx2.length >= 1) {
                    String jx2_1 = jx2[0].toString();
                    _news.url = jx2_1;
                    if (jx2_1.length() < 5) {
                        continue;
                    }
                }
            }

            //解析TITLE
            String[] jx3 = jj.split("</a>");
            if (jx3.length >= 1) {
                String jx3_1 = jx3[0].toString();
                String[] jx4 = jx3_1.split(">");
                if (jx4.length >= 2) {
                    String jx4_1 = jx4[1].toString();
                    _news.title = jx4_1;
                }
            }

            if (_news.url.toString().length() < 5) {
                continue;
            }

            if (_news.title.toString().length() < 5) {
                continue;
            }

            //添加到数组
            if (guolv(_news)) {
                _listnews.add(_news);
            }
        }
        return _listnews;
    }

    private static String getURLContent(String url, String encoding) {
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
    private static String removeWwwFromUrl(String url) {
        int index = url.indexOf("://www.");
        if (index != -1) {
            return url.substring(0, index + 3)
                    + url.substring(index + 7);
        }
        return (url);
    }

    private static class news {

        public String title = "";
        public String url = "";
    }

    private static boolean guolv(news _news) {
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

    private static void mainkey() {
        _list_main_key.add("<img");
        _list_main_key.add("ICP证");
        _list_main_key.add("经营许可证");
        _list_main_key.add("举报热线");
        _list_main_key.add("减肥");
        _list_main_key.add("诚信网站");
        _list_main_key.add("\"");
    }

    private static void ini() {
        _list.add("http://finance.sina.com.cn/forex/$$UTF-8");
        _list.add("http://www.ifeng.com/$$UTF-8");
        _list.add("http://www.csdn.net/$$UTF-8");
    }

    /*
     private static void send_mail(String title, String mes) {

     getmail.mailmessage _mail = new getmail.mailmessage();
     _mail.subject = title;
     _mail.content = mes;
     _mail.to = "306247911@qq.com";
     boolean _bs = getmail.sendmain(_mail);
     if (_bs) {
     System.out.println("邮件发送成功");
     } else {
     System.out.println("邮件发送失败");
     }

     }
     */
    private static String getWebData(String strurl, String code) {
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
            System.out.println("status:" + httpResponse.getStatusLine());
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
