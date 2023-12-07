package com.example.demo.models;

import java.util.concurrent.*;

public class MonitorRecepcion {

    private final BlockingQueue<MonitorCarro> colaEspera ;
    private final Semaphore semaforoLavado = null;
    private final ExecutorService ejecutorOperadores;
    private final ExecutorService ejecutorClientes;
    private final int capacidad;

    private final Runnable operadorTarea = () -> {
        try {
            while (true) {
                semaforoLavado.acquire();  // Esperar hasta que haya un espacio en el lavado
                MonitorCarro cliente = null;  // Tomar un cliente de la cola
                if (cliente != null) {
                    cliente.atender();  // Atender al cliente
                    semaforoLavado.release();  // Liberar el espacio del lavado
                } else {
                    semaforoLavado.release();  // Liberar el espacio del lavado si no hay cliente en espera
                    descansar();  // Descansar si no hay cliente
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    };


    public MonitorRecepcion(int capacidad, Runnable onClienteLlego, Runnable onClienteAtendido) {
        this.capacidad = capacidad;
        this.colaEspera = new LinkedBlockingQueue<>();
        //this.semaforoLavado = new Semaphore(capacidad, true);
        this.ejecutorOperadores = Executors.newFixedThreadPool((int) (capacidad * 0.3));
        this.ejecutorClientes = Executors.newSingleThreadExecutor();

        // Configurar operadores
        for (int i = 0; i < (int) (capacidad * 0.3); i++) {
            ejecutorOperadores.submit(operadorTarea);
        }

        // Configurar cliente
        ejecutorClientes.execute(() -> {
            try {
                while (true) {
                    MonitorCarro cliente = colaEspera.take();  // Tomar un cliente de la cola
                    onClienteLlego.run();  // Notificar a la interfaz gráfica
                    semaforoLavado.acquire();  // Esperar hasta que haya un espacio en el lavado
                    ejecutorOperadores.submit(operadorTarea);  // Asignar un operador para atender al cliente
                    semaforoLavado.release();  // Liberar el espacio del lavado
                    onClienteAtendido.run();  // Notificar a la interfaz gráfica
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void atenderCliente(MonitorCarro cliente) {
        colaEspera.offer(cliente);  // Agregar cliente a la cola
    }

    private void descansar() {
        // Simular descanso
        try {
            Thread.sleep(5000);  // Descansar por 5 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
