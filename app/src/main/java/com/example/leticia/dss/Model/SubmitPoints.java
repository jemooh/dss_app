package com.example.leticia.dss.Model;

import java.io.Serializable;


public  class SubmitPoints implements Serializable {
    private static final long serialVersionUID = 1L;
    private String option_id;
    private String myrating;
    public SubmitPoints(String option_id, String myrating, String points) {
        super();
        this.option_id = option_id ;
        this.myrating = myrating;
        }

    public void setOption_id(String option_id) {
        option_id= option_id;
    }
    public String getOption_id(){
        return option_id;
    }

    public void setMyrating(String myrating) {
        myrating= myrating;
    }
    public String getMyrating(){
        return myrating;
    }


}
