package ru.biriukov.androidclient.dto;

import com.example.apollographqlandroid.LoginUserQuery;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO implements Serializable {
    private String firstName;

    private String secondName;

    private String lastName;

    public static TeacherDTO of(LoginUserQuery.Teacher data) {
        return TeacherDTO.builder()
                .firstName(data.firstName())
                .secondName(data.secondName())
                .lastName(data.lastName())
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
