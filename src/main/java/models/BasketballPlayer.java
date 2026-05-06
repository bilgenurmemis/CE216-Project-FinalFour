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
            System.out.println(name + " sakat olduğu için antrenman yapamaz!");
            return;
        }
        fitness = Math.min(100, fitness + 10);
        fitness = Math.max(0, fitness - 2);
        shooting += 5;
        rebounding += 5;
        if (r.nextInt(100) < 10) {
            isInjured = true;
            System.out.println(name + " antrenmanda sakatlandı!");
        }
    }

    @Override
    public void recover() {
        fitness = Math.min(100, fitness + 15);
        if (isInjured && fitness > 60) {
            isInjured = false;
            System.out.println(name + " iyileşti!");
        }
    }

    @Override
    public int getSkillLevel() {
        // Şut, ribaund ve kondisyonun ortalaması oyuncunun yeteneğini belirler
        int skill = (int) ((shooting + rebounding + fitness) / 3);
        if (isInjured) {
            skill = (int) (skill * 0.5); // Sakatsa yeteneği yarı yarıya düşer
        }
        return skill;
    }

    // Getter ve Setter'lar eklenebilir
    public int getShooting() { return shooting; }
    public void setShooting(int shooting) { this.shooting = shooting; }

    public int getRebounding() { return rebounding; }
    public void setRebounding(int rebounding) { this.rebounding = rebounding; }
}
