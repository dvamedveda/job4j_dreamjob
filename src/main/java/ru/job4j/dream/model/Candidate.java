package ru.job4j.dream.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Модель данных для кандидата.
 */
public class Candidate {
    private int id;
    private String name;
    private int city;
    private List<Integer> userPhotos = new ArrayList<>();

    public Candidate(int id, String name, int city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getUserPhotos() {
        return this.userPhotos;
    }

    public void setUserPhotos(List<Integer> userPhotos) {
        this.userPhotos = userPhotos;
    }

    public int getCity() {
        return this.city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id
                && name.equals(candidate.name)
                && city == candidate.city;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city);
    }
}