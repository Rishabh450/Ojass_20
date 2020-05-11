package ojass20.nitjsr.in.ojass.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ojass20.nitjsr.in.ojass.Models.CoordinatorsModel;
import ojass20.nitjsr.in.ojass.Models.EventHeadModal;
import ojass20.nitjsr.in.ojass.R;

public class EventHeadAdapter extends RecyclerView.Adapter<EventHeadAdapter.ViewHolder> {
    Context context;
    ArrayList<CoordinatorsModel> eventHead;

    public EventHeadAdapter(Context context, ArrayList<CoordinatorsModel> eventHead) {
        this.context = context;
        this.eventHead = eventHead;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_event_head_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.name.setText(eventHead.get(position).getName());
        holder.number.setText(eventHead.get(position).getPhone());
        try {
            holder.number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + eventHead.get(position).getPhone()));
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return eventHead.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.profile_number);
        }
    }
}
