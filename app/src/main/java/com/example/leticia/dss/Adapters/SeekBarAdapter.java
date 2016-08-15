package com.example.leticia.dss.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.leticia.dss.Model.SeekBars;
import com.example.leticia.dss.Model.SubmitPoints;
import com.example.leticia.dss.R;
import com.example.leticia.dss.database.DatabaseHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
		 * This adapter is used to show our items objects in a ListView
		 * It hasn't got many memory optimisations, if your list is getting bigger or more complex
		 * you may want to look at better using your view resources: http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/view/List14.html
		 * @author paul.blundell
		 */
		public class SeekBarAdapter extends BaseAdapter {
			public List<SeekBars> Items;
	        public List<SubmitPoints> values;
	        //public static ArrayList<Myratings> myratings ;
	        DatabaseHandler db;

			DecimalFormat df = new DecimalFormat("#.##");
		    // An inflator to use when creating rows
		    private LayoutInflater mInflater;
		    Context mContext;
		   //public String posy =SingleItemActivity.positionDel;
		    /**
		     * @param context this is the context that the list will be shown in - used to create new list rows
		     * @param Items this is a list of items to display
		     */
		    public SeekBarAdapter(Context context, List<SeekBars> Items) {
		        this.Items = Items;
		        this.mInflater = LayoutInflater.from(context);
		        this.mContext = context;
		    }
		 
		    
		    public void setListData(List<SeekBars> ListItems){
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
				public TextView title, value;
				public SeekBar seekBar;
			}
		 
		    //@Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		        View view = null; // If convertView wasn't null it means we have already set it to our list_item_user_video so no need to do it again
				if(convertView == null) {
		            // This is the layout we are using for each row in our list anything you declare in this layout can then be referenced below
		            convertView = mInflater.inflate(R.layout.seekbar_row, parent, false);
					
					final ViewHolder viewHolder = new ViewHolder();
					viewHolder.title = (TextView) convertView.findViewById(R.id.txtSbtitle);
					viewHolder.value = (TextView) convertView.findViewById(R.id.txtValue);
					viewHolder.seekBar = (SeekBar) convertView.findViewById(R.id.seekBar);

					convertView.setTag(viewHolder);
					convertView.setTag(R.id.txtSbtitle, viewHolder.title);
					convertView.setTag(R.id.textViewpoints, viewHolder.value);
					convertView.setTag(R.id.seekBar, viewHolder.seekBar);

				} 
				
				final ViewHolder holder = (ViewHolder) convertView.getTag();
		        // Get a single video from our list
			    final 	SeekBars Item = Items.get(position);

		        holder.title.setText(Item.getTitle());
				holder.value.setText("0.5");



				holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
						holder.value.setText(""+getConvertedValue(i));
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

						db = new DatabaseHandler(mContext);

						String id = Item.getId();
						String  myrating = holder.value.getText().toString();
						String points = null;
						db.addmyrating(id, myrating, points);





					}
				});

		        return convertView;
		    }


			public String getConvertedValue(int intVal){
				DecimalFormat dF = new DecimalFormat("0.00");
				double floatVal = 0.01 * intVal;
				String value = dF.format(floatVal);

				return value ;
			}
		    
		   
		}
