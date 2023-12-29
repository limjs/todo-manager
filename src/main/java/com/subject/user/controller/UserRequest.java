package com.subject.user.controller;

import com.subject.user.service.SearchUserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public class UserRequest {
    private String name;
    private LocalDate practiceDate;
    private Integer sort; // 오름차순 또는 내림차순 정렬 선택

    public SearchUserDto toDto() {
        SearchUserDto searchUserDto = new SearchUserDto();
        searchUserDto.setName(this.name);
        searchUserDto.setPracticeDate(this.practiceDate);
        searchUserDto.setSort(this.sort);
        return searchUserDto;
    }
}
