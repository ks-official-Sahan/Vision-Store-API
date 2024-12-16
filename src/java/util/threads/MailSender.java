package util.threads;

import util.Mail;

public class MailSender extends Thread {
    
    private String email;
    private String subject;
    private String content;

    public MailSender() {
    }
    
    public MailSender(String email, String subject, String content) {
        this.email = email;
        this.content = content;
        this.subject = subject;        
    }
    
    @Override
    public void run() {
        Mail.sendMail(email, subject, content);
    }    
}
