package ojass20.nitjsr.in.ojass.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ojass20.nitjsr.in.ojass.Activities.MainActivity;
import ojass20.nitjsr.in.ojass.Activities.SubEventActivity;
import ojass20.nitjsr.in.ojass.Adapters.EventHeadAdapter;
import ojass20.nitjsr.in.ojass.R;

public class CoordianatorFragment extends Fragment {
    TextView heading,body;
    RecyclerView rv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coordinator,container,false);
        heading=view.findViewById(R.id.heading);
        rv=view.findViewById(R.id.rv_eventhead);
        body=view.findViewById(R.id.body);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        if(MainActivity.data.get(SubEventActivity.position).getName().compareTo("DISASSEMBLE")==0){
//            Log.e("TAG",MainActivity.data.get(SubEventActivity.position).getCoordinatorsModelArrayList().get(0).getName());
//        }
        rv.setAdapter(new EventHeadAdapter(getActivity(), MainActivity.data.get(SubEventActivity.position).getCoordinatorsModelArrayList()));
        return view;
    }
}
