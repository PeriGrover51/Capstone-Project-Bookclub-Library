package learn.bookclub.models;

import java.util.Objects;

public class Nomination {
    private int nominationId;
    private String title;
    private String author;
    private String genre;
    private User user;

    public Nomination(int nominationId, String title, String author, String genre, User user) {
        this.nominationId = nominationId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.user = user;
    }

    public int getNominationId() {
        return nominationId;
    }

    public void setNominationId(int nominationId) {
        this.nominationId = nominationId;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Nomination that = (Nomination) o;
        return nominationId == that.nominationId && Objects.equals(title, that.title) && Objects.equals(author, that.author) && Objects.equals(genre, that.genre) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nominationId, title, author, genre, user);
    }
}
