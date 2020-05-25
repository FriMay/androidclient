package ru.biriukov.androidclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.biriukov.androidclient.dto.GroupSubjectDTO;
import ru.biriukov.androidclient.dto.StudentDataDTO;

public class SubjectListActivity extends AppCompatActivity {

    private StudentDataDTO data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);
        setTitle("Список предметов");

        data = (StudentDataDTO) getIntent().getSerializableExtra(
                "data");

        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        TextView group = findViewById(R.id.group);
        TextView student = findViewById(R.id.student);

        group.setText(group.getText() + " " + data.getGroupName());
        student.setText(student.getText() + " " + data.getFullName());

        Map<String, String> map;
        // коллекция для групп
        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
        // заполняем коллекцию групп из массива с названиями групп

        for (String groupName : ApplicationUtils.DAYS_OF_WEEK) {
            // заполняем список атрибутов для каждой группы
            map = new HashMap<>();
            map.put("groupName", groupName); // время года
            groupDataList.add(map);
        }

        // список атрибутов групп для чтения
        String[] groupFrom = new String[]{"groupName"};
        // список ID view-элементов, в которые будет помещены атрибуты групп
        int[] groupTo = new int[]{android.R.id.text1};

        // создаем общую коллекцию для коллекций элементов
        ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();

        // в итоге получится сhildDataList = ArrayList<сhildDataItemList>

        // создаем коллекцию элементов для первой группы

        // заполняем список атрибутов для каждого элемента

        final List<List<GroupSubjectDTO>> groupSubjects = new ArrayList<>();

        for (int i = 1; i <= ApplicationUtils.DAYS_OF_WEEK.length; ++i) {
            ArrayList<Map<String, String>> childData = new ArrayList<>();
            List<GroupSubjectDTO> subjectDTOS = new ArrayList<>();

            for (GroupSubjectDTO dto : data.getGroupSubjects()) {
                if (i > dto.getDayOfWeek()) {
                    continue;
                }
                if (i < dto.getDayOfWeek()) {
                    break;
                }

                subjectDTOS.add(dto);

                String text = ApplicationUtils.ORDER_NUMBERS[dto.getOrderNumber() - 1] + ". ";
                text += dto.getSubject().getName() + ". ";
                text += dto.getTeacher().getFullName();

                map = new HashMap<>();
                map.put("subject", text);
                childData.add(map);
            }

            groupSubjects.add(subjectDTOS);

            сhildDataList.add(childData);
        }


        // список атрибутов элементов для чтения
        String[] childFrom = new String[]{"subject"};
        // список ID view-элементов, в которые будет помещены атрибуты
        // элементов
        int[] childTo = new int[]{android.R.id.text1};

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this, groupDataList,
                android.R.layout.simple_expandable_list_item_1, groupFrom,
                groupTo, сhildDataList, android.R.layout.simple_list_item_1,
                childFrom, childTo);

        ExpandableListView expandableListView = findViewById(R.id.dayOfWeek);

        expandableListView.setOnChildClickListener((a, b, row, column, e) -> {

            Intent intent = new Intent(this, CheckAttendanceActivity.class);

            intent.putExtra("groupSubject", groupSubjects.get(row).get(column));
            intent.putExtra("studentId", data.getId());

            startActivity(intent);

            return true;
        });

        expandableListView.setAdapter(adapter);

    }
}
