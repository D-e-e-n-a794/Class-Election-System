package com.classElection.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class Candidate {

    private int id;
    private final StringProperty name;
    private final StringProperty registrationNumber;
    private final StringProperty description;

    // Constructor
    public Candidate(int id, String name, String registrationNumber, String description) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.registrationNumber = new SimpleStringProperty(registrationNumber);
        this.description = new SimpleStringProperty(description);
    }

    // No-arg constructor (optional)
    public Candidate() {
        this.name = new SimpleStringProperty();
        this.registrationNumber = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getRegistrationNumber() {
        return registrationNumber.get();
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber.set(registrationNumber);
    }

    public StringProperty registrationNumberProperty() {
        return registrationNumber;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public ObservableValue<String> regNoProperty() {
        return registrationNumber;
    }

    public String getRegNo() {
        return registrationNumber.get();
    }
}
