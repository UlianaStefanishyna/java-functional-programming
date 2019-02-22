package com.functional.programming;

import com.functional.programming.model.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class ParentChildrenMappingTest {

    private ParentChildrenMapping parentChildrenMapping;
    private List<Data> dataList;

    @Before
    public void setUp() {
        dataList = Arrays.asList(
                Data.builder().id(1L).data("data1").mark(2L).build(), // child
                Data.builder().id(2L).data("data2").mark(null).build(), // parent
                Data.builder().id(3L).data("data3").mark(2L).build(), // child
                Data.builder().id(4L).data("data4").mark(2L).build(), // child
                Data.builder().id(5L).data("data5").mark(4L).build(), // child
                Data.builder().id(6L).data("data6").mark(null).build()); // usual object
        parentChildrenMapping = ParentChildrenMapping.of(dataList);
    }

    @Test
    public void initTest() {

    }
}