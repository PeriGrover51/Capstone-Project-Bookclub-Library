package learn.bookclub.models;

import java.util.Objects;

public class Vote {
    private int voteId;
    private int userId;
    private int nominationId;
    private int score;
    //a vote shouldn't need to know about the full User / Nomination object


    public Vote(int voteId, int userId, int nominationId, int score) {
        this.voteId = voteId;
        this.userId = userId;
        this.nominationId = nominationId;
        this.score = score;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNominationId() {
        return nominationId;
    }

    public void setNominationId(int nominationId) {
        this.nominationId = nominationId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return voteId == vote.voteId && userId == vote.userId && nominationId == vote.nominationId && score == vote.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(voteId, userId, nominationId, score);
    }
}
