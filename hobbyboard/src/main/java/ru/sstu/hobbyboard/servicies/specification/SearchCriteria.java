package ru.sstu.hobbyboard.servicies.specification;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SearchCriteria {
    private final String key;
    private final String operation;
    private final Object value;

    private String secondKey;
    private Object secondValue;
}