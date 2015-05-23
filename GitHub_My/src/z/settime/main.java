/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.settime;

/**
 *
 * @author yangzhen
 *
 */
public class main extends Thread {

    private static util.GetTools.tools _tools = new util.GetTools.tools();
    private static util.GetTools.getPro _getPro = new util.GetTools.getPro();

    //-------------------------------日志-------------------------------------// 
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_settime.config");
    }

    public void run() {
        try {
            //主运行程序
            doing();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private static void doing() {

        try {
            String timenows = "";
            timenows = _tools.getnettime(2);//取网络时间更新系统时间
            if (timenows.length() > 0) {
                String cmdString = "sudo date -s \"" + timenows + "\"";
                _getPro.Pro_Start(cmdString);
                log.info("System Status:date is update.[" + timenows + "]");
            }
        } catch (Exception ex) {
            log.info("Exception:" + ex.getMessage().toString());
        }
    }

}
