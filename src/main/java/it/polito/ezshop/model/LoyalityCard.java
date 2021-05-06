package it.polito.ezshop.model;

public class LoyalityCard {
    String id;
    int points;

    public LoyalityCard(String id){
        this.id = id;
        points = 0;
    }

    public LoyalityCard(String id, int points){
        this.id = id;
        this.points = points;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public int getPoints(){
        return points;
    }

    public void addPoints(int points){
        this.points += points;
    }
}
