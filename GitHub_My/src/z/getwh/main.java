/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.getwh;

//import farmers.getTools;
//import farmers.getmail;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
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

/**
 *
 * @author yangzhen
 */
public class main extends Thread {

    private static util.GetTools.tools _tools = new util.GetTools.tools();
    private static util.GetFile.txt _txt = new util.GetFile.txt();
    private static List _list = new ArrayList();

    private static strclass.data _USDX = new strclass.data();

    public static List _bigmessage = new ArrayList();

    public void run() {
        try {
            fun.ini(_list);
            fun.ini2(_bigmessage, _tools);
            //处理数据

            while (true) {
                if (fun.isOnline()) {
                    jx_url();
                    Thread.sleep(1000 * 60);
                } else {
                    System.out.println("网络不通 等待15s...");
                    Thread.sleep(1000 * 15);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void jx_url() {

        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.0000");
        //java.text.DecimalFormat df2 = new java.text.DecimalFormat("#0.00000");
        StringBuffer br = new StringBuffer();

        if (_list.size() > 0) {

            String systime = _tools.systime_prase_string("时") + ":" + _tools.systime_prase_string("分") + ":" + _tools.systime_prase_string("秒");
            strclass.data _EURUSD = new strclass.data();
            strclass.data _USDJPY = new strclass.data();
            strclass.data _GBPUSD = new strclass.data();
            strclass.data _USDCAD = new strclass.data();
            strclass.data _USDSEK = new strclass.data();
            strclass.data _USDCHF = new strclass.data();
            String[] _ll = null;
            String[] _ll2 = null;
            for (int i = 0; i < _list.size(); i++) {

                _EURUSD = new strclass.data();
                _USDJPY = new strclass.data();
                _GBPUSD = new strclass.data();
                _USDCAD = new strclass.data();
                _USDSEK = new strclass.data();
                _USDCHF = new strclass.data();

                String _url = _list.get(i).toString();
                if (_url.length() > 0) {
                    _url = fun.removeWwwFromUrl(_url);
                    try {
                        //获取网页代码
                        String pageContents = "";
                        pageContents = fun.getWebData(_url, "utf-8");
                        //pageContents = fun.getURLContent(_url, "utf-8");
                        String sp = pageContents;

                        _ll = null;
                        _ll = sp.split("\\n");//按换行截取

                        double sum = 50.14348112;

                        if (_ll.length > 0) {
                            for (int i1 = 0; i1 < _ll.length; i1++) {
                                String out = "";
                                String sp2 = _ll[i1].toString();
                                _ll2 = sp2.split(",");
                                if (_ll2.length > 1) {//格式正确
                                    String name = _ll2[0].toString().replace("=x", "").replace("\"", "");
                                    String value = _ll2[1].toString();
                                    String time = systime;
                                    if (name.equals("EURUSD")) {
                                        _EURUSD.NAME = name;
                                        _EURUSD.VALUE = value;
                                        _EURUSD.TIME = time;
                                        double sum_l = Double.parseDouble(_EURUSD.VALUE);
                                        sum = sum * Math.pow(sum_l, -0.576);
                                        sum = Double.parseDouble(df.format(sum));
                                    } else if (name.equals("USDJPY")) {
                                        _USDJPY.NAME = name;
                                        _USDJPY.VALUE = value;
                                        _USDJPY.TIME = time;
                                        double sum_l = Double.parseDouble(_USDJPY.VALUE);
                                        sum = sum * Math.pow(sum_l, 0.136);
                                        sum = Double.parseDouble(df.format(sum));
                                    } else if (name.equals("GBPUSD")) {
                                        _GBPUSD.NAME = name;
                                        _GBPUSD.VALUE = value;
                                        _GBPUSD.TIME = time;
                                        double sum_l = Double.parseDouble(_GBPUSD.VALUE);
                                        sum = sum * Math.pow(sum_l, -0.119);
                                        sum = Double.parseDouble(df.format(sum));
                                    } else if (name.equals("USDCAD")) {
                                        _USDCAD.NAME = name;
                                        _USDCAD.VALUE = value;
                                        _USDCAD.TIME = time;
                                        double sum_l = Double.parseDouble(_USDCAD.VALUE);
                                        sum = sum * Math.pow(sum_l, 0.091);
                                        sum = Double.parseDouble(df.format(sum));
                                    } else if (name.equals("USDSEK")) {
                                        _USDSEK.NAME = name;
                                        _USDSEK.VALUE = value;
                                        _USDSEK.TIME = time;
                                        double sum_l = Double.parseDouble(_USDSEK.VALUE);
                                        sum = sum * Math.pow(sum_l, 0.042);
                                        sum = Double.parseDouble(df.format(sum));
                                    } else if (name.equals("USDCHF")) {
                                        _USDCHF.NAME = name;
                                        _USDCHF.VALUE = value;
                                        _USDCHF.TIME = time;
                                        double sum_l = Double.parseDouble(_USDCHF.VALUE);
                                        sum = sum * Math.pow(sum_l, 0.036);
                                        sum = Double.parseDouble(df.format(sum));
                                    }
                                } else {
                                    System.out.println("未识别:" + sp2);
                                }
                            }

                            boolean _issendok = false;

                            if (_USDX.VALUE != null && _USDX.VALUE.length() > 0) {
                                _USDX.NAME = "USDX";
                                _USDX.TIME = systime;
                                double datanew = Double.parseDouble(df.format(sum));
                                double dataold = Double.parseDouble(df.format(Double.parseDouble(_USDX.VALUE)));
                                double sum_updown = (datanew - dataold);
                                BigDecimal pd = new BigDecimal(sum_updown);
                                int r = pd.compareTo(BigDecimal.ZERO); //和0，Zero比较
                                if (r >= 0) {  //0  等于0   1大于0
                                    _USDX.UPDOWN = "+" + df.format(sum_updown);
                                    _issendok = fun.sendmail(pd, _USDX.UPDOWN);
                                } else {  //   -1小于0
                                    _USDX.UPDOWN = df.format(sum_updown);
                                    _issendok = fun.sendmail(pd, _USDX.UPDOWN);
                                }
                                _USDX.VALUE = df.format(sum);
                            } else {
                                _USDX.NAME = "USDX";
                                _USDX.TIME = systime;
                                _USDX.VALUE = df.format(sum);
                            }

                            br = new StringBuffer();
                            br.append(_USDX.TIME);
                            br.append(" ").append("UX:").append(_USDX.VALUE);
                            br.append(" ").append("").append(_USDX.UPDOWN);
                            //br.append(" ").append("E/U:").append(_EURUSD.VALUE);
                            //br.append(" ").append("U/J:").append(_USDJPY.VALUE);
                            if (_issendok) {
                                br.append(" ").append("SendMail");
                            }

                            System.out.println(br.toString());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }

    }

}
