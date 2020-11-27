package ru.job4j.dream.model;

import org.junit.Assert;
import org.junit.Test;

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
        Candidate candidate = new Candidate(1, "test");
        Assert.assertThat(candidate.getId(), is(1));
        Assert.assertThat(candidate.getName(), is("test"));
    }

    /**
     * Здесь проверяется редактирование кандидата.
     */
    @Test
    public void whenEditCandidateThenSuccess() {
        Candidate candidate = new Candidate(1, "test");
        candidate.setId(2);
        candidate.setName("tset");
        Assert.assertThat(candidate.getId(), is(2));
        Assert.assertThat(candidate.getName(), is("tset"));
    }

    /**
     * Здесь проверяется сравнение двух одинаковых кандидатов.
     */
    @Test
    public void whenCreateCandidateWithSameDataThenEquals() {
        Candidate candidate1 = new Candidate(1, "test");
        Candidate candidate2 = new Candidate(1, "test");
        Assert.assertTrue(candidate1.equals(candidate2));
    }
}