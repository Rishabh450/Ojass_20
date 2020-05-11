package ojass20.nitjsr.in.ojass.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ojass20.nitjsr.in.ojass.Activities.MainActivity;
import ojass20.nitjsr.in.ojass.Activities.SubEventActivity;
import ojass20.nitjsr.in.ojass.R;

public class RulesFragment extends Fragment {
    private LinearLayout rules_layout;
//    private BtmNavVisCallback mCallback;
    private TextView rules;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rules,container,false);
        createUI(view);
        return view;
    }

    private void createUI(View view) {
//        TextView t1= view.findViewById(R.id.title_rules);
//        Typeface customFontBold= Typeface.createFromAsset(getActivity().getAssets(),"Ojass.otf");
//        t1.setTypeface(customFontBold);
        TextView t2= view.findViewById(R.id.text_rules);
//        Typeface typeface= Typeface.createFromAsset(getActivity().getAssets(),"textfont.otf");
//        t2.setTypeface(typeface);
        String rules="";

        for(int i = 0; i< MainActivity.data.get(SubEventActivity.position).getRulesModels().size(); i++)
        {
            rules=rules+"<br>"+(i+1)+". "+ MainActivity.data.get(SubEventActivity.position).getRulesModels().get(i).getText();
        }

        t2.setText(Html.fromHtml(rules));
        // t2.setText("Rules");
    }
}
