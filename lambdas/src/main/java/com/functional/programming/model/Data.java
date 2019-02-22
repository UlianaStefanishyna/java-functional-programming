package com.functional.programming.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@lombok.Data
@Getter
public class Data {
    Long id;
    String data;
    Long mark;
}