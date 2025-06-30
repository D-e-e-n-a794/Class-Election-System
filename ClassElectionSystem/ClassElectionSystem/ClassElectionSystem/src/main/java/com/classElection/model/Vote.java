package com.classElection.model;

public class Vote {
    private String voterUsername;
    private int candidateId;

    public Vote(String voterUsername, int candidateId) {
        this.voterUsername = voterUsername;
        this.candidateId = candidateId;
    }

    public String getVoterUsername() { return voterUsername; }
    public int getCandidateId() { return candidateId; }
}
