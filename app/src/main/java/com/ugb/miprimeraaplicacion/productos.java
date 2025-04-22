package com.ugb.miprimeraaplicacion;

public class productos {
    String idProducto;
    String codigo;
    String nombre;
    String presentacion;
    String marca;
    String precio;
    String urlFoto;

     String costo;
    String stock;



    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getCosto() {
        return costo;
    }
    public void setCosto(String costo) {
        this.costo = costo;
    }
    public String getStock() {
        return stock;
    }
    public void setStock(String stock) {
        this.stock = stock;
    }

    public productos(String idProducto, String codigo, String nombre, String presentacion, String marca, String precio, String urlFoto, String costo, String stock) {
        this.idProducto = idProducto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.presentacion = presentacion;
        this.marca = marca;
        this.precio = precio;
        this.urlFoto = urlFoto;
        this.costo = costo;
        this.stock = stock;

    }
}



