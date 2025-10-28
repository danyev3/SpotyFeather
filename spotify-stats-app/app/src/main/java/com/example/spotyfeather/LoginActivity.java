package com.example.spotyfeather;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class LoginActivity extends AppCompatActivity {

    private static final String REDIRECT_URI = "http://com.example.spotyfeather/callback";
    private static final int REQUEST_CODE = 1337;

    private EditText mClientIdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mClientIdEditText = findViewById(R.id.client_id_edit_text);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            String clientId = mClientIdEditText.getText().toString();
            if (clientId.isEmpty()) {
                Toast.makeText(this, "Please enter a CLIENT_ID", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthorizationRequest.Builder builder =
                    new AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

            builder.setScopes(new String[]{"user-top-read"});
            AuthorizationRequest request = builder.build();

            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                case TOKEN:
                    Intent mainActivityIntent = new Intent(this, MainActivity.class);
                    mainActivityIntent.putExtra("ACCESS_TOKEN", response.getAccessToken());
                    startActivity(mainActivityIntent);
                    finish();
                    break;
                case ERROR:
                    // Handle error
                    break;
                default:
                    // Handle cancellation
            }
        }
    }
}
