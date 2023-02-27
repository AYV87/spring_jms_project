package com.example.spring_jms;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.example.spring.rabbitmq.service.PusherService;


@SpringBootApplication
    public class Application implements ApplicationRunner {

        // сервис по отправке сообщений
        private final PusherService pusherService;

        // инжект бина через конструктор
        public Application(PusherService pusherService) {
            this.pusherService = pusherService;
        }

        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }

        @Override
        public void run(ApplicationArguments applicationArguments) {
            pusherService.start(); // запускаем отправку сообщений
        }
}
