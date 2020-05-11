package ojass20.nitjsr.in.ojass.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ojass20.nitjsr.in.ojass.Models.ItinerraryModal;
import ojass20.nitjsr.in.ojass.R;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.Viewholder> {

    private Context context;
    private ArrayList<ItinerraryModal> datalist;

    public ItineraryAdapter(Context context, ArrayList<ItinerraryModal> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_itinerary,parent,false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        if(position%2 == 0){
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);
        }
        else{
            holder.left.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class Viewholder extends RecyclerView.ViewHolder {
        TextView name_odd,name_even,time_odd,time_even,venue_odd,venue_even;
        RelativeLayout left,right;
        Viewholder(@NonNull View itemView) {
            super(itemView);

            name_odd = itemView.findViewById(R.id.event_name_odd);
            name_even = itemView.findViewById(R.id.event_name_even);
            time_odd = itemView.findViewById(R.id.event_time_odd);
            time_even = itemView.findViewById(R.id.event_time_even);
            venue_odd = itemView.findViewById(R.id.event_venue_odd);
            venue_even = itemView.findViewById(R.id.event_venue_even);

            left = itemView.findViewById(R.id.left_side);
            right = itemView.findViewById(R.id.right_side);
        }
    }
}
