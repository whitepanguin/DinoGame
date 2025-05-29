package com.dino;

import controller.Controller;
import domain.*;
import repository.Repository;
import service.Service;

public class DinoMain {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.start();
    }
}
