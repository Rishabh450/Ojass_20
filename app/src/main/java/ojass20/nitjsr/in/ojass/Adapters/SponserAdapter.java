package ojass20.nitjsr.in.ojass.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ojass20.nitjsr.in.ojass.R;

public class SponserAdapter extends RecyclerView.Adapter<SponserAdapter.ViewHolder> {
    ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();
    Context context;

    public SponserAdapter(ArrayList<Map<String, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_sponser,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // Log.d("ak47", "onBindViewHolder: "+list.get(position).get("Title"));
        holder.sponser_name.setText(list.get(position).get("Name"));
        Glide.with(context).asBitmap().load(list.get(position).get("Logo")).transform(new RoundedCorners(4)).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(holder.sponser_pic);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView sponser_pic;
        TextView sponser_name;
        FrameLayout frameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sponser_pic=itemView.findViewById(R.id.single_sponser_image);
            sponser_name=itemView.findViewById(R.id.single_sponser_name);

        }
    }
}
