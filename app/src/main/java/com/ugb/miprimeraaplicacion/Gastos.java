package com.ugb.miprimeraaplicacion;

public class Gastos {

    private String idGasto;
    private String idUsuario;
    private String categoria;
    private String fecha;
    private String concepto;
    private String total;
    private String urlFoto;

    public Gastos(String idGasto, String idUsuario, String categoria, String fecha, String concepto, String total, String urlFoto) {
        this.idGasto = idGasto;
        this.idUsuario = idUsuario;
        this.categoria = categoria;
        this.fecha = fecha;
        this.concepto = concepto;
        this.total = total;
        this.urlFoto = urlFoto;
    }

    public String getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(String idGasto) {
        this.idGasto = idGasto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
}