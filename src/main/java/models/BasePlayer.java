package models;

public abstract class BasePlayer {
    protected String name;
    protected int age;
    protected double fitness;
    protected boolean isInjured;

    public BasePlayer(String name, int age, double fitness) {
        this.name = name;
        this.age = age;
        this.fitness = fitness;
        this.isInjured = false;
    }

    public abstract void train();
    public abstract void recover();

    public double getFitness() { return fitness; }
    public String getName() { return name; }
    public boolean isInjured() { return isInjured; }
    public void setInjured(boolean injured) { isInjured = injured; }
}