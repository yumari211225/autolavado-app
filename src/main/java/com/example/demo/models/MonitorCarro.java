package com.example.demo.models;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
public class MonitorCarro {
    private final MonitorRecepcion autoLavado;

    public MonitorCarro(MonitorRecepcion autoLavado) {
        this.autoLavado = autoLavado;
    }

    public void atender() {
        // Simular tiempo de atención
        try {
            Thread.sleep(10000);  // Simular tiempo de lavado por 10 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
