package model;

import java.time.LocalDateTime;

public class Venta {
    private String cajero;
    private double total;
    private LocalDateTime fechaHora;

    public Venta(String cajero, double total) {
        this.cajero = cajero;
        this.total = total;
        this.fechaHora = LocalDateTime.now();
    }

    public String getCajero() { return cajero; }
    public double getTotal() { return total; }
    public LocalDateTime getFechaHora() { return fechaHora; }
}