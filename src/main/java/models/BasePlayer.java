package models;

import java.io.Serializable;

public abstract class BasePlayer implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String name;
    protected int age;
    protected double fitness;
    protected boolean isInjured;
    protected int injuredGamesRemaining;
    protected int statsScore; // gol veya sayı

    public BasePlayer(String name, int age, double fitness) {
        this.name = name;
        this.age = age;
        this.fitness = Math.min(100, Math.max(0, fitness));
        this.isInjured = false;
        this.injuredGamesRemaining = 0;
        this.statsScore = 0;
    }

    public abstract void train();
    public abstract void recover();
    public abstract int getSkillLevel();

    public double getFitness() { return fitness; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public boolean isInjured() { return isInjured; }
    public void setInjured(boolean injured) { isInjured = injured; }
    public int getInjuredGamesRemaining() { return injuredGamesRemaining; }
    public void setInjuredGamesRemaining(int games) {
        this.injuredGamesRemaining = games;
        this.isInjured = games > 0;
    }
    public void decreaseInjury() {
        if (injuredGamesRemaining > 0) {
            injuredGamesRemaining--;
            if (injuredGamesRemaining == 0) isInjured = false;
        }
    }
    public int getStatsScore() { return statsScore; }
    public void addStatsScore(int score) { this.statsScore += score; }

    @Override
    public String toString() {
        return name + " | Fitness: " + fitness + " | Injured: " + isInjured;
    }
}