package com.smartcat.internship.airplaneservices.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
public class ApplicationRunner {

    @Autowired
    public ApplicationRunner(){
        Executors.newSingleThreadExecutor().submit(this::run);
    }

    public void run(){
        while (true){
            try {
                // woot
            }catch (Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
    }


}
