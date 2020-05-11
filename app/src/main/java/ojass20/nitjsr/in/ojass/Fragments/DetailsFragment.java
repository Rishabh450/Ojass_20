package ojass20.nitjsr.in.ojass.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import ojass20.nitjsr.in.ojass.Activities.MainActivity;
import ojass20.nitjsr.in.ojass.Activities.SubEventActivity;
import ojass20.nitjsr.in.ojass.R;

public class DetailsFragment extends Fragment {

    private RemotePDFViewPager remotePDFViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        createUI(view);
        return view;
    }

    private void createUI(View view) {

        TextView t2 = view.findViewById(R.id.text_details);
        t2.setText(Html.fromHtml(MainActivity.data.get(SubEventActivity.position).getDetails()));
        try {
            final String url = MainActivity.data.get(SubEventActivity.position).getLink();
            TextView textView = view.findViewById(R.id.link);
            textView.setText("Link to problem statement");
            if(url==null || url.compareTo("")==0){
                textView.setVisibility(View.GONE);
            }else{
                textView.setVisibility(View.VISIBLE);
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        } catch (Exception e) {
            view.findViewById(R.id.link).setVisibility(View.GONE);
        }
    }
}
