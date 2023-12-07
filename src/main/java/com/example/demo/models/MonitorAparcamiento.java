package com.example.demo.models;

public class MonitorAparcamiento {
    private int id;
    private boolean ocupado;

    public MonitorAparcamiento(int id) {
        this.id = id;
        this.ocupado = false;
    }

    public int getId() {
        return id;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

}
