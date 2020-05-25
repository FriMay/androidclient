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
public class SubjectDTO implements Serializable {
    private Integer id;

    private String name;

    public static SubjectDTO of(LoginUserQuery.Subject data) {
        return SubjectDTO.builder()
                .id(data.id())
                .name(data.subjectName())
                .build();
    }

}
