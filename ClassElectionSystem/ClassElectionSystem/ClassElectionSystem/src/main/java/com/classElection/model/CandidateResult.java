package com.classElection.model;

import javafx.beans.property.*;

public class CandidateResult {

    private final StringProperty name;
    private final StringProperty regNo;
    private final IntegerProperty votes;

    public CandidateResult(String name, String regNo, int votes) {
        this.name = new SimpleStringProperty(name);
        this.regNo = new SimpleStringProperty(regNo);
        this.votes = new SimpleIntegerProperty(votes);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty regNoProperty() {
        return regNo;
    }

    public IntegerProperty votesProperty() {
        return votes;
    }

    public String getName() {
        return name.get();
    }

    public String getRegNo() {
        return regNo.get();
    }

    public int getVotes() {
        return votes.get();
    }
}
