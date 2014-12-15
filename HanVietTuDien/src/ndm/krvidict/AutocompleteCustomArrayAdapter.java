package ndm.krvidict;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
 
public class AutocompleteCustomArrayAdapter extends ArrayAdapter<Word> {
 
    final String TAG = "AutocompleteCustomArrayAdapter.java";
         
    //Context mContext;
    //int layoutResourceId;
    //public  Word data[] = null;
 
    public AutocompleteCustomArrayAdapter(Context mContext, int layoutResourceId, Word[] data) {
 
        super(mContext, layoutResourceId, data);
         
        //this.layoutResourceId = layoutResourceId;
       // this.mContext = mContext;
        //this.data = data;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         
        try{
             
            /*
             * The convertView argument is essentially a "ScrapView" as described is Lucas post 
             * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
             * It will have a non-null value when ListView is asking you recycle the row layout. 
             * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
             */
        	
            if(convertView==null){
                // inflate the layout
                LayoutInflater inflater = ((MainActivity) getContext()).getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_view_row, parent, false);
            }
             
            // object item based on the position
            Word objectItem = getItem(position);
 
            // get the TextView and then set the text (item name) and tag (item ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
            textViewItem.setTag(objectItem.id);
            
             TextView textViewItem1 = (TextView) convertView.findViewById(R.id.textViewItem1);
            textViewItem1.setText(objectItem.mean);
            textViewItem.setText(Html.fromHtml(objectItem.objectName));
           // Toast.makeText(mContext, position+objectItem.objectName, 2000).show();
             
            // in case you want to add some style, you can do something like:
           // textViewItem.setBackgroundColor(Color.CYAN);
             
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        return convertView;
         
    }
    
    
    
    
        
    
    
    
}