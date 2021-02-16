package ru.job4j.dream.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;

/**
 * Тесты класса ru.job4j.dream.model.Candidate.
 */
public class CandidateTest {

    /**
     * Здесь проверяется создание кандидата.
     */
    @Test
    public void whenCandidateCreateThenSuccess() {
        Candidate candidate = new Candidate(1, "test", 22);
        Assert.assertThat(candidate.getId(), is(1));
        Assert.assertThat(candidate.getName(), is("test"));
        Assert.assertThat(candidate.getCity(), is(22));
    }

    /**
     * Здесь проверяется редактирование кандидата.
     */
    @Test
    public void whenEditCandidateThenSuccess() {
        Candidate candidate = new Candidate(1, "test", 111);
        candidate.setId(2);
        candidate.setName("tset");
        Assert.assertThat(candidate.getId(), is(2));
        Assert.assertThat(candidate.getName(), is("tset"));
        Assert.assertThat(candidate.getCity(), is(111));
    }

    /**
     * Здесь проверяется сравнение двух одинаковых кандидатов.
     */
    @Test
    public void whenCreateCandidateWithSameDataThenEquals() {
        Candidate candidate1 = new Candidate(1, "test", 11);
        Candidate candidate2 = new Candidate(1, "test", 11);
        Assert.assertTrue(candidate1.equals(candidate2));
    }

    /**
     * Здесь проверяется что по умолчанию у кандидата не создается фото.
     */
    @Test
    public void whenCreateCandidateThenDefaultFoto() {
        Candidate candidate1 = new Candidate(1, "test", 1);
        Assert.assertThat(candidate1.getUserPhotos().size(), is(0));
    }

    /**
     * Здесь проверяется сохранения фото в объекте кандидата.
     */
    @Test
    public void whenSetPhotoForCandidateThenSuccess() {
        Candidate candidate1 = new Candidate(1, "test", 1);
        ArrayList<Integer> photos = new ArrayList<>();
        photos.add(11);
        candidate1.setUserPhotos(photos);
        Assert.assertThat(candidate1.getUserPhotos().get(0), is(11));
        Assert.assertThat(candidate1.getUserPhotos().size(), is(1));
    }
}