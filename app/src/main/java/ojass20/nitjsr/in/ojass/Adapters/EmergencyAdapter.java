package ojass20.nitjsr.in.ojass.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ojass20.nitjsr.in.ojass.Models.EmergencyModel;
import ojass20.nitjsr.in.ojass.R;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.EmergencyHolder> {
    Context mContext;
    ArrayList<EmergencyModel> list;
    public EmergencyAdapter(Context mContext, ArrayList<EmergencyModel> list){
        this.mContext = mContext;
        this.list = list;
    }
    @NonNull
    @Override
    public EmergencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_emergency,parent,false);
        return new EmergencyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyHolder holder, final int position) {
        holder.title.setText(list.get(position).title);
        holder.phno.setText(list.get(position).phone);
        holder.phno.setPaintFlags(holder.phno.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.phno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = list.get(position).phone;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EmergencyHolder extends RecyclerView.ViewHolder{
        TextView title,phno;
        public EmergencyHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.emergency_title);
            phno = itemView.findViewById(R.id.emergency_phone);
        }
    }
}
