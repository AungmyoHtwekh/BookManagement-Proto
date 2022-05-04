package com.aungmyohtwe.note.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResponseStatus {
    private String status;
    private String description;
    private String code;
}
