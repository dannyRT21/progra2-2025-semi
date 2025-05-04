package com.ugb.miprimeraaplicacion;

public class Gastos {

    String IdGasto;
    String IdUsuario;
    String Categoria;
    String Fecha;
    String Concepto;
    String Total;
    String UrlFoto;

    public String getIdGasto() {
        return IdGasto;
    }

    public void setIdGasto(String idGasto) {
        IdGasto = idGasto;
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getConcepto() {
        return Concepto;
    }

    public void setConcepto(String concepto) {
        Concepto = concepto;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getUrlFoto() {
        return UrlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        UrlFoto = urlFoto;
    }

    public Gastos(String idGasto, String idUsuario, String categoria, String fecha, String concepto, String total, String urlFoto) {
        IdGasto = idGasto;
        IdUsuario = idUsuario;
        Categoria = categoria;
        Fecha = fecha;
        Concepto = concepto;
        Total = total;
        UrlFoto = urlFoto;
    }
}


