package ru.biriukov.androidclient.dto;

import com.example.apollographqlandroid.LoginUserQuery;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDataDTO implements Serializable {

    private Integer id;

    private String firstName;

    private String secondName;

    private String lastName;

    private Integer groupId;

    private String groupName;

    private List<GroupSubjectDTO> groupSubjects;

    public static StudentDataDTO of(LoginUserQuery.StudentLogin data) {

        LoginUserQuery.Group group = data.group().get(0);

        return StudentDataDTO.builder()
                .id(data.id())
                .firstName(data.firstName())
                .secondName(data.secondName())
                .lastName(data.lastName())
                .groupId(group.id())
                .groupName(group.name())
                .groupSubjects(GroupSubjectDTO.of(group.groupSubjects()))
                .build();
    }

    public String getFullName() {
        String result = lastName + " " + firstName;

        if (secondName != null) {
            result += " " + secondName;
        }

        return result;
    }
}
