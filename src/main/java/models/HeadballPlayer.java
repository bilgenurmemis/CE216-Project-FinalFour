
package models;

import java.io.Serializable;
import java.util.Random;

public class HeadballPlayer extends BasePlayer implements Serializable {
    private static final long serialVersionUID = 1L;

    private int heading;
    private static final Random r = new Random();

    public HeadballPlayer(String name, int age, double fitness) {
        super(name, age, fitness);
        this.heading = 50;
    }

    @Override
    public void train() {
        if (isInjured) {
            System.out.println(name + " is injured and cannot train!");
            return;
        }
        fitness = Math.min(100, fitness + 10);
        fitness = Math.max(0, fitness - 2);
        heading += 7;
        if (r.nextInt(100) < 10) {
            isInjured = true;
            System.out.println(name + " got injured!");
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
        int skill = (int) ((heading + fitness) / 2);
        if (isInjured) skill = (int) (skill * 0.5);
        return skill;
    }
}