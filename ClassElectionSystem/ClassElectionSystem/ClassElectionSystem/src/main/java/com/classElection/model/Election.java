package com.classElection.model;

import java.util.ArrayList;
import java.util.List;

public class Election {
    private String type;
    private List<Candidate> candidates = new ArrayList<>();

    public Election(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }
}
