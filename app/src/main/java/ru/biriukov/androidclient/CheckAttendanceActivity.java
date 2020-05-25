package ru.biriukov.androidclient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.apollographqlandroid.StudentAttendanceQuery;

import org.jetbrains.annotations.NotNull;

import ru.biriukov.androidclient.dto.GroupSubjectDTO;

public class CheckAttendanceActivity extends AppCompatActivity {

    private ApolloClient apolloClient = ApolloClient
            .builder()
            .serverUrl(ApplicationUtils.BASE_URL)
            .build();

    private GroupSubjectDTO groupSubject;

    private Integer studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance);
        groupSubject = (GroupSubjectDTO) getIntent().getSerializableExtra("groupSubject");
        studentId = getIntent().getIntExtra("studentId", -1);
        setTitle("Успеваемость");
        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        TextView teacher = findViewById(R.id.teacher);
        TextView subject = findViewById(R.id.subject);
        TextView event_time = findViewById(R.id.event_time);

        teacher.setText(teacher.getText() + " " + groupSubject.getTeacher().getFullName());
        subject.setText(subject.getText() + " " + groupSubject.getSubject().getName());
        event_time.setText(event_time.getText() + " " + ApplicationUtils.DAYS_OF_WEEK[groupSubject.getDayOfWeek()] + " в " + ApplicationUtils.ORDER_NUMBERS[groupSubject.getOrderNumber()]);

        apolloClient.query(StudentAttendanceQuery.builder().studentId(studentId).groupSubjectId(groupSubject.getId()).build()).enqueue(new ApolloCall.Callback<StudentAttendanceQuery.Data>() {

            @Override
            public void onResponse(@NotNull Response<StudentAttendanceQuery.Data> response) {

                LinearLayout linearLayout = findViewById(R.id.attendance_layout);

                if (response.data().studentAttendance().size() == 0) {
                    TextView textView = new TextView(CheckAttendanceActivity.this);

                    textView.setText("Успеваемость ещё не проставлена.");

                    linearLayout.addView(textView);

                    return;
                }

                response.data()
                        .studentAttendance()
                        .forEach((el) -> {

                            TextView textView = new TextView(CheckAttendanceActivity.this);

                            String data = el.stringData().split(" ")[0];

                            textView.setText(data + " - " + el.mark().markValue());

                            linearLayout.addView(textView);

                        });

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                finishAffinity();
            }
        });


    }
}
