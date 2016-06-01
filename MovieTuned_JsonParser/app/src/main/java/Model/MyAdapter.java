package Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apps.alpinist.movietuned_jsonparser.R;

import java.util.ArrayList;

import BusinessObjects.MovieListItem;

/**
 * Created by Hassan Khalid on 27/05/2016.
 */
public class MyAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<MovieListItem> data;

    public MyAdapter(Context context, int textViewResourceId, ArrayList<MovieListItem> arr) {
        super(context, textViewResourceId, arr);

        this.context = context;
        this.data = arr;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        try {
            if (view == null) { //inflate layout

                //get inflater service
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_view_item, null, true);

                //set viewHolder
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.cover_image_path = (TextView)view.findViewById(R.id.imagepath);
                viewHolder.title = (TextView)view.findViewById(R.id.movietitle);
                viewHolder.id = (TextView)view.findViewById(R.id.id);

                view.setTag(viewHolder);
            }

            ViewHolder vh = (ViewHolder)view.getTag();
            vh.title.setText(data.get(position).getTitle());
            vh.cover_image_path.setText(data.get(position).getCover_path());
            vh.id.setText(data.get(position).getId());

        }catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    static class ViewHolder{
        TextView title, id, cover_image_path;
    }
}
