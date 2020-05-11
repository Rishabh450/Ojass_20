package ojass20.nitjsr.in.ojass.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ojass20.nitjsr.in.ojass.R;
import ojass20.nitjsr.in.ojass.Utils.Constants;

import static ojass20.nitjsr.in.ojass.Utils.Utilities.setGlideImage;

public class AboutActivity extends AppCompatActivity {
    ImageButton fb;
    ImageButton insta;
    ImageButton twitter;
    ImageButton webpage;
    ImageButton share, helpdesk_phone1,helpdesk_phone2, rateUs;
    ImageView helpdesk_whatsapp1,helpdesk_whatsapp2;
    Toolbar toolbar;
    private static final String MRIDUL_PHONE = "7992425023";
    private static final String DIVYANSH_PHONE = "6299079553";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        init();
        setToolbar();



        TextView t1 = (TextView) findViewById(R.id.description);




        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Ojassnitjamshedpur/?ref=br_rs"));
                startActivity(intent);
            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ojass_techfest/"));
                startActivity(intent);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ojass_nitjsr?s=08"));
                startActivity(intent);
            }
        });

        webpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ojass.in"));
                startActivity(intent);
            }
        });

        helpdesk_phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91"+MRIDUL_PHONE));
                startActivity(intent);
            }
        });
        helpdesk_phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91"+DIVYANSH_PHONE));
                startActivity(intent);
            }
        });

        helpdesk_whatsapp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_VIEW);
                String url = "https://api.whatsapp.com/send?phone=+91" + MRIDUL_PHONE + "&text=Hey! I'm "+ FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+".";
                sendIntent.setData(Uri.parse(url));
                startActivity(sendIntent);
            }
        });
        helpdesk_whatsapp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_VIEW);
                String url = "https://api.whatsapp.com/send?phone=+91" + DIVYANSH_PHONE + "&text=Hey! I'm "+ FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+".";
                sendIntent.setData(Uri.parse(url));
                startActivity(sendIntent);
            }
        });

    }



    private void init() {
        fb =  findViewById(R.id.fb);
        insta =  findViewById(R.id.insta);
        twitter =  findViewById(R.id.twitter);
        webpage =  findViewById(R.id.web);
//        back =  findViewById(R.id.ib_back_about_us);
//        share =  findViewById(R.id.ib_app_share);
        helpdesk_phone1 =  findViewById(R.id.ib_helpdesk_phone1);
        helpdesk_phone2 = findViewById(R.id.ib_helpdesk_phone2);
        helpdesk_whatsapp1 =  findViewById(R.id.ib_helpdesk_whatsapp1);
        helpdesk_whatsapp2 = findViewById(R.id.ib_helpdesk_whatsapp2);
        toolbar = findViewById(R.id.toolbar);
    }
    private void setToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("About Us");
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }else if(item.getItemId() == R.id.rate_us){
            String appPackageName = Constants.PACKAGE_NAME;
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

        }else if(item.getItemId() == R.id.share){
            String playStoreURL = Constants.PLAYSTORE_LINK;
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Ojass");
            i.putExtra(Intent.EXTRA_TEXT, playStoreURL);
            startActivity(Intent.createChooser(i, ""));
        }

        return true;
    }
}
