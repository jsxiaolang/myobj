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

/**
 *
 * @author yangzhen
 *
 */
public class main extends Thread {

    //-------------------------------日志-------------------------------------// 
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_wh.config");
    }

    //-------------------------------日志-------------------------------------//
    private static util.GetTools.tools _tools = new util.GetTools.tools();
    private static util.GetTools.getPro _getPro = new util.GetTools.getPro();
    private static util.GetFile.txt _txt = new util.GetFile.txt();
    private static List _list = new ArrayList();
    private static util.GetFile.xmlconf _xmlconf = new util.GetFile.xmlconf();
    private static util.GetThread.thread _thread = new util.GetThread.thread(3);
    //_xmlconf.getvalue(db_name, "DRIVER");
    private static strclass.data _USDX = new strclass.data();

    public static List _bigmessage = new ArrayList();

    public void run() {
        try {
            // --------------------------添加任务------------------------//
            _thread.execute(task_min());//任务处理
            _thread.execute(task_time());// 程序监控
            // --------------------------添加任务------------------------//

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // ------------------等待线程运行---------------//
            _thread.waitFinish(); // 等待所有任务执行完毕
            _thread.closePool(); // 关闭线程池
            // ------------------等待线程运行---------------//
        }
    }

    private static Runnable task_min() {
        return new Runnable() {
            public void run() {

                fun.ini(_list);
                fun.ini2(_bigmessage, _tools);
                //处理数据

                log.info("task_min is runing...");

                while (true) {
                    //取网络时间，同时判断是否有网
                    String timenows = "";
                    try {
                        timenows = _tools.getnettime(2);
                    } catch (Exception ex) {
                        log.info(ex.getMessage().toString());
                    }
                    if (timenows.length() > 0) {//有网络
                        jx_url();
                        //------------------------------休眠----------------------------------//
                        int times = 2;//默认2秒
                        try {
                            String gettime = _xmlconf.getvalue("WH", "TIMES");
                            times = _tools.string_parse_int(gettime);//获取配置文件中设置的间隔时间
                            Thread.sleep(1000 * times);
                        } catch (Exception ex) {
                            log.info(ex.getMessage().toString());
                        }
                        //------------------------------休眠----------------------------------//

                    } else {
                        log.info("System Status: has not net ,please wating  15s...");

                        //------------------------------休眠----------------------------------//
                        try {
                            Thread.sleep(1000 * 15);
                        } catch (Exception ex) {
                            log.info(ex.getMessage().toString());
                        }
                        //------------------------------休眠----------------------------------//
                    }
                }
            }
        };
    }

    private static Runnable task_time() {
        return new Runnable() {
            public void run() {
                log.info("task_hour is runing...");

                boolean bs_t1 = false;
                boolean bs_t2 = false;
                boolean bs_t3 = false;
                while (true) {

                    try {//取网络时间更新系统时间
                        String sys_time_min = "";
                        sys_time_min = _tools.systime_prase_string("分");
                        if (sys_time_min.equals("46")) {
                            bs_t1 = true;
                        }

                        if (bs_t1) {
                            if (sys_time_min.equals("47")) {
                                bs_t2 = true;
                            }
                        }

                        if (bs_t2) {
                            String sys_time_hour = "";
                            sys_time_hour = _tools.systime_prase_string("时");

                            if (sys_time_hour.equals("7")
                                    || sys_time_hour.equals("12")
                                    || sys_time_hour.equals("17")
                                    || sys_time_hour.equals("22")) {
                                bs_t3 = true;
                            }
                        }

                        if (bs_t3) {

                            //更新系统时间
                            try {
                                String timenows = "";
                                timenows = _tools.getnettime(2);
                                if (timenows.length() > 0) {
                                    String cmdString = "sudo date -s \"" + timenows + "\"";
                                    _getPro.Pro_Start(cmdString);
                                    log.info("System Status:date is update...");
                                }
                            } catch (Exception ex) {
                                log.info(ex.getMessage().toString());
                            }

                            //更新系统状态
                            try {
                                boolean bs = fun.sendmail2("System Status: is OK", _xmlconf);
                                if (!bs) {
                                    log.info("System Status:send mail false ");
                                } else {
                                    log.info("System Status: is OK");
                                }
                            } catch (Exception ex) {
                                log.info(ex.getMessage().toString());
                            }

                            bs_t1 = false;
                            bs_t2 = false;
                            bs_t3 = false;
                        }
                    } catch (Exception ex) {
                        log.info(ex.getMessage().toString());
                    }

                    //------------------------------休眠----------------------------------//
                    try {
                        Thread.sleep(1000 * 15);
                    } catch (Exception ex) {
                        log.info(ex.getMessage().toString());
                    }
                    //------------------------------休眠----------------------------------//
                }
            }
        };
    }

    private static void jx_url() {

        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.0000");
        //java.text.DecimalFormat df2 = new java.text.DecimalFormat("#0.00000");
        StringBuffer br = new StringBuffer();

        if (_list.size() > 0) {

            String systime = _tools.getnettime(1);
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

                        //EURUSD=x",1.1177,"4/30/2015","4:53pm"
                        _ll = null;
                        _ll = sp.split("\\n");//按换行截取

                        double sum = 50.14348112;

                        boolean _jixi = true;
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
                                    log.info("no jiexi:[" + sp + "]");
                                    _jixi = false;
                                }
                            }

                            if (!_jixi) {
                                continue;
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
                                    _issendok = fun.sendmail(pd, _USDX.UPDOWN, _xmlconf);
                                } else {  //   -1小于0
                                    _USDX.UPDOWN = df.format(sum_updown);
                                    _issendok = fun.sendmail(pd, _USDX.UPDOWN, _xmlconf);
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

                            log.info(br.toString());
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
