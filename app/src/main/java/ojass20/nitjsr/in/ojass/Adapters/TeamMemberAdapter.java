package ojass20.nitjsr.in.ojass.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;
import ojass20.nitjsr.in.ojass.Models.TeamMember;
import ojass20.nitjsr.in.ojass.R;

import static ojass20.nitjsr.in.ojass.Utils.Utilities.setGlideImage;

public class TeamMemberAdapter extends PagerAdapter {

    OnClickItem onClickItem;
    ArrayList<TeamMember> list;
    Context context;
    boolean side,dev;
    ImageView profile_upper;
    TextView name_upper,designation_upper;
    ImageView call_upper,whatsapp_upper,facebook_upper,arrow_left,arrow_right;

    public TeamMemberAdapter(Context context,OnClickItem monclick,ArrayList<TeamMember> list){
        this.context = context;
        this.list = list;
        this.onClickItem=monclick;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.team_page_upper_item,container,false);
        profile_upper=v.findViewById(R.id.profile_image_team_member_upper);
        name_upper=v.findViewById(R.id.team_member_name_upper);
        designation_upper=v.findViewById(R.id.team_member_designation_upper);
        call_upper=v.findViewById(R.id.team_member_call);
        whatsapp_upper=v.findViewById(R.id.team_member_whatsapp);
        facebook_upper=v.findViewById(R.id.team_member_facebook);
        arrow_left=v.findViewById(R.id.left_arrow_team);
        arrow_right=v.findViewById(R.id.right_arrow_team);
        if(position==0){
            arrow_left.setVisibility(View.GONE);
        }
        if(position==list.size()-1){
            arrow_right.setVisibility(View.GONE);
        }

        name_upper.setText(list.get(position).name);
        designation_upper.setText(list.get(position).desig);
        setGlideImage(context,list.get(position).img,profile_upper);
        if(getEmailUsers(position)){
            call_upper.setImageResource(R.drawable.gmail_icon);
            whatsapp_upper.setImageResource(R.drawable.ic_icon_instagram);
            if(list.get(position).desig.compareTo("Technical Secretary")!=0){
                whatsapp_upper.setVisibility(View.GONE);
                facebook_upper.setVisibility(View.GONE);
            }
        }
//        Glide.with(context).asBitmap().fitCenter().load(list.get(position).img).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(profile_upper);
        call_upper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(getEmailUsers(position)){
                        if(!list.get(position).email.isEmpty()){
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + list.get(position).email));
                            context.startActivity(i.createChooser(i, "Choose an Email Client"));
                        }else{
                            Toast.makeText(context, "Not available", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        String phone = list.get(position).call;
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        context.startActivity(intent);
                    }
                }catch (Exception e){Toast.makeText(context, "Not Available", Toast.LENGTH_SHORT).show();}

            }
        });
        whatsapp_upper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                try {
                    if(getEmailUsers(position)){
                        Intent likeIng = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).insta));
                        context.startActivity(likeIng);
                    }else {
                        i = new Intent(Intent.ACTION_VIEW);
                        String url = "https://api.whatsapp.com/send?phone=+91" + list.get(position).whatsapp;
//                   Toast.makeText(context, url, Toast.LENGTH_SHORT).show();

                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    }


                }catch (Exception e){
                    Toast.makeText(context, "Not Available", Toast.LENGTH_SHORT).show();
                }
                }

        });
        facebook_upper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(list.get(position).facebook));
                    if (dev)
                        viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(list.get(position).github));
                    context.startActivity(viewIntent);

                }catch (Exception e){
                    Toast.makeText(context, "Not Available Now", Toast.LENGTH_SHORT).show();
                }

            }
        });
        arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onSelected(position,true);
            }
        });
        arrow_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onSelected(position,false);
            }
        });
        container.addView(v);
        return v;
    }

    private boolean getEmailUsers(int position) {
        if(list.get(position).desig.compareTo("Technical Secretary")==0 || list.get(position).team==0)
            return true;
        return false;
    }

    //    public TeamMemberAdapter(Context context,OnClickItem onClickItem, ArrayList<TeamMember> list,boolean side,boolean dev) {
//        this.context=context;
//        this.onClickItem = onClickItem;
//        this.list = list;
//        this.side = side;
//        this.dev = dev;
//    }
//
//
//
//    @NonNull
//    @Override
//    public TeamMemberViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view;
//        if(side){
//            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_tab,parent,false);
//        }
//        else{
//            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.team_page_upper_item,parent,false);
//        }
//        TeamMemberViewModel viewModel=new TeamMemberViewModel(view,side);
//        return viewModel;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TeamMemberViewModel holder, final int position) {
//        if(side){
//            holder.name.setText(list.get(position).name);
//            holder.designation.setText(list.get(position).desig);
//            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onClickItem.onSelected(position);
//                }
//            });
//            Glide.with(context).asBitmap().fitCenter().load(list.get(position).img).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(holder.imageView);
//            //holder.imageView.setImageResource(list.get(position).img);
//        }
//        else {
//            holder.name_upper.setText(list.get(position).name);
//            holder.designation_upper.setText(list.get(position).desig);
//            Glide.with(context).asBitmap().fitCenter().load(list.get(position).img).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(holder.profile_upper);
//            holder.call_upper.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String phone = list.get(position).call;
//                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
//                    context.startActivity(intent);
//                }
//            });
//            holder.whatsapp_upper.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String url = "https://api.whatsapp.com/send?phone="+list.get(position).whatsapp;
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse(url));
//                    context.startActivity(i);
//                }
//            });
//            holder.facebook_upper.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent viewIntent = new Intent("android.intent.action.VIEW",Uri.parse(list.get(position).facebook));
//                    if(dev)
//                        viewIntent=new Intent("android.intent.action.VIEW",Uri.parse(list.get(position).github));
//                    context.startActivity(viewIntent);
//                }
//            });
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public static class TeamMemberViewModel extends RecyclerView.ViewHolder {
//        ImageView imageView;
//        LinearLayout linearLayout;
//        TextView name,designation;
//
//        ImageView profile_upper;
//        TextView name_upper,designation_upper;
//        ImageView call_upper,whatsapp_upper,facebook_upper;
//        public TeamMemberViewModel(@NonNull View itemView,boolean side) {
//            super(itemView);
//            if (side) {
//                imageView=itemView.findViewById(R.id.single_team_member_image);
//                name=itemView.findViewById(R.id.single_team_member_name);
//                designation=itemView.findViewById(R.id.single_team_member_desig);
//                linearLayout=itemView.findViewById(R.id.singleItemTeamMember);
//            }
//            else {
//                profile_upper=itemView.findViewById(R.id.profile_image_team_member_upper);
//                name_upper=itemView.findViewById(R.id.team_member_name_upper);
//                designation_upper=itemView.findViewById(R.id.team_member_designation_upper);
//                call_upper=itemView.findViewById(R.id.team_member_call);
//                whatsapp_upper=itemView.findViewById(R.id.team_member_whatsapp);
//                facebook_upper=itemView.findViewById(R.id.team_member_facebook);
//            }
//        }
//    }
    public interface OnClickItem{
        public void onSelected(int position,boolean side);
    }
}
