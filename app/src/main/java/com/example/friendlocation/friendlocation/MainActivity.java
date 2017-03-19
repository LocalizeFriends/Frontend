package com.example.friendlocation.friendlocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.friendlocation.friendlocation.activities.DashActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile","email", "read_custom_friendlists");

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
                    // App code
                }
            });
    }

}
