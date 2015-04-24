/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetFtp;

/**
 *
 * @author zhen
 *
 */
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;
import util.AbstractClass.ftp;

/**
 * ftp上传，下载
 *
 * @author yangzhen
 *
 * @author ini() ：初始化 传值类型的
 * @author ini(String) ：初始化 conf类型的
 *
 */
public class ftpclient extends ftp {

    private util.GetFile.xmlconf _xmlconf = new util.GetFile.xmlconf();

    private String FTP_NAME = "";
    private String FTP_IP = "";
    private String FTP_USERNAME = "";
    private String FTP_PWD = "";
    private String FTP_DIR = "";
    private int FTP_PORT = 21;

    private FtpClient FTP_CLIENT = null;
    private OutputStream os = null;
    private FileInputStream is = null;

    @Override
    public void set_FTP_IP(String FTP_IP) {
        this.FTP_IP = FTP_IP;
    }

    @Override
    public void set_FTP_USERNAME(String FTP_USERNAME) {
        this.FTP_USERNAME = FTP_USERNAME;
    }

    @Override
    public void set_FTP_PWD(String FTP_PWD) {
        this.FTP_PWD = FTP_PWD;
    }

    @Override
    public void set_FTP_PORT(int FTP_PORT) {
        this.FTP_PORT = FTP_PORT;
    }

    @Override
    public String get_FTP_IP() {
        return FTP_IP.toString();
    }

    @Override
    public String get_FTP_USERNAME() {
        return FTP_USERNAME.toString();
    }

    @Override
    public String get_FTP_PWD() {
        return FTP_PWD.toString();
    }

    @Override
    public int get_FTP_PORT() {
        return FTP_PORT;
    }

    @Override
    public boolean ini() {
        boolean _bs = false;
        if (open()) {
            _bs = true;
        }
        if (!_bs) {
            System.out.println("FtpClient ini error:" + FTP_NAME + "#" + FTP_IP + "#" + FTP_USERNAME + "#" + FTP_PWD + "#" + FTP_DIR);
        }
        return _bs;
    }

    public boolean ini(String ftp_name) {
        boolean _bs = false;
        this.FTP_NAME = ftp_name;
        if (load()) {
            if (open()) {
                _bs = true;
            }
        }
        if (!_bs) {
            System.out.println("FtpClient ini error:" + FTP_NAME + "#" + FTP_IP + "#" + FTP_USERNAME + "#" + FTP_PWD + "#" + FTP_DIR);
        }
        return _bs;
    }

    private boolean load() {
        boolean _bs = false;
        try {
            FTP_IP = _xmlconf.getvalue(FTP_NAME, "IP");
            FTP_USERNAME = _xmlconf.getvalue(FTP_NAME, "USER");
            FTP_PWD = _xmlconf.getvalue(FTP_NAME, "PWD");
            FTP_DIR = _xmlconf.getvalue(FTP_NAME, "DIR");
            _bs = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!_bs) {
            System.out.println("FtpClient load error:" + FTP_NAME + "#" + FTP_IP + "#" + FTP_USERNAME + "#" + FTP_PWD + "#" + FTP_DIR);
        }
        return _bs;
    }

    private boolean open() {
        boolean _bs = false;
        FTP_CLIENT = new FtpClient();
        try {
            if (this.FTP_PORT != -1) {
                FTP_CLIENT.openServer(this.FTP_IP, this.FTP_PORT);
            } else {
                FTP_CLIENT.openServer(this.FTP_IP);
            }
            FTP_CLIENT.login(this.FTP_USERNAME, this.FTP_PWD);
            if (this.FTP_DIR.length() != 0) {
                FTP_CLIENT.cd(this.FTP_DIR);// path是ftp服务下主目录的子目录
            }
            FTP_CLIENT.binary();// 用2进制上传、下载
            System.out.println("FTP 已登录到\"" + FTP_CLIENT.pwd() + "\"目录");
            _bs = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!_bs) {
            System.out.println("FtpClient open error:" + FTP_NAME + "#" + FTP_IP + "#" + FTP_USERNAME + "#" + FTP_PWD + "#" + FTP_DIR);
        }
        return _bs;
    }

    @Override
    public boolean close() {
        boolean _bs = false;
        try {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (FTP_CLIENT != null) {
                FTP_CLIENT.closeServer();
            }
            _bs = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _bs;
    }

    @Override
    public boolean rush() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isDirExist(String full_dir) {
        boolean _bs = false;
        String pwd = "";
        try {
            pwd = FTP_CLIENT.pwd();
            FTP_CLIENT.cd(full_dir);
            FTP_CLIENT.cd(pwd);
            _bs = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _bs;
    }

    @Override
    public boolean EXECMD(String cmd) {
        boolean _bs = false;
        try {
            FTP_CLIENT.sendServer(cmd);
            System.out.println(cmd);
            _bs = true;
            //int reply = ftpClient.readServerResponse();
            //System.out.println(reply);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _bs;
    }

    @Override
    public boolean createDir(String full_dir) {
        try {
            FTP_CLIENT.ascii();
            StringTokenizer s = new StringTokenizer(full_dir, "/"); //sign
            s.countTokens();
            String pathName = "";//ftpClient.pwd();
            while (s.hasMoreElements()) {
                pathName = pathName + "/" + (String) s.nextElement();
                if (this.isDirExist(pathName)) {
                    continue;
                }
                try {
                    FTP_CLIENT.sendServer("MKD " + pathName + "\r\n");
                } catch (Exception e) {
                    e = null;
                    return false;
                }
                FTP_CLIENT.readServerResponse();
            }
            FTP_CLIENT.binary();
            return true;
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
    }

    @Override
    public List getDirFileList(String full_dir) {
        List list = new ArrayList();
        DataInputStream dis;
        try {
            //System.out.println(full_dir);
            dis = new DataInputStream(FTP_CLIENT.nameList(full_dir));
            String filename = "";
            while ((filename = dis.readLine()) != null) {
                String sfilename = new String(filename.getBytes("ISO-8859-1"), "utf-8");
                list.add(sfilename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 
     * @param full_filedir-----------------------------------------------------人家服务器的地址
     * @param full_filedir_new-------------------------------------------------
     * @return 
     */
    @Override
    public boolean downloadFile(String full_filedir, String full_filedir_new) {
        boolean _bs = false;
        long result = 0;
        TelnetInputStream is = null;
        FileOutputStream os = null;
        try {
            is = FTP_CLIENT.get(full_filedir);
            java.io.File outfile = new java.io.File(full_filedir_new);
            os = new FileOutputStream(outfile);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = is.read(bytes)) != -1) {
                os.write(bytes, 0, c);
                result = result + c;
            }
            _bs = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _bs;
    }

}
