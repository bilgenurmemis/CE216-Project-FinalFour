package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeagueTable {
    private List<BaseTeam> teams;
    public LeagueTable(List<BaseTeam> teams){
        this.teams = new ArrayList<>(teams);
    }
    public List<BaseTeam> getTable(){
        Collections.sort(teams, new Comparator<BaseTeam>() {
            @Override
            public int compare(BaseTeam o1, BaseTeam o2) {
                int pointC = Integer.compare(o2.getPoints(), o1.getPoints());
                if (pointC != 0){
                    return pointC;
                }
                return Integer.compare(o2.getAverage(), o1.getAverage());
            }
        });
        return teams;
    }
    public void clearStatus(){
        for(BaseTeam team : teams){
            team.resetStatus();
        }
    }
}
