package com.example.demo.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainViewController implements Initializable {

    @FXML
    private AnchorPane MainContainer;
    @FXML
    private ImageView Recepcion;
    @FXML
    private ImageView Spot1;
    @FXML
    private ImageView Spot2;
    @FXML
    private ImageView Spot3;
    @FXML
    private ImageView Spot4;
    @FXML
    private ImageView Spot5;
    @FXML
    private ImageView Spot6;
    @FXML
    private ImageView Spot7;
    @FXML
    private ImageView Spot8;
    @FXML
    private ImageView Spot9;
    @FXML
    private ImageView Spot10;

    private List<ImageView> spots;
    private double recepcionX;
    private double recepcionY;
    private AtomicBoolean AutoEnCamino = new AtomicBoolean(true);
    private Timer timer;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        double x = Recepcion.getLayoutX();
        double y = Recepcion.getLayoutY();
        recepcionX = x;
        recepcionY = y;
        generarAparcamientos();
        Autolavado();
    }

    public void generarAparcamientos(){
        spots = new ArrayList<>();
        spots.add(Spot1);
        spots.add(Spot2);
        spots.add(Spot3);
        spots.add(Spot4);
        spots.add(Spot5);
        spots.add(Spot6);
        spots.add(Spot7);
        spots.add(Spot8);
        spots.add(Spot9);
        spots.add(Spot10);
    }

    public void Autolavado(){
        generarCarros();
    }


    public void generarCarros(){
        Image carro = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/auto-sucio.png")));
        ImageView contenedor_carro = new ImageView();
        contenedor_carro.setImage(carro);
        MainContainer.getChildren().add(contenedor_carro);
        //contenedor_aux.setLayoutX(30.00);
        //contenedor_aux.setLayoutY(130.00);
        contenedor_carro.setFitHeight(75.00);
        contenedor_carro.setFitWidth(150.00);
        contenedor_carro.setLayoutX(30.00);
        contenedor_carro.setLayoutY(40.00);
        contenedor_carro.setPreserveRatio(true);
        iniciarSimulacion(contenedor_carro);
    }

    public ImageView generarOperador(ImageView spotDisponible){
        ImageView operador = new ImageView();
        operador.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/operador.png"))));
        operador.setFitWidth(70);
        operador.setFitHeight(70);
        double LX = spotDisponible.getLayoutX();
        double LY = spotDisponible.getLayoutY();
        MainContainer.getChildren().add(operador);
        operador.setLayoutY((LY + 45.00));
        operador.setLayoutX((LX + 40.00));
        return operador;
    }

    public void iniciarSimulacion(ImageView contenedor_carro) {
        AtomicBoolean AutoenRecepcion = new AtomicBoolean(false);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(5), contenedor_carro);
        translateTransition.setByX(415);
        translateTransition.setCycleCount(1);
        translateTransition.play();
        translateTransition.setOnFinished(event -> {
            if (contenedor_carro.getBoundsInParent().intersects(Recepcion.getBoundsInParent())) {
                AutoenRecepcion.set(true);
                translateTransition.stop();
                asignarSpotLavado(contenedor_carro, AutoenRecepcion);
            } else if (contenedor_carro.getLayoutX() >= recepcionX - 80) {
                AutoenRecepcion.set(true);
                translateTransition.stop();
            }
        });
    }

    private void asignarSpotLavado(ImageView contenedor_carro, AtomicBoolean autoEnRecepcion) {
        if (autoEnRecepcion.get()) {
            ImageView spotDisponible = spots.stream().filter(spot -> spot.getImage() != null && spot.getImage().getUrl().contains("/assets/lugar-libre.png")).findFirst().orElse(null);
            if (spotDisponible != null) {
                AutoEnCamino.set(false);
                spotDisponible.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/lugar-ocupado.png"))));
                System.out.println("Auto asignado al Spot de Lavado #" + (spots.indexOf(spotDisponible) + 1));
                contenedor_carro.setVisible(false);
                ImageView operador = generarOperador(spotDisponible);
                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished(event -> finalizarCicloLavado(contenedor_carro, spotDisponible, operador));
                pause.play();
            } else {
                System.out.println("Todos los spots de lavado están ocupados. Espere...");
            }
        }
    }

    private void finalizarCicloLavado(ImageView contenedor_carro, ImageView spotOcupado, ImageView operador) {
        // Cambiar la imagen del spot a "disponible"
        spotOcupado.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/lugar-libre.png"))));
        contenedor_carro.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/auto-limpio.png"))));
        MainContainer.getChildren().remove(operador);
        contenedor_carro.setLayoutY(155);
        contenedor_carro.setVisible(true);
        System.out.println("Ciclo de lavado finalizado. Auto Listo.");
        animarSalida(contenedor_carro);
    }

    private void animarSalida(ImageView contenedor_carro) {
        TranslateTransition salidaTransition = new TranslateTransition(Duration.seconds(3), contenedor_carro);
        salidaTransition.setByX(-420);  // Desplazamiento horizontal de -400 unidades (en sentido contrario)
        salidaTransition.setCycleCount(1);
        salidaTransition.setOnFinished(event -> {
            System.out.println("Auto ha salido del lavado. Fin de la simulación.");
            MainContainer.getChildren().remove(contenedor_carro);
        });
        salidaTransition.play();

    }

}