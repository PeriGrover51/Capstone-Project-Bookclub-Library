package learn.bookclub.models;

import java.time.LocalDate;
//import java.util.Date;
import java.util.Objects;

public class Book {

    private int bookId; //pk
    private String title; //required
    private String author; //required
    private String genre; //required
    private LocalDate whenRead; //required
    private String link;
    private String imgLink;

    public Book(int bookId, String title, String author, String genre, LocalDate whenRead, String link, String imgLink) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.whenRead = whenRead;
        this.link = link;
        this.imgLink = imgLink;
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

    public LocalDate getWhenRead() {
        return whenRead;
    }

    public void setWhenRead(LocalDate whenRead) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId == book.bookId && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(genre, book.genre) && Objects.equals(whenRead, book.whenRead) && Objects.equals(link, book.link) && Objects.equals(imgLink, book.imgLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, title, author, genre, whenRead, link, imgLink);
    }
}
