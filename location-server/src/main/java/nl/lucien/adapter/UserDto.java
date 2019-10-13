package nl.lucien.adapter;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private String name;
    private List<String> keywords;
}
