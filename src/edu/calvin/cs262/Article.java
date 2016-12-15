package edu.calvin.cs262;

//import java.sql.String;

/**
 * A Player class (POJO) for the player relation
 *
 * @author kvlinden
 * @version summer, 2016
 */
public class Article {

    private int id;
    private String sender, senderName, subject, body, date;

    Article() { /* a default constructor, required by Gson */  }

    Article(int id, String sender, String senderName, String subject, String body, String date) {
        this.id = id;
        this.subject = subject;
        this.sender = sender;
        this.senderName = senderName;
        this.body = body;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public String getSubject() {
        return subject;
    }
    public String getSender() {
        return sender;
    }
    public String getBody() {return body;}
    public String getDate() { return date; }
    public String senderName() {return senderName;}

    public void setId(int id) {
        this.id = id;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setBody(String body) { this.body = body; }
    public void setDate(String date) {this.date = date; }
    public void setSenderName(String senderName) {this.senderName = senderName;}

}
