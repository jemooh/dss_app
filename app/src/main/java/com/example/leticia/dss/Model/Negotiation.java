/**
 * Created by Ketter on 5/26/2016.
 */
package com.example.leticia.dss.Model;

public class Negotiation  {

    private String title,negotiations_users_id,negotiation_id,opponent,active;
    //private int opponent,

    public Negotiation(String title, String negotiation_id, String negotiations_users_id,String opponent, String active){
        this.title= title;
        this.opponent = opponent;
        this.negotiation_id = negotiation_id;
        this.negotiations_users_id = negotiations_users_id;
        this.active = active;

    }
    public String getTitle(){
        return title;
    }
    public String getNegotiation_id(){
        return negotiation_id;
    }
    public String getOpponent(){
        return opponent;
    }
    public String getActive(){
        return active;
    }
    public String getNegotiationsUsersId(){
        return negotiations_users_id;
    }
}