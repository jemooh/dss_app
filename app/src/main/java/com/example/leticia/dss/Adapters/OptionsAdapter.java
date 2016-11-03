package com.example.leticia.dss.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.leticia.dss.Model.Negotiation;
import com.example.leticia.dss.Model.Options;
import com.example.leticia.dss.R;
import com.example.leticia.dss.src.PendingViewActivity;

import java.text.DecimalFormat;
import java.util.List;

import static com.example.leticia.dss.R.color.black;

/**
		 * This adapter is used to show our items objects in a ListView
		 * It hasn't got many memory optimisations, if your list is getting bigger or more complex
		 * you may want to look at better using your view resources: http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/view/List14.html
		 */
		public class OptionsAdapter extends BaseAdapter {
			public List<Options> Items;
			DecimalFormat df = new DecimalFormat("#.##");
		    // An inflator to use when creating rows
		    private LayoutInflater mInflater;
		    Context mContext;
		   //public String posy =SingleItemActivity.positionDel;
		    /**
		     * @param context this is the context that the list will be shown in - used to create new list rows
		     * @param Items this is a list of items to display
		     */
		    public OptionsAdapter(Context context, List<Options> Items) {
		        this.Items = Items;
		        this.mInflater = LayoutInflater.from(context);
		        this.mContext = context;
		    }
		 
		    
		    public void setListData(List<Options> ListItems){
		         Items= ListItems;
		        
		      }
		    
		    //@Override
		    public int getCount() {
		        return Items.size();
		    }
		 
		    //@Override
		    public Object getItem(int position) {
		        return Items.get(position);
		    }

		    
		    //@Override
		    public long getItemId(int position) {
		        return position;
		    }
		    
		   
			
			static class ViewHolder {
				public TextView title, options,color;
			}
		 
		    //@Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		        View view = null; // If convertView wasn't null it means we have already set it to our list_item_user_video so no need to do it again
				if(convertView == null) {
		            // This is the layout we are using for each row in our list anything you declare in this layout can then be referenced below
		            convertView = mInflater.inflate(R.layout.options_row, parent, false);
					
					final ViewHolder viewHolder = new ViewHolder();
					viewHolder.title = (TextView) convertView.findViewById(R.id.textViewoptntitle);
					viewHolder.options = (TextView) convertView.findViewById(R.id.textViewpoints);
					//viewHolder.color = (TextView) convertView.findViewById(R.id.textViewcolor);

					convertView.setTag(viewHolder);
					convertView.setTag(R.id.textViewoptntitle, viewHolder.title);
					convertView.setTag(R.id.textViewpoints, viewHolder.options);
					//convertView.setTag(R.id.textViewcolor, viewHolder.color);

		            
		            //convertView.setTag(R.id.checkBoxStatus, viewHolder.checkorder);
				}


               /*
				if(position!=SelectedPosition)
				{
					convertView.setBackgroundColor(default Color);
				}
				else
				{
					convertView.setBackgroundColor(Color.argb(125,75,236,90));
				}*/

				
				ViewHolder holder = (ViewHolder) convertView.getTag();
		        // Get a single video from our list
				Options Item = Items.get(position);


					//holder.color.setBackgroundColor(Color.RED);//

				if ((Item.getRating()!=null) && (Item.getPoints()!=null)) {
					DecimalFormat dF = new DecimalFormat("0.00");
					DecimalFormat dp = new DecimalFormat("0.0");
					String rating = Item.getRating();
					String points = Item.getPoints();
				    String option_id = Item.getOption_id();
				    String colorId = Item.getColor_id();
					String mycolor = Item.getMycolor();
					String opponentColorId = Item.getOpponent_color_id();

					double r = Double.parseDouble(rating);
					double p = Double.parseDouble(points);
					holder.title.setText(Item.getTitle());
					holder.options.setText("value " + ":" + dp.format(r) + "" + " bonus" + ":" + dF.format(p));

					convertView.setBackgroundColor(Color.parseColor(mycolor));

				   /* if (colorId.equals(option_id)){
					//holder.color.setText("color me!");
					//convertView.setBackgroundResource(R.drawable.list_selector_green);
					convertView.setBackgroundColor(Color.parseColor(mycolor));}*/
                         //if(opponentColorId==mycolor)
					if (opponentColorId.equals(option_id)){
						convertView.setBackgroundResource(R.drawable.list_selector_orange);}

				}


                         //String test = Item.getColor().toString();
				//Log.d("",""+Item.getColor());

				
		        return convertView;
		    }

		   
		}
