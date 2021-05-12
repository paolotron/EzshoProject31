package it.polito.ezshop.model;

public class LoyalityCard {
    int id;
    int points;

    public LoyalityCard(){}

    public LoyalityCard(int id){
        this.id = id;
        points = 0;
    }

    public LoyalityCard(int id, int points){
        this.id = id;
        this.points = points;
    }

    public String getId(){
        return String.valueOf(id);
    }

    public void setId(int id){
        this.id = id;
    }

    public int getPoints(){
        return points;
    }

    public void addPoints(int points){
        this.points += points;
    }
}
