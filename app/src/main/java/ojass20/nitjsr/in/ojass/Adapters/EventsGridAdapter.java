package ojass20.nitjsr.in.ojass.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;
import ojass20.nitjsr.in.ojass.Activities.EventsActivity;
import ojass20.nitjsr.in.ojass.Activities.SubEventActivity;
import ojass20.nitjsr.in.ojass.Models.EventsDisplayModel;
import ojass20.nitjsr.in.ojass.R;

public class EventsGridAdapter extends RecyclerView.Adapter<EventsGridAdapter.MyHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<EventsDisplayModel> list;
    int width;
    ItemClickInterface itemClickInterface;

    public EventsGridAdapter(Context context, int width, ArrayList<EventsDisplayModel> list, ItemClickInterface mClickInterface){
        this.mContext = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
        this.width = width;
        this.itemClickInterface = mClickInterface;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= mInflater.inflate(R.layout.item_events_grid,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        final EventsDisplayModel data = list.get(position);
        float alphaVal = data.getAlphaVal();
        holder.iv.setImageResource(data.getEvImg());
        holder.tv.setText(data.getEvName());
        holder.iv.setAlpha(alphaVal);
        holder.tv.setAlpha(1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickInterface.EventsItemClick(position,holder.iv);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class MyHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl;
        ImageView iv;
        TextView tv;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.events_icon);
            rl = itemView.findViewById(R.id.events_layout);
            tv = itemView.findViewById(R.id.events_name);
            WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            int width = display.getWidth()/3;
            int height = width;
            rl.setLayoutParams(new RelativeLayout.LayoutParams(width,height));

        }
    }
    public interface ItemClickInterface{
        void EventsItemClick(int position, ImageView iv);
    }
}
