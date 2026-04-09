package model;

import java.util.ArrayList;

public class Inventario {

    private ArrayList<Producto> listaProductos;

    public Inventario() {
        this.listaProductos = new ArrayList<>();
        cargarProductosPrueba();
    }

    private void cargarProductosPrueba() {
        listaProductos.add(new Producto("leche", 12.35, 15.5, 16, 5));
        listaProductos.add(new Producto("pan", 5.5, 7.95, 18, 6));
        listaProductos.add(new Producto("agua", 13.39, 18.55, 12, 4));
        listaProductos.add(new Producto("huevos", 22.4, 30.39, 20, 7));
        listaProductos.add(new Producto("refresco", 10.99, 14.75, 30, 8));
        listaProductos.add(new Producto("aceite", 13.99, 25.75, 25, 7));
        listaProductos.add(new Producto("arroz", 20.6, 31.55, 30, 8));
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public ArrayList<Producto> getListaProductosActivos() {
        ArrayList<Producto> productosActivos = new ArrayList<>();
        ArrayList<Producto> listaProductos = getListaProductos();
        for (Producto p : listaProductos) {
            if (p.isActive()) productosActivos.add(p);
        }
        return productosActivos;
    }
}