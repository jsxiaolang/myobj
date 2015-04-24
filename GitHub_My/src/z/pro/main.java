package z.pro;


/**
 *
 * @author
 *
 *
 */
public class main extends Thread {

    private static util.GetTools.tools _tools = new util.GetTools.tools();

    //-------------------------------------程序入口----------------------------------//
    public void run() {

        while (true) {
            try {
                doing_main();
                Thread.sleep(1000 * 15);//间隔后再次运行
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

//-------------------------------------主程序------------------------------------//
    public void doing_main() {
        String str_mes = _tools.systime_prase_string("");

        if (fun.isOnline()) {
            for (int i = 0; i < 100; i++) {
                System.out.println("progress: " + i);//这个我只是给你个提示 我这里没测试 
                try {
                    Thread.sleep(100 * 1);//间隔后再次运行
                  
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            System.out.print("[" + str_mes + "]:" + "当前网络不通");
        }
    }
}
