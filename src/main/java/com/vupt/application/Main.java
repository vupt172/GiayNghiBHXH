package com.vupt.application;

import javafx.application.Application;
import javafx.stage.Stage;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication

public class Main extends Application {
    private static ConfigurableApplicationContext applicationContext;
    @Override
    public void init() throws Exception {
        this.applicationContext= SpringApplication.run(Main.class);
        System.setProperty("java.awt.headless", "false");

    }

    @Override
    public void stop() throws Exception {
        applicationContext.close();
    }
    @Override
    public void start(Stage primaryStage) throws Exception{

        MyController.loadView(primaryStage);

    }


    public static void main(String[] args) {
        launch(args);
    }
    public static ConfigurableApplicationContext getApplicationContext(){
        return applicationContext;
    }
  @Bean
    public ModelMapper modelMapper(){
        return  new ModelMapper();
  }
}
