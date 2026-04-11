package model;

public class DetalleVenta {
    private Producto producto;
    private int cantidad;
    private double subtotal;

    public DetalleVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = producto.getPrecioVenta() * cantidad;
    }

    public Producto getProducto() { return producto; }

    public String getNombreProducto() { return producto.getNombre(); }
    public double getPrecioUnitario() { return producto.getPrecioVenta(); }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = this.producto.getPrecioVenta() * cantidad; // Recalcula automático
    }

    public double getSubtotal() { return subtotal; }
}