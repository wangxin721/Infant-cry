package com.example.demo;

import com.example.service.Communication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    Communication communication;

    @Test
    void t() throws IOException {
        String s = communication.getResult("D:/myuniversity/myrecording.mp3");
        System.out.println(s);
    }

}
