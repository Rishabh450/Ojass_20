package ojass20.nitjsr.in.ojass.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ojass20.nitjsr.in.ojass.Models.NotificationModal;
import ojass20.nitjsr.in.ojass.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    ArrayList<NotificationModal> datalist;
    Context context;

    public NotificationAdapter(Context context, ArrayList<NotificationModal> datalist) {
        this.datalist = datalist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_notif, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            NotificationModal model = datalist.get(position);
            holder.event_name.setText(datalist.get(position).getEvent());
            holder.header.setText(model.getQues());
            holder.body.setText(model.getAns());
            holder.root.getBackground().setAlpha(50);
            boolean isExpanded = datalist.get(position).isExplandable();
            holder.footer_layout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationModal data = datalist.get(position);
                    data.setExplandable(!data.isExplandable());
                    notifyItemChanged(position);
                }
            });
        }catch (Exception e){}

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout root, footer_layout;
        TextView header, body, event_name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            body = itemView.findViewById(R.id.body);
            root = itemView.findViewById(R.id.root);
            event_name = itemView.findViewById(R.id.event_name);
            footer_layout = itemView.findViewById(R.id.footer_layout);
        }
    }
}