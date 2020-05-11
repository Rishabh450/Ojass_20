package ojass20.nitjsr.in.ojass.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ojass20.nitjsr.in.ojass.Adapters.FAQAdapter;
import ojass20.nitjsr.in.ojass.Models.FaqModel;

import ojass20.nitjsr.in.ojass.R;

public class FaqActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FAQAdapter adapter;
    DatabaseReference ref;
    public static ArrayList<FaqModel> data;
    ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        //Picasso.with(this).load(R.drawable.ojass_bg).fit().into((ImageView)findViewById(R.id.iv_faq));

        recyclerView=(RecyclerView)findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        data=new ArrayList<>();

        ref= FirebaseDatabase.getInstance().getReference().child("Faq");
        ref.keepSynced(true);
        p=new ProgressDialog(this);
        p.setMessage("Loading FAQs....");
        p.setCancelable(false);
        p.show();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                p.dismiss();
                data.clear();
                if (dataSnapshot.exists()){
                    try {
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            FaqModel q=ds.getValue(FaqModel.class);
                            data.add(q);

                        }

                        adapter = new FAQAdapter(FaqActivity.this, data);
                        recyclerView.setAdapter(adapter);

                    } catch (Exception e){

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter=new FAQAdapter(FaqActivity.this, data);

        recyclerView.setAdapter(adapter);

        findViewById(R.id.ib_back_faq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

}
