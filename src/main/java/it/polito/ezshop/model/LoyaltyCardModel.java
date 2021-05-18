package it.polito.ezshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    public String getId(){
        String orig = String.valueOf(id);
        StringBuilder s = new StringBuilder();
        for(int i=0; i < 10-orig.length(); i++){
            s.append("0");
        }
        s.append(orig);
        return s.toString();
    }

    @JsonIgnore
    public void setId(int id){
        this.id = id;
    }

    public int getIntId(){
        return id;
    }

    public void setIntId(int id){
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

    public static boolean checkCard(String cardCode){
        return cardCode != null && !cardCode.equals("") && cardCode.matches("\\d{10}$");
    }
}
