package ojass20.nitjsr.in.ojass.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ojass20.nitjsr.in.ojass.Activities.MainActivity;
import ojass20.nitjsr.in.ojass.Activities.SubEventActivity;
import ojass20.nitjsr.in.ojass.R;

public class AboutFragment extends Fragment {
    private TextView abt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
//        Toast.makeText(getContext(), "" + SubEventActivity.position, Toast.LENGTH_SHORT).show();
        createUI(view);
        return view;
    }

    private void createUI(View view) {

        TextView t2 = view.findViewById(R.id.text_about);
//        Typeface typeface= Typeface.createFromAsset(getActivity().getAssets(), "textfont.otf");
//        t2.setTypeface(typeface);
        t2.setText(Html.fromHtml(MainActivity.data.get(SubEventActivity.position).getAbout()));
    }
}
