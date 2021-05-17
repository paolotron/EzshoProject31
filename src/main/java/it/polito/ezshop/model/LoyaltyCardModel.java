package it.polito.ezshop.model;

public class LoyaltyCardModel {
    //public static final int MAXPOINTS = 10000;
    int id;
    int points;

    public LoyaltyCardModel(){}

    public LoyaltyCardModel(int id){
        this.id = id;
        points = 0;
    }

    public LoyaltyCardModel(int id, int points){
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

    public boolean updatePoints(int points){
        if(this.points + points < 0)
            return false;
        this.points += points;
        return true;
    }
}
