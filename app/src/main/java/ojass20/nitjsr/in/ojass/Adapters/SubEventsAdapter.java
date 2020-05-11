package ojass20.nitjsr.in.ojass.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ojass20.nitjsr.in.ojass.Activities.SubEventActivity;
import ojass20.nitjsr.in.ojass.Fragments.EventBottomSheet;
import ojass20.nitjsr.in.ojass.Models.SubEventsModel;
import ojass20.nitjsr.in.ojass.R;
import ojass20.nitjsr.in.ojass.Utils.RecyclerClickInterface;

public class SubEventsAdapter extends RecyclerView.Adapter<SubEventsAdapter.MyViewHolder> {
    Context mCtx;
    ArrayList<SubEventsModel> data;
    RecyclerClickInterface mInterface;
    public SubEventsAdapter(Context mContext, ArrayList<SubEventsModel> data, RecyclerClickInterface mInterface) {
        this.mCtx = mContext;
        this.data = data;
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mCtx).inflate(R.layout.item_sub_events,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        SubEventsModel subEvent = data.get(position);
        holder.subName.setText(subEvent.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Interface",""+position);
                mInterface.onLayoutClick(v,position);
            }
        });
    }




    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView subName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subName = itemView.findViewById(R.id.sub_ev_name);
        }
    }
}
