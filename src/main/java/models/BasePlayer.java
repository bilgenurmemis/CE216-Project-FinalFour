package models;

import java.io.Serializable;

public abstract class BasePlayer implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String name;
    protected int age;
    protected double fitness;
    protected boolean isInjured;

    public BasePlayer(String name, int age, double fitness) {
        this.name = name;
        this.age = age;
        this.fitness = Math.min(100, Math.max(0, fitness));
        this.isInjured = false;
    }

    public abstract void train();
    public abstract void recover();
    public abstract int getSkillLevel();

    public double getFitness() { return fitness; }
    public String getName() { return name; }
    public boolean isInjured() { return isInjured; }
    public void setInjured(boolean injured) { isInjured = injured; }

    @Override
    public String toString() {
        return name + " | Fitness: " + fitness + " | Injured: " + isInjured;
    }
}
