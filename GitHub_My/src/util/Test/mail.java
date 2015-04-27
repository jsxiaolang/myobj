/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Test;

import util.GetMail.qqsmtp;

/**
 *
 * @author zhen
 */
public class mail {

    public static util.GetMail.qqsmtp _mail = new util.GetMail.qqsmtp();

    public static void main(String[] args) {
        util.GetMail.qqsmtp.mailmessage _mailmessage = new util.GetMail.qqsmtp.mailmessage();
        _mailmessage.smtp = "smtp.qq.com";
        _mailmessage.from = "11941450@qq.com";
        _mailmessage.to = "18605236780@wo.cn";
        _mailmessage.copyto = "";
        _mailmessage.subject = "test";
        _mailmessage.content = "test";
        _mailmessage.username = "11941450";
        _mailmessage.password = "txyz5011";
        _mailmessage.filename = "";
        _mail.send(_mailmessage);
    }

}
