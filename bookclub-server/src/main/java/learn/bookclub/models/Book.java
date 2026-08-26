package learn.bookclub.models;

import java.util.Date;

public class Book {

    private int bookId; //pk
    private String title; //required
    private String author; //required
    private String genre; //required
    private Date whenRead; //required
    private String link;
    private String imgLink;

    public Book(int bookId, String title, String author, String genre, Date whenRead) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.whenRead = whenRead;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Date getWhenRead() {
        return whenRead;
    }

    public void setWhenRead(Date whenRead) {
        this.whenRead = whenRead;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }
}
