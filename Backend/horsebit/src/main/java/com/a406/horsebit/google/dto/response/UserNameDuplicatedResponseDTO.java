package com.a406.horsebit.google.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNameDuplicatedResponseDTO {
    private boolean duplicated;

    @Builder
    public UserNameDuplicatedResponseDTO(boolean duplicated){
        this.duplicated = duplicated;
    }
}
