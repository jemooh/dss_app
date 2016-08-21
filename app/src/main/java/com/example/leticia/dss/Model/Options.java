package com.example.leticia.dss.Model;

import java.io.Serializable;
			
			/**
			 * This is the 'library' of all the users videos
			 *
			 * @author paul.blundell
			 */
			public  class Options implements Serializable {
				/**
				 * code, title, teacher, starttime, endtime, location,
				 */
				private static final long serialVersionUID = 1L;
				private String option_id;
				private String title;
			    private String rating;
			    private String points;
				private String color_id;
				private String mycolor;
				private String opponent_color_id;
			    boolean selected;

			    public Options(String option_id,String title, String rating, String points,String color_id,String mycolor,String opponent_color_id) {
			        super();
					this.option_id = option_id ;
			        this.title = title ;
			        this.rating = rating;
			        this.points = points;
					this.color_id = color_id;
					this.mycolor = mycolor;
					this.opponent_color_id = opponent_color_id;
				    }
			 

			    public String getOption_id(){
			        return option_id;
			    }

				public String getTitle(){
					return title;
				}
			    
			
			    public String getRating(){
			        return rating;
			    }
			    
			    
			    public String getPoints(){
			        return points;
			    }

				public String getColor_id(){
					return color_id;
				}

				public String getOpponent_color_id(){
					return opponent_color_id;
				}

				public String getMycolor(){
					return mycolor;
				}

			    	  

		
	
}
