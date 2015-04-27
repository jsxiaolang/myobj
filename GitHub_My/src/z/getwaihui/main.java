/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.getwaihui;

//import farmers.getTools;
//import farmers.getmail;
import z.getnews.*;
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

    public void run() {
        try {
            ini(_list);
            //处理数据
            while (true) {
                if (fun.isOnline()) {
                    jx_url(_list);
                    Thread.sleep(1000 * 10);
                } else {
                    System.out.println("网络不通 等待60s...");
                    Thread.sleep(1000 * 60);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ini(List _list) {
        _list.add("http://download.finance.yahoo.com/d/quotes.csv?e=.csv"
                + "&s="
                + "EURUSD=x,"
                + "USDJPY=x,"
                + "GBPUSD=x,"
                + "USDCAD=x,"
                + "USDSEK=x,"
                + "USDCHF=x"
                + "&f=sl1d1t1");

        //USDX = 50.14348112 × EURUSD(-0.576) × USDJPY(0.136) × GBPUSD(-0.119) × USDCAD(0.091) × USDSEK(0.042) × USDCHF(0.036);
    }

    private static void jx_url(List _list) {

        if (_list.size() > 0) {

            String systime = _tools.systime_prase_string("");
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

                        for (int i1 = 0; i1 < _ll.length; i1++) {
                            String out = "";
                            String sp2 = _ll[i1].toString();
                            _ll2 = sp2.split(",");
                            String name = _ll2[0].toString().replace("=x", "").replace("\"", "");
                            String value = _ll2[1].toString();
                            String time = systime;
                            if (name.equals("EURUSD")) {
                                _EURUSD.NAME = name;
                                _EURUSD.VALUE = value;
                                _EURUSD.TIME = time;
                                double sum_l = Double.parseDouble(_EURUSD.VALUE);
                                sum = sum * Math.pow(sum_l, -0.576);
                            } else if (name.equals("USDJPY")) {
                                _USDJPY.NAME = name;
                                _USDJPY.VALUE = value;
                                _USDJPY.TIME = time;
                                double sum_l = Double.parseDouble(_USDJPY.VALUE);
                                sum = sum * Math.pow(sum_l, 0.136);
                            } else if (name.equals("GBPUSD")) {
                                _GBPUSD.NAME = name;
                                _GBPUSD.VALUE = value;
                                _GBPUSD.TIME = time;
                                double sum_l = Double.parseDouble(_GBPUSD.VALUE);
                                sum = sum * Math.pow(sum_l, -0.119);
                            } else if (name.equals("USDCAD")) {
                                _USDCAD.NAME = name;
                                _USDCAD.VALUE = value;
                                _USDCAD.TIME = time;
                                double sum_l = Double.parseDouble(_USDCAD.VALUE);
                                sum = sum * Math.pow(sum_l, 0.091);
                            } else if (name.equals("USDSEK")) {
                                _USDSEK.NAME = name;
                                _USDSEK.VALUE = value;
                                _USDSEK.TIME = time;
                                double sum_l = Double.parseDouble(_USDSEK.VALUE);
                                sum = sum * Math.pow(sum_l, 0.042);
                            } else if (name.equals("USDCHF")) {
                                _USDCHF.NAME = name;
                                _USDCHF.VALUE = value;
                                _USDCHF.TIME = time;
                                double sum_l = Double.parseDouble(_USDCHF.VALUE);
                                sum = sum * Math.pow(sum_l, 0.036);
                            }
                        }
                        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.0000");
                        java.text.DecimalFormat df2 = new java.text.DecimalFormat("#0.00");

                        if (_USDX.VALUE != null && _USDX.VALUE.length() > 0) {
                            _USDX.NAME = "USDX";
                            _USDX.TIME = systime;
                            _USDX.VALUE = df.format(sum);

                            double sum_updown = (Double.parseDouble(df.format(sum)) - Double.parseDouble(_USDX.VALUE)) / Double.parseDouble(_USDX.VALUE);
                            BigDecimal pd = new BigDecimal(sum_updown);
                            int r = pd.compareTo(BigDecimal.ZERO); //和0，Zero比较
                            if (r >= 0) {  //0  等于0   1大于0
                                _USDX.UPDOWN = "+" + df2.format(sum_updown);
                                sendmail(pd, _USDX.UPDOWN);
                            } else {  //   -1小于0
                                _USDX.UPDOWN = df2.format(sum_updown);
                                sendmail(pd, _USDX.UPDOWN);
                            }

                        } else {
                            _USDX.NAME = "USDX";
                            _USDX.TIME = systime;
                            _USDX.VALUE = df.format(sum);
                        }

                        StringBuffer br = new StringBuffer();
                        br.append(_USDX.TIME);
                        br.append(" ").append("USDX:").append(_USDX.VALUE);
                        br.append(" ").append("").append(_USDX.UPDOWN);
                        // br.append(" ").append("E/U:").append(_EURUSD.VALUE);
                        // br.append(" ").append("U/J:").append(_USDJPY.VALUE);

                        System.out.println(br.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }

    }

    private static void sendmail(BigDecimal pd, String UPDOWN) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.0000");
        java.text.DecimalFormat df2 = new java.text.DecimalFormat("#0.00");

        boolean _bs = false;

        //------------------判断是否发邮件------------------//
        double sum_2 = Double.parseDouble("0.01");
        BigDecimal pd2 = new BigDecimal(sum_2);
        int r2 = pd.compareTo(pd2);
        if (r2 >= 0) {  // 表示 >=0.01
            _bs = true;
        }
        //------------------判断是否发邮件------------------//

        //------------------判断是否发邮件------------------//
        double sum_3 = Double.parseDouble("-0.01");
        BigDecimal pd3 = new BigDecimal(sum_3);
        int r3 = pd.compareTo(pd3);
        if (r3 < 0) {  // 表示 <-0.01
            _bs = true;
        }
        //------------------判断是否发邮件------------------//

        if (_bs) {
            util.GetMail.qqsmtp _qqsmtp = new util.GetMail.qqsmtp();
            util.GetMail.qqsmtp.mailmessage _mailmessage = new util.GetMail.qqsmtp.mailmessage();

            _mailmessage.smtp = "smtp.qq.com";
            _mailmessage.from = "11941450@qq.com";
            _mailmessage.to = "18605236780@wo.cn";
            _mailmessage.copyto = "";
            _mailmessage.subject = "USDX提醒:" + UPDOWN;
            _mailmessage.content = "USDX提醒:" + UPDOWN;
            _mailmessage.username = "11941450";
            _mailmessage.password = "txyz5011";
            _mailmessage.filename = "";
            _qqsmtp.send(_mailmessage);
        }
    }
}
