/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.getwh;

import java.io.BufferedInputStream;
import z.pro.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
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
 *
 */
public class fun {

    public static List _list_main_key = new ArrayList();

    public static boolean isOnline() {
        boolean bs = false;

        String strurl = "www.baidu.com";
        String code = "";
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
                System.out.println("run error:" + ex.getMessage());
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

    public static boolean sendmail(BigDecimal pd, String UPDOWN, util.GetFile.xmlconf _xmlconf) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.0000");
        boolean _issendok = false;
        boolean _bs = false;
        boolean _bs2 = false;

        //------------------判断是否发邮件------------------//
        //double sum_2 = Double.parseDouble("0.03");
        BigDecimal pd2 = new BigDecimal(Double.parseDouble(_xmlconf.getvalue("WH", "VALUE1")));
        int r2 = pd.compareTo(pd2);
        if (r2 >= 0) {  // 表示 >=0.03
            _bs = true;
        }
        //------------------判断是否发邮件------------------//

        //------------------判断是否发邮件------------------//
        //double sum_3 = Double.parseDouble("-0.03");
        BigDecimal pd3 = new BigDecimal(Double.parseDouble(_xmlconf.getvalue("WH", "VALUE2")));
        int r3 = pd.compareTo(pd3);
        if (r3 < 0) {  // 表示 <-0.03
            _bs = true;
        }
        //------------------判断是否发邮件------------------//

        //------------------判断是否发邮件------------------//
        String _sendmail_conf = _xmlconf.getvalue("WH", "SENDMAIL");
        if (!_sendmail_conf.equals("false")) {
            _bs2 = true;
        } else {
            _bs2 = false;
        }
        //------------------判断是否发邮件------------------//

        if (_bs) {
            if (_bs2) {
                util.GetMail.qqsmtp _qqsmtp = new util.GetMail.qqsmtp();
                util.GetMail.qqsmtp.mailmessage _mailmessage = new util.GetMail.qqsmtp.mailmessage();

                _mailmessage.smtp = "smtp.qq.com";
                _mailmessage.from = "11941450@qq.com";
                _mailmessage.to = "18605236780@wo.cn";
                _mailmessage.copyto = "";
                _mailmessage.subject = "UX:" + UPDOWN;
                _mailmessage.content = "UX:" + UPDOWN;
                _mailmessage.username = "11941450";
                _mailmessage.password = "txyz5011";
                _mailmessage.filename = "";

                try {
                    _qqsmtp.send(_mailmessage);
                    _issendok = true;
                } catch (Exception ex) {
                    //公司邮箱补发
                    try {
                        util.GetMail.tidesmtp _tidesmtp = new util.GetMail.tidesmtp();
                        util.GetMail.tidesmtp.mailmessage _mailmessage2 = new util.GetMail.tidesmtp.mailmessage();
                        _mailmessage2.smtp = "Smtpcom.263xmail.com";
                        _mailmessage2.from = "zhenyang@tidestonesoft.com";
                        _mailmessage2.to = "18605236780@wo.cn";
                        _mailmessage2.copyto = "";
                        _mailmessage2.subject = "UX:" + UPDOWN;
                        _mailmessage2.content = "UX:" + UPDOWN;
                        _mailmessage2.username = "zhenyang@tidestonesoft.com";
                        _mailmessage2.password = "txyz5011";
                        _mailmessage2.filename = "";
                        _tidesmtp.send(_mailmessage2);
                        _issendok = true;
                    } catch (Exception ex2) {
                        System.out.println("sendmail  false：\r\n" + ex.getMessage() + "\r\n" + ex2.getMessage());
                    }
                }
            }
        }
        return _issendok;
    }

    public static boolean sendmail2(String message, util.GetFile.xmlconf _xmlconf) {
        boolean _issendok = false;
        boolean _bs2 = false;

        //------------------判断是否发邮件------------------//
        String _sendmail_conf = _xmlconf.getvalue("WH", "SENDMAIL");
        if (!_sendmail_conf.equals("false")) {
            _bs2 = true;
        } else {
            _bs2 = false;
        }
        //------------------判断是否发邮件------------------//     
            if (_bs2) {
                util.GetMail.qqsmtp _qqsmtp = new util.GetMail.qqsmtp();
                util.GetMail.qqsmtp.mailmessage _mailmessage = new util.GetMail.qqsmtp.mailmessage();

                _mailmessage.smtp = "smtp.qq.com";
                _mailmessage.from = "11941450@qq.com";
                _mailmessage.to = "18605236780@wo.cn";
                _mailmessage.copyto = "";
                _mailmessage.subject = message;
                _mailmessage.content = message;
                _mailmessage.username = "11941450";
                _mailmessage.password = "txyz5011";
                _mailmessage.filename = "";

                try {
                    _qqsmtp.send(_mailmessage);
                    _issendok = true;
                } catch (Exception ex) {
                    //公司邮箱补发
                    try {
                        util.GetMail.tidesmtp _tidesmtp = new util.GetMail.tidesmtp();
                        util.GetMail.tidesmtp.mailmessage _mailmessage2 = new util.GetMail.tidesmtp.mailmessage();
                        _mailmessage2.smtp = "Smtpcom.263xmail.com";
                        _mailmessage2.from = "zhenyang@tidestonesoft.com";
                        _mailmessage2.to = "18605236780@wo.cn";
                        _mailmessage2.copyto = "";
                        _mailmessage2.subject = message;
                        _mailmessage2.content = message;
                        _mailmessage2.username = "zhenyang@tidestonesoft.com";
                        _mailmessage2.password = "txyz5011";
                        _mailmessage2.filename = "";
                        _tidesmtp.send(_mailmessage2);
                        _issendok = true;
                    } catch (Exception ex2) {
                        System.out.println("sendmail  false：\r\n" + ex.getMessage() + "\r\n" + ex2.getMessage());
                    }
                }
        }
        return _issendok;
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

    public static void ini2(List _bigmessage, util.GetTools.tools _tools) {
        _bigmessage = fun.bigmessage_Load(_bigmessage);

        int m = _bigmessage.size();
        if (m > 0) {
            String systime = _tools.systime_prase_string("月") + "月" + _tools.systime_prase_string("日") + "日";
            //System.out.println(systime);
            String str = "";
            for (int i = 0; i < m; i++) {
                String mess = _bigmessage.get(i).toString();
                if (mess != null && mess.length() > 0) {
                    if (mess.indexOf(systime) != -1) {
                        if (str.length() == 0) {
                            str = mess;
                        } else {
                            str = str + "\r\n" + mess;
                        }
                    }
                }
            }

            if (str.length() > 0) {
                //  System.out.println("----------------重要事件--------------\r\n" + str    + "\r\n--------------------------------------\r\n");
            }
        }

    }

    public static List bigmessage_Load(List _bigmessage) {
        _bigmessage.clear();

        //2015年美联储(FED)
        _bigmessage.add("04月27日test");
        _bigmessage.add("04月28日至04月29日－美联储FOMC举行为期两天的议息会议。#2015年美联储(FED)");
        _bigmessage.add("04月29日－美联储FOMC宣布利率决定。#2015年美联储(FED)");
        _bigmessage.add("06月16日至06月17日－美联储FOMC举行为期两天的议息会议。#2015年美联储(FED)");
        _bigmessage.add("06月17日－美联储FOMC宣布利率决定。#2015年美联储(FED)");
        _bigmessage.add("07月28日至7月29日－美联储FOMC举行为期两天的议息会议。#2015年美联储(FED)");
        _bigmessage.add("07月29日－美联储FOMC宣布利率决定。#2015年美联储(FED)");
        _bigmessage.add("09月16日至09月17日－美联储FOMC举行为期两天的议息会议。#2015年美联储(FED)");
        _bigmessage.add("09月17日－美联储FOMC宣布利率决定。#2015年美联储(FED)");
        _bigmessage.add("10月27日至10月28日－美联储FOMC举行为期两天的议息会议。#2015年美联储(FED)");
        _bigmessage.add("10月28日-美联储FOMC宣布利率决定。#2015年美联储(FED)");
        _bigmessage.add("12月15日至12月16日－美联储FOMC举行为期两天的议息会议。#2015年美联储(FED)");
        _bigmessage.add("12月16日－美联储FOMC宣布利率决定。#2015年美联储(FED)");
        //2015年欧洲央行(ECB)
        _bigmessage.add("05月06日－管理委员会会议，无宣布利率决定的计划。#2015年欧洲央行(ECB)");
        _bigmessage.add("05月20日－管理委员会会议，无宣布利率决定的计划。#2015年欧洲央行(ECB)");
        _bigmessage.add("06月03日－管理委员会会议，之后宣布利率决定并召开记者会。#2015年欧洲央行(ECB)");
        _bigmessage.add("06月17日－管理委员会会议和全行委员会会议，无宣布利率决定的计划(至6月18日)。#2015年欧洲央行(ECB)");
        _bigmessage.add("07月01日－管理委员会会议，无宣布利率决定的计划。#2015年欧洲央行(ECB)");
        _bigmessage.add("07月16日－管理委员会会议，之后宣布利率决定并召开记者会。#2015年欧洲央行(ECB)");
        _bigmessage.add("08月05日－管理委员会会议，无宣布利率决定的计划。#2015年欧洲央行(ECB)");
        _bigmessage.add("09月03日－管理委员会会议，之后宣布利率决定并召开记者会。#2015年欧洲央行(ECB)");
        _bigmessage.add("09月16日－管理委员会会议和全行委员会会议，无宣布利率决定的计划(至9月17日)。#2015年欧洲央行(ECB)");
        _bigmessage.add("10月07日－管理委员会会议，无宣布利率决定的计划。#2015年欧洲央行(ECB)");
        _bigmessage.add("10月22日－管理委员会会议，之后宣布利率决定并召开记者会。#2015年欧洲央行(ECB)");
        _bigmessage.add("11月04日－管理委员会会议，无宣布利率决定的计划。#2015年欧洲央行(ECB)");
        _bigmessage.add("11月18日－管理委员会会议，无宣布利率决定的计划。#2015年欧洲央行(ECB)");
        _bigmessage.add("12月03日－管理委员会会议，之后宣布利率决定并召开记者会。#2015年欧洲央行(ECB)");
        _bigmessage.add("12月16日－管理委员会会议和全行委员会会议，无宣布利率决定的计划(至12月17日)。#2015年欧洲央行(ECB)");
        //2015年英国央行(BOE)
        _bigmessage.add("05月08日－货币政策会议(05月11日周一继续开会)。#2015年英国央行(BOE)");
        _bigmessage.add("06月03日－货币政策会议(至06月04日)。#2015年英国央行(BOE)");
        _bigmessage.add("07月08日－货币政策会议(至07月09日)。#2015年英国央行(BOE)");
        _bigmessage.add("08月05日－货币政策会议(至08月06日)。#2015年英国央行(BOE)");
        _bigmessage.add("09月09日－货币政策会议(至09月10日)。#2015年英国央行(BOE)");
        _bigmessage.add("10月07日－货币政策会议(至10月08日)。#2015年英国央行(BOE)");
        _bigmessage.add("11月04日－货币政策会议(至11月05日)。#2015年英国央行(BOE)");
        _bigmessage.add("12月09日－货币政策会议(至12月10日)。#2015年英国央行(BOE)");
        //2015年日本央行(BOJ)
        _bigmessage.add("04月30日－日本央行货币政策会议。#2015年日本央行(BOJ)");
        _bigmessage.add("05月21日－日本央行货币政策会议(至05月22日)。#2015年日本央行(BOJ)");
        _bigmessage.add("06月18日－日本央行货币政策会议(至06月19日)。#2015年日本央行(BOJ)");
        //2015年加拿大央行(BOC)
        _bigmessage.add("04月15日－公布指标利率决定和货币政策报告。#2015年加拿大央行(BOC)");
        _bigmessage.add("05月27日－公布指标利率决定。#2015年加拿大央行(BOC)");
        _bigmessage.add("07月15日－公布指标利率决定和货币政策报告。#2015年加拿大央行(BOC)");
        _bigmessage.add("09月09日－公布指标利率决定。#2015年加拿大央行(BOC)");
        _bigmessage.add("10月21日－公布指标利率决定和货币政策报告。#2015年加拿大央行(BOC)");
        _bigmessage.add("12月02日－公布指标利率决定。#2015年加拿大央行(BOC)");
        //2015年瑞士央行(SNB)
        _bigmessage.add("06月18日－货币政策评估。#2015年瑞士央行(SNB)");
        _bigmessage.add("09月17日－货币政策评估。#2015年瑞士央行(SNB)");
        _bigmessage.add("12月10日－货币政策评估。#2015年瑞士央行(SNB)");
        //2015年澳洲央行(RBA)
        _bigmessage.add("05月05日－召开利率会议。#2015年澳洲央行(RBA)");
        _bigmessage.add("06月02日－召开利率会议。#2015年澳洲央行(RBA)");
        _bigmessage.add("07月07日－召开利率会议。#2015年澳洲央行(RBA)");
        _bigmessage.add("08月04日－召开利率会议。#2015年澳洲央行(RBA)");
        _bigmessage.add("09月01日－召开利率会议。#2015年澳洲央行(RBA)");
        _bigmessage.add("10月06日－召开利率会议。#2015年澳洲央行(RBA)");
        _bigmessage.add("11月03日－召开利率会议。#2015年澳洲央行(RBA)");
        _bigmessage.add("12月01日－召开利率会议。#2015年澳洲央行(RBA)");
        //2015年新西兰联储(RBNZ)
        _bigmessage.add("04月30日－公布指标利率。#2015年新西兰联储(RBNZ)");
        _bigmessage.add("06月11日－公布货币政策声明和指标利率。#2015年新西兰联储(RBNZ)");
        _bigmessage.add("07月23日－公布指标利率。#2015年新西兰联储(RBNZ)");
        _bigmessage.add("09月10日－公布货币政策声明和指标利率。#2015年新西兰联储(RBNZ)");
        _bigmessage.add("10月29日－公布指标利率。#2015年新西兰联储(RBNZ)");
        _bigmessage.add("12月10日－公布货币政策声明和指标利率。#2015年新西兰联储(RBNZ)");
        return _bigmessage;
    }

}
