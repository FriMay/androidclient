package ru.biriukov.androidclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.apollographqlandroid.LoginUserQuery;

import org.jetbrains.annotations.NotNull;

import ru.biriukov.androidclient.dto.StudentDataDTO;
import ru.biriukov.androidclient.notifications.NotificationService;

public class MainActivity extends AppCompatActivity {

    private ApolloClient apolloClient = ApolloClient
            .builder()
            .serverUrl(ApplicationUtils.BASE_URL)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Авторизация");
    }

    public void login(View view) {

        String login = ((EditText) findViewById(R.id.login)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        if (login.trim().isEmpty()) {
            alert(R.string.login_error);
            return;
        }

        if (password.trim().isEmpty()) {
            alert(R.string.password_error);
            return;
        }

        apolloClient.query(LoginUserQuery.builder().login(login).password(password).build())
                .enqueue(new ApolloCall.Callback<LoginUserQuery.Data>() {

                    @Override
                    public void onResponse(@NotNull Response<LoginUserQuery.Data> response) {

                        if (response.data().studentLogin() == null) {
                            runOnUiThread(() -> alert(R.string.wrong_login_or_password));
                            return;
                        }

                        Intent intent = new Intent(MainActivity.this, SubjectListActivity.class);

                        StudentDataDTO data = StudentDataDTO.of(response.data().studentLogin());

                        Intent serviceIntent = new Intent(MainActivity.this, NotificationService.class);

                        startService(serviceIntent.putExtra("userId", data.getId()).putExtra("groupId", data.getGroupId()));

                        intent.putExtra("data", data);

                        startActivity(intent);

                        finish();
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        finishAffinity();
                    }
                });
    }

    private void alert(int text) {
        Toast.makeText(getApplicationContext(), getString(text), Toast.LENGTH_SHORT).show();
    }
}
