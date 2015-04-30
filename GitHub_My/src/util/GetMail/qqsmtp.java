/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetMail;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class qqsmtp {

     
    public static class mailmessage {
        public String smtp = "smtp.qq.com";
        public String from = "";
        public String to = "";
        public String copyto = "";
        public String subject = "";
        public String content = "";
        public String username = "";
        public String password = "";
        public String filename = "";
        public boolean needAuth = false; //smtp是否需要认证 

    }

    private static MimeMessage mimeMsg; //MIME邮件对象 
    private static Multipart mp; //Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象 
    private static Session session; //邮件会话对象 
    private static Properties props; //系统属性 

    /**
     * Constructor
     *
     * @param smtp 邮件发送服务器
     */
    private void openMail(String smtp) {
        setSmtpHost(smtp);
        createMimeMessage();
    }

    /**
     * 设置邮件发送服务器
     *
     * @param hostName String
     */
    private void setSmtpHost(String hostName) {
        //System.out.println("设置系统属性：mail.smtp.host = " + hostName);
        if (props == null) {
            props = System.getProperties(); //获得系统属性对象 	
        }
        props.put("mail.smtp.host", hostName); //设置SMTP主机 
    }

    /**
     * 创建MIME邮件对象
     *
     * @return
     */
    private boolean createMimeMessage() {
        try {
            //System.out.println("准备获取邮件会话对象！");
            session = Session.getDefaultInstance(props, null); //获得邮件会话对象 
        } catch (Exception e) {
            System.err.println("获取邮件会话对象时发生错误！" + e);
            return false;
        }

        //System.out.println("准备创建MIME邮件对象！");
        try {
            mimeMsg = new MimeMessage(session); //创建MIME邮件对象 
            mp = new MimeMultipart();

            return true;
        } catch (Exception e) {
            System.err.println("创建MIME邮件对象失败！" + e);
            return false;
        }
    }

    /**
     * 设置SMTP是否需要验证
     *
     * @param need
     */
    private void setNeedAuth(boolean need) {
        //System.out.println("设置smtp身份认证：mail.smtp.auth = " + need);
        if (props == null) {
            props = System.getProperties();
        }
        if (need) {
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.smtp.auth", "false");
        }
    }

    /**
     * 设置邮件主题
     *
     * @param mailSubject
     * @return
     */
    private boolean setSubject(String mailSubject) {
       // System.out.println("设置邮件主题！");
        try {
            mimeMsg.setSubject(mailSubject);
            return true;
        } catch (Exception e) {
            System.err.println("设置邮件主题发生错误！");
            return false;
        }
    }

    /**
     * 设置邮件正文
     *
     * @param mailBody String
     */
    private boolean setBody(String mailBody) {
        try {
            BodyPart bp = new MimeBodyPart();
            bp.setContent("" + mailBody, "text/html;charset=GBK");
            mp.addBodyPart(bp);
            return true;
        } catch (Exception e) {
            System.err.println("设置邮件正文时发生错误！" + e);
            return false;
        }
    }

    /**
     * 添加附件
     *
     * @param filename String
     */
    private boolean addFileAffix(String filename) {

        //System.out.println("增加邮件附件：" + filename);
        try {
            BodyPart bp = new MimeBodyPart();
            FileDataSource fileds = new FileDataSource(filename);
            bp.setDataHandler(new DataHandler(fileds));
            bp.setFileName(fileds.getName());

            mp.addBodyPart(bp);

            return true;
        } catch (Exception e) {
            System.err.println("增加邮件附件：" + filename + "发生错误！" + e);
            return false;
        }
    }

    /**
     * 设置发信人
     *
     * @param from String
     */
    private boolean setFrom(String from) {
        //System.out.println("设置发信人！");
        try {
            mimeMsg.setFrom(new InternetAddress(from)); //设置发信人 
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置收信人
     *
     * @param to String
     */
    private boolean setTo(String to) {
        if (to == null) {
            return false;
        }
        try {
            mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置抄送人
     *
     * @param copyto String
     */
    private boolean setCopyTo(String copyto) {
        if (copyto == null) {
            return false;
        }
        try {
            mimeMsg.setRecipients(Message.RecipientType.CC, (Address[]) InternetAddress.parse(copyto));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 发送邮件
     */
    private boolean sendOut(mailmessage _mailmessage) {
        try {
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            //System.out.println("正在发送邮件....");

            Session mailSession = Session.getInstance(props, null);
            Transport transport = mailSession.getTransport("smtp");
            transport.connect((String) props.get("mail.smtp.host"), _mailmessage.username, _mailmessage.password);
            transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
            if (_mailmessage.copyto.length() > 0) {
                transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
            }
            //transport.send(mimeMsg); 

            //System.out.println("发送邮件成功！");
            transport.close();
            return true;
        } catch (Exception e) {
            System.err.println("邮件发送失败！" + e.getMessage().toString());
            return false;
        }
    }

    /**
     * 调用sendOut方法完成邮件发送
     *
     * @param smtp
     * @param from
     * @param to
     * @param subject
     * @param content
     * @param username
     * @param password
     * @return boolean
     */
    public boolean send(mailmessage _mailmessage) {

        openMail(_mailmessage.smtp);
        setNeedAuth(true);

        if (!setSubject(_mailmessage.subject)) {
            return false;
        }
        if (!setBody(_mailmessage.content)) {
            return false;
        }
        if (!setTo(_mailmessage.to)) {
            return false;
        }
        if (!setFrom(_mailmessage.from)) {
            return false;
        }

        //带抄送
        if (_mailmessage.copyto.length() > 0) {
            if (!setCopyTo(_mailmessage.copyto)) {
                return false;
            }
        }

        //带附件
        if (_mailmessage.filename.length() > 0) {
            if (!addFileAffix(_mailmessage.filename)) {
                return false;
            }
        }

        if (!sendOut(_mailmessage)) {
            return false;
        }
        return true;
    }
}
