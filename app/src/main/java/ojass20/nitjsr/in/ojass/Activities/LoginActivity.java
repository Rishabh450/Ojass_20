package ojass20.nitjsr.in.ojass.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import ojass20.nitjsr.in.ojass.R;

public class LoginActivity extends AppCompatActivity {

    CardView google_signin_btn,facebook_signin_btn;
    private GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN = 7;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    boolean ans=false;
    private ProgressDialog mprogressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialise();
        setProgressDialog();

        FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

        google_signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mCallbackManager = CallbackManager.Factory.create();

        facebook_signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("VIVZ", "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("VIVZ", "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("VIVZ", "facebook:onError", error);
                    }
                });
            }
        });

    }

    private void setProgressDialog() {
        mprogressDialog = new ProgressDialog(this);
        mprogressDialog.setMessage("Please wait...");
        mprogressDialog.setCancelable(false);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            sendToMainActicity();
        }
    }

    private void sendToMainActicity() {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    }

    private void sendToRegisteredActivity(){
        startActivity(new Intent(LoginActivity.this,RegistrationPage.class));
        finish();
    }

    private void initialise() {
        google_signin_btn=findViewById(R.id.google_sign_in_btn);
        facebook_signin_btn=findViewById(R.id.facebook_sign_in_btn);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        mprogressDialog.show();

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            mprogressDialog.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("VIVZ", "Google sign in failed "+e.getMessage()+" due to "+task.getException());
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                if(mprogressDialog.isShowing()){
                    mprogressDialog.dismiss();
                }
            }
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("VIVZ", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("VIVZ", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //check if user is registered and then move to respective activity
                            if(mprogressDialog.isShowing()){
                                mprogressDialog.dismiss();
                            }
                            registeredUser();
//                            if(registeredUser()){
//                                sendToMainActicity();
//                            }
//                            else{
//                                sendToRegisteredActivity();
//                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("VIVZ", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            if(mprogressDialog.isShowing()){
                                mprogressDialog.dismiss();
                            }
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("VIVZ", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("VIVZ", "signInWithCredential:success");
                            Toast.makeText(LoginActivity.this, "Authentication successful", Toast.LENGTH_LONG).show();


                            if(mprogressDialog.isShowing()){
                                mprogressDialog.dismiss();
                            }
                            //check if user is registered and then move to respective activity
                            registeredUser();
//                            if(registeredUser()){
//                                sendToMainActicity();
//                            }
//                            else{
//                                sendToRegisteredActivity();
//                            }
                        } else {
                            Log.w("VIVZ", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed due to "+task.getException(), Toast.LENGTH_LONG).show();
                            if(mprogressDialog.isShowing()){
                                mprogressDialog.dismiss();
                            }
                        }
                    }
                });
    }

    private void registeredUser() {
        //boolean ans=false;
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Users");
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).exists()){
                    Log.e("46onDataChange: ","heyy");
                    sendToMainActicity();
                }
                else {
                    sendToRegisteredActivity();
                }
//                for(DataSnapshot ds:dataSnapshot.getChildren())
//                {
//                    Log.e("onDataChange: ",mAuth.getCurrentUser().getUid());
//                    Log.e("onDataChange: ",ds.getKey());
//
//                    if(mAuth.getCurrentUser().getUid().equals(ds.getKey())){
//                        Log.e("onDataChange: ","heyy");
//                        sendToMainActicity();
//                        break;
//                    }
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //sendToRegisteredActivity();
//        if(ans)
//        Log.e( "registeredUser: ","true");
//        else Log.e("registeredUser: ","false");
//        return false;
    }
}
