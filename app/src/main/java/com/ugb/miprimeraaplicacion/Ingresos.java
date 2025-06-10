package com.ugb.miprimeraaplicacion;

public class Ingresos {
    private String idIngreso;
    private String idUsuario;
    private String fuenteIngreso;
    private String fechaIngreso;
    private String descripcionIngreso;
    private String montoIngreso;

    public Ingresos(String idIngreso, String idUsuario, String fuenteIngreso, String fechaIngreso, String descripcionIngreso, String montoIngreso) {
        this.idIngreso = idIngreso;
        this.idUsuario = idUsuario;
        this.fuenteIngreso = fuenteIngreso;
        this.fechaIngreso = fechaIngreso;
        this.descripcionIngreso = descripcionIngreso;
        this.montoIngreso = montoIngreso;
    }

    public String getIdIngreso() {
        return idIngreso;
    }

    public void setIdIngreso(String idIngreso) {
        this.idIngreso = idIngreso;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFuenteIngreso() {
        return fuenteIngreso;
    }

    public void setFuenteIngreso(String fuenteIngreso) {
        this.fuenteIngreso = fuenteIngreso;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getDescripcionIngreso() {
        return descripcionIngreso;
    }

    public void setDescripcionIngreso(String descripcionIngreso) {
        this.descripcionIngreso = descripcionIngreso;
    }

    public String getMontoIngreso() {
        return montoIngreso;
    }

    public void setMontoIngreso(String montoIngreso) {
        this.montoIngreso = montoIngreso;
    }


}
