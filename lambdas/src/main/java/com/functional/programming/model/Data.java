package com.functional.programming.model;

import lombok.Builder;

@Builder
@lombok.Data
public class Data {
    Long id;
    String data;
    Long mark;
}