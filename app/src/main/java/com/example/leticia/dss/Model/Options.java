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
			    boolean selected;

			    public Options(String option_id,String title, String rating, String points) {
			        super();
					this.option_id = option_id ;
			        this.title = title ;
			        this.rating = rating;
			        this.points = points ;
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
			    

			    	  

		
	
}
