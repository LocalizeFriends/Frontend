package com.example.friendlocation.friendlocation;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.friendlocation.friendlocation.activities.DashActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.login_button) LoginButton loginButton;
    @BindView(R.id.text1) TextView t;
    CallbackManager callbackManager;
    static String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //String tokenFb = FirebaseInstanceId.getInstance().getToken();
        //Log.v("token:", tokenFb);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.friendlocation.friendlocation",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile","email", "read_custom_friendlists", "user_friends");

        AccessToken token = AccessToken.getCurrentAccessToken();
        if(token!= null){
            t.setText(token.getToken() +"\n" +
                    token.getUserId());
            user_id = token.getUserId();
            startActivity(new Intent(getBaseContext(), DashActivity.class));
        }
        else {
            logIn();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public void logIn(){
    LoginManager.getInstance().registerCallback(callbackManager,
            new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    t.setText(loginResult.getAccessToken().getToken() +
                            loginResult.getAccessToken().getUserId()
                            );
                    startActivity(new Intent(getBaseContext(), DashActivity.class));
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(getBaseContext(), "Incorect facebook log in try ", Toast.LENGTH_SHORT).show();
                }
            });
    }

}
