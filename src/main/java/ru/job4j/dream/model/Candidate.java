package ru.job4j.dream.model;

import java.util.Objects;

/**
 * Модель данных для кандидата.
 */
public class Candidate {
    private int id;
    private String name;
    private int photoId = 0;

    public Candidate(int id, String name) {
        this.id = id;
        this.name = name;
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

    public int getPhotoId() {
        return this.photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
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
                && name.equals(candidate.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}