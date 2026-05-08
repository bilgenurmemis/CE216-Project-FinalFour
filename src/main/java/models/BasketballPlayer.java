package models;

import java.io.Serializable;
import java.util.Random;

public class BasketballPlayer extends BasePlayer implements Serializable {
    private static final long serialVersionUID = 1L;

    private int shooting;
    private int rebounding;
    private static final Random r = new Random();

    public BasketballPlayer(String name, int age, double fitness) {
        super(name, age, fitness);
        this.shooting = 50;
        this.rebounding = 50;
    }

    @Override
    public void train() {
        if (isInjured) {
            System.out.println(name + " is injured and cannot train!");
            return;
        }
        fitness = Math.min(100, fitness + 10);
        fitness = Math.max(0, fitness - 2);
        shooting += 5;
        rebounding += 5;
        if (r.nextInt(100) < 10) {
            isInjured = true;
            System.out.println(name + " got injured during training!");
        }
    }

    @Override
    public void recover() {
        fitness = Math.min(100, fitness + 15);
        if (isInjured && fitness > 60) {
            isInjured = false;
            System.out.println(name + " has recovered!");
        }
    }

    @Override
    public int getSkillLevel() {
        int skill = (int) ((shooting + rebounding + fitness) / 3);
        if (isInjured) {
            skill = (int) (skill * 0.5);
        }
        return skill;
    }
    public int getShooting() { return shooting; }
    public void setShooting(int shooting) { this.shooting = shooting; }

    public int getRebounding() { return rebounding; }
    public void setRebounding(int rebounding) { this.rebounding = rebounding; }
}
