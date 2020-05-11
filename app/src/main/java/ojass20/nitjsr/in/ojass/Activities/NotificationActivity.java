package ojass20.nitjsr.in.ojass.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import ojass20.nitjsr.in.ojass.Adapters.NotificationAdapter;
import ojass20.nitjsr.in.ojass.Models.NotificationModal;
import ojass20.nitjsr.in.ojass.R;

import static ojass20.nitjsr.in.ojass.Utils.Constants.FIREBASE_REF_NOTIF;
import static ojass20.nitjsr.in.ojass.Utils.Constants.eventNames;
import static ojass20.nitjsr.in.ojass.Utils.Constants.subscribedEvents;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog p;
    Spinner spinner;
    DatabaseReference ref;
    ArrayList<NotificationModal> data, displayData = new ArrayList<>();
    NotificationAdapter adapter;
    TextView no_noti_text;
    ArrayList<String> notiList = new ArrayList<>();
    private boolean firstTymOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstTymOpen = getIntent().getBooleanExtra("nottap", false);
        setContentView(R.layout.activity_notification);

        init();
        getEventList();
        setRecyclerView();

        findViewById(R.id.ib_back_feed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstTymOpen) {
                    startActivity(new Intent(NotificationActivity.this, MainActivity.class));
                    finish();
                } else {
                    finish();
                }
            }
        });

    }

    private void getEventList() {
        if (eventNames.size() != 18) {
            eventNames.clear();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Branches");
            ref.keepSynced(true);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().equalsIgnoreCase("National College Film Festival"))
                            continue;
                        boolean z = false;
                        for (int i = 0; i < eventNames.size(); i++) {
                            if (eventNames.get(i).equals(ds.getKey())) {
                                z = true;
                                break;
                            }
                        }
                        if (!z) {
                            eventNames.add(ds.getKey());
                        }
                    }
                    setSpinner();
                    getData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            setSpinner();
            getData();
        }
    }

    private void getData() {
        p.setMessage("Loading Feed....");
        p.setCancelable(true);
        p.show();
        ref = FirebaseDatabase.getInstance().getReference().child(FIREBASE_REF_NOTIF);
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                p.dismiss();
                data.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    NotificationModal model = ds.getValue(NotificationModal.class);
                    data.add(model);
                }
                Collections.reverse(data);
                setInitList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setRecyclerView() {
        adapter = new NotificationAdapter(NotificationActivity.this, displayData);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        recyclerView.setAdapter(adapter);
    }

    private void setSpinner() {
        notiList.clear();
        notiList.addAll(eventNames);
        notiList.add(0, "Ojass");
        notiList.add(0, "Subscribed");
        notiList.add(0, "All");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, notiList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onItemSelect();
                if (displayData.size() == 0) {
                    no_noti_text.setVisibility(View.VISIBLE);
                } else {
                    no_noti_text.setVisibility(View.GONE);
                }
                //  Toast.makeText(getApplication(),spinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void init() {
        recyclerView = findViewById(R.id.recycler_view);
        spinner = findViewById(R.id.spinner_feed);
        no_noti_text = findViewById(R.id.no_noti_text);
        p = new ProgressDialog(this);
        data = new ArrayList<>();
    }

    public void onItemSelect() {
        displayData.clear();
        String sel = (String) spinner.getSelectedItem();

        for (int i = 0; i < data.size(); i++) {
            if (sel.compareTo("All") == 0) {
                displayData.add(data.get(i));
                continue;
            }
            if (sel.compareTo("Subscribed") == 0) {
                if (subscribedEvents.contains(data.get(i).getEvent())) {
                    displayData.add((data.get(i)));
                }
            }
            if (data.get(i).getEvent().compareTo(sel) == 0) {
                displayData.add(data.get(i));
            }
        }

//        adapter = new NotificationAdapter(NotificationActivity.this, displayData);
//        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void setInitList() {

        String sel = (String) spinner.getSelectedItem();
        displayData.clear();

        for (int i = 0; i < data.size(); i++) {
            if (sel.compareTo("All") == 0) {
                displayData.add(data.get(i));
                continue;
            }
            if (data.get(i).getEvent().compareTo(sel) == 0) {
                displayData.add(data.get(i));
            }
        }
        if (displayData.size() == 0) {
            no_noti_text.setVisibility(View.VISIBLE);
        } else {
            no_noti_text.setVisibility(View.GONE);
        }
//        adapter = new NotificationAdapter(NotificationActivity.this, displayData);
//        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (firstTymOpen) {
            startActivity(new Intent(NotificationActivity.this, MainActivity.class));
            finish();
        } else {
            finish();
        }
    }
}
