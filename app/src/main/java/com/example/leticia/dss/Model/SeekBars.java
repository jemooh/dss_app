package com.example.leticia.dss.Model;

import java.io.Serializable;


            public  class SeekBars implements Serializable {
                private static final long serialVersionUID = 1L;
                private String title;
                private String negotiation_id;
                private String id;

                public SeekBars(String id, String title,String negotiation_id) {
                    super();
                    this.id = id ;
                    this.title = title;
                    this.negotiation_id = negotiation_id ;
                    }


                public String getTitle(){
                    return title;
                }


                public String getId(){
                    return id;
                }

                public String getNegotiation_id() {
                    return negotiation_id;
                }

            }
