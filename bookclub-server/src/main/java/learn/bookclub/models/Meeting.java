package learn.bookclub.models;

import java.util.Date;
import java.util.Objects;

public class Meeting {
    private int meetingId;
    private String readingGoal;
    private Date meetingDate;
    private String meetingNotes;
    private Book book;

    public Meeting(int meetingId, String readingGoal, Date meetingDate, String meetingNotes, Book book) {
        this.meetingId = meetingId;
        this.readingGoal = readingGoal;
        this.meetingDate = meetingDate;
        this.meetingNotes = meetingNotes;
        this.book = book;
    }


    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getReadingGoal() {
        return readingGoal;
    }

    public void setReadingGoal(String readingGoal) {
        this.readingGoal = readingGoal;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingNotes() {
        return meetingNotes;
    }

    public void setMeetingNotes(String meetingNotes) {
        this.meetingNotes = meetingNotes;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return meetingId == meeting.meetingId && Objects.equals(readingGoal, meeting.readingGoal) && Objects.equals(meetingDate, meeting.meetingDate) && Objects.equals(meetingNotes, meeting.meetingNotes) && Objects.equals(book, meeting.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingId, readingGoal, meetingDate, meetingNotes, book);
    }
}
