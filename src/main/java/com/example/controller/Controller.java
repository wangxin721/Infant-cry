package com.example.controller;

import com.example.service.Communication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
public class Controller {

//    @Autowired
//    Communication communication;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        System.out.println("1111");
        Communication communication = new Communication();
        // time string for not same name
        String timeStamp = new SimpleDateFormat("MMdd_HHmmss").format(new Date());
        System.out.println(timeStamp);
        File newFile = new File("C:/Users/Gavi/infant/" + timeStamp + file.getOriginalFilename());
        System.out.println("newFile: " + newFile);

        file.transferTo(newFile);

        System.out.println("File uploaded successfully!");

        String name = "C:/Users/Gavi/infant/" + timeStamp + file.getOriginalFilename();
        String result = communication.getResult(name);
//        String result = name;
        System.out.println("result: " + result);
        return result;
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("testststestsetsets");
        return "okokok";
    }


}
