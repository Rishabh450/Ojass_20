package ojass20.nitjsr.in.ojass.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import ojass20.nitjsr.in.ojass.R;

public class FeedbackActivity extends AppCompatActivity {
    HashMap<String, String> formdata;
    DatabaseReference ref;
    Toolbar toolbar;
    EditText name, email, subject, feedback;
    Button Submit;
    int Count = -1;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        /* to initialize data */
        init();
        initToolbar();
        setListner();
        closeKeyboard();

    }

    private boolean validate() {
        boolean check = true;
        if (TextUtils.isEmpty(email.getText().toString().trim())) {
            Toast.makeText(this, "Email Empty", Toast.LENGTH_LONG).show();

            check = false;
        }
        if(!email.getText().toString().trim().matches(emailPattern)){
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_LONG).show();

            check = false;
        }
        if (TextUtils.isEmpty(name.getText().toString().trim())) {
            Toast.makeText(this, "Name Empty", Toast.LENGTH_LONG).show();
            check = false;
        }
        if (TextUtils.isEmpty(feedback.getText().toString().trim())) {
            Toast.makeText(this, "Feedback empty", Toast.LENGTH_LONG).show();
            check = false;
        }
        if (TextUtils.isEmpty(subject.getText().toString().trim())) {
            Toast.makeText(this, "Subject empty", Toast.LENGTH_LONG).show();
            check = false;
        }
        if (Count == -1) {
            Toast.makeText(this, "Try in a Bit!!!!", Toast.LENGTH_LONG).show();
            check = false;
        } else if (Count >= 5) {
            Toast.makeText(this, "Feedbacks Limit!!!!", Toast.LENGTH_LONG).show();
            check = false;
        }
        return check;
    }

    private void set() {
        formdata = new HashMap<>();
        formdata.put("email", email.getText().toString().trim());
        formdata.put("name", name.getText().toString().trim());
        formdata.put("message", feedback.getText().toString().trim());
        formdata.put("subject", subject.getText().toString().trim());


    }

    private void submit() {
        ref.child("feedback").push().setValue(formdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FeedbackActivity.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                closeKeyboard();
                finish();
            }
        });
    }

    private void setListner() {
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    set();
                    submit();
                }

            }
        });
    }

    private void init() {
        ref = FirebaseDatabase.getInstance().getReference();
        name = findViewById(R.id.FeedBackName);
        email = findViewById(R.id.FeedbackEmail);
        subject = findViewById(R.id.FeedBackSubject);
        feedback = findViewById(R.id.FeedbackFeedBack);
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        name.setKeyListener(null);
        Submit = findViewById(R.id.FeedbackSubmit);
        Log.e("sad: ","heyy");
        Log.e("sad: 2", FirebaseAuth.getInstance().getCurrentUser().getEmail());

        if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(null) ||
                !FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("")) {
            Log.e("sad: 1", FirebaseAuth.getInstance().getCurrentUser().getEmail());
            email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            //email.setEnabled(false);
        }

//        ref.child("Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
//
//                    HashMap<String,String> user=(HashMap<String,String>)dataSnapshot1.getValue();
//                    if(user.get("uid").equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid()))
//                    {
//                        email.setText(user.get("email"));
//                        email.setKeyListener(null);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        ref.child("feedback").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Count = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    HashMap<String, String> feedback = (HashMap<String, String>) dataSnapshot1.getValue();
                    if (feedback.get("name").equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
                        Count++;
                    }
                }
                Submit.setText("SUBMIT(" + (5 - Count) + ")");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    public void closeKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
