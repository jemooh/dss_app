package com.example.leticia.dss.Adapters;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.leticia.dss.Model.Negotiation;
import com.example.leticia.dss.R;

/**
		 * This adapter is used to show our items objects in a ListView
		 * It hasn't got many memory optimisations, if your list is getting bigger or more complex
		 * you may want to look at better using your view resources: http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/view/List14.html
		 * @author paul.blundell
		 */
		public class NegotiationsAdapter extends BaseAdapter {
			public List<Negotiation> Items;
			DecimalFormat df = new DecimalFormat("#.##");
		    // An inflator to use when creating rows
		    private LayoutInflater mInflater;
		    Context mContext;
		   //public String posy =SingleItemActivity.positionDel;
		    /**
		     * @param context this is the context that the list will be shown in - used to create new list rows
		     * @param Items this is a list of items to display
		     */
		    public NegotiationsAdapter(Context context, List<Negotiation> Items) {
		        this.Items = Items;
		        this.mInflater = LayoutInflater.from(context);
		        this.mContext = context;
		    }
		 
		    
		    public void setListData(List<Negotiation> ListItems){
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
				public TextView title;
			}
		 
		    //@Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		        View view = null; // If convertView wasn't null it means we have already set it to our list_item_user_video so no need to do it again
				if(convertView == null) {
		            // This is the layout we are using for each row in our list anything you declare in this layout can then be referenced below
		            convertView = mInflater.inflate(R.layout.negotiation_row, parent, false);
					
					final ViewHolder viewHolder = new ViewHolder();
					viewHolder.title = (TextView) convertView.findViewById(R.id.txtneg);

					convertView.setTag(viewHolder);
					convertView.setTag(R.id.txtneg, viewHolder.title);
		            
		            //convertView.setTag(R.id.checkBoxStatus, viewHolder.checkorder);
				} 
				
				ViewHolder holder = (ViewHolder) convertView.getTag();
		        // Get a single item from our list
		        Negotiation Item = Items.get(position);

		        holder.title.setText(Item.getTitle());

		        return convertView;
		    }
		    
		   
		}
