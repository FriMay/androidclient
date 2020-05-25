package ru.biriukov.androidclient.dto;

import com.example.apollographqlandroid.LoginUserQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupSubjectDTO implements Serializable {
    private Integer id;

    private Integer dayOfWeek;

    private Integer orderNumber;

    private TeacherDTO teacher;

    private SubjectDTO subject;

    public static GroupSubjectDTO of(LoginUserQuery.GroupSubject data) {
        return GroupSubjectDTO.builder()
                .id(data.id())
                .dayOfWeek(data.dayOfWeek())
                .orderNumber(data.orderNumber())
                .teacher(TeacherDTO.of(data.teacher()))
                .subject(SubjectDTO.of(data.subject()))
                .build();
    }

    public static List<GroupSubjectDTO> of(List<LoginUserQuery.GroupSubject> groupSubjects) {
        final List<GroupSubjectDTO> groupSubjectDTOS = new ArrayList<>();

        groupSubjects.forEach((gs) -> {
            groupSubjectDTOS.add(GroupSubjectDTO.of(gs));
        });

        groupSubjectDTOS.sort((a, b) -> {
            if (!a.dayOfWeek.equals(b.dayOfWeek)) {
                return a.dayOfWeek - b.dayOfWeek;
            }
            return a.orderNumber - b.orderNumber;
        });

        return groupSubjectDTOS;
    }
}
