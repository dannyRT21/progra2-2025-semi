package com.ugb.miprimeraaplicacion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GastosDB";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_USUARIOS = "CREATE TABLE usuarios (" +
            "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "usuario TEXT, clave TEXT, nombre TEXT, direccion TEXT, telefono TEXT)";

    private static final String SQL_CREATE_GASTOS = "CREATE TABLE Gastos (" +
            "IdGasto INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "IdUsuario INTEGER, Categoria TEXT, Fecha TEXT, Concepto TEXT, Total REAL, UrlFoto TEXT)";

    // Tabla de Ingresos sin UrlFoto
    private static final String SQL_CREATE_INGRESOS = "CREATE TABLE Ingresos (" +
            "IdIngreso INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "IdUsuario INTEGER, Categoria TEXT, Fecha TEXT, Concepto TEXT, Total REAL)";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USUARIOS);
        db.execSQL(SQL_CREATE_GASTOS);
        db.execSQL(SQL_CREATE_INGRESOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("ALTER TABLE Gastos ADD COLUMN UrlFoto TEXT");

        }
    }

    public String administrarUsuarios(String accion, String[] datos) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mensaje = "ok", sql = "";
            switch (accion) {
                case "agregar":
                    sql = "INSERT INTO usuarios (usuario, clave, nombre, direccion, telefono ) " +
                            "VALUES ('" + datos[1] + "', '" + datos[2] + "', '" + datos[3] + "', '" + datos[4] + "', '" + datos[5] + "')";
                    break;
                case "modificar":
                    sql = "UPDATE usuarios SET " +
                            "usuario = '" + datos[1] + "', clave = '" + datos[2] + "', nombre = '" + datos[3] + "', " +
                            "direccion = '" + datos[4] + "', telefono = '" + datos[5] + "' WHERE idUsuario = " + datos[0];
                    break;
                case "eliminar":
                    sql = "DELETE FROM usuarios WHERE idUsuario = " + datos[0];
                    break;
            }
            db.execSQL(sql);
            db.close();
            return mensaje;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String administrar_gastos(String accion, String[] datos) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mensaje = "ok", sql = "";
            switch (accion) {
                case "nuevo":
                    sql = "INSERT INTO Gastos (IdUsuario, Categoria, Fecha, Concepto, Total, UrlFoto) " +
                            "VALUES (" + datos[1] + ", '" + datos[2] + "', '" + datos[3] + "', '" + datos[4] + "', " + datos[5] + ", '" + datos[6] + "')";
                    break;
                case "modificar":
                    sql = "UPDATE Gastos SET " +
                            "IdUsuario = " + datos[1] + ", Categoria = '" + datos[2] + "', Fecha = '" + datos[3] + "', " +
                            "Concepto = '" + datos[4] + "', Total = " + datos[5] + ", UrlFoto = '" + datos[6] + "' WHERE IdGasto = " + datos[0];
                    break;
                case "eliminar":
                    sql = "DELETE FROM Gastos WHERE IdGasto = " + datos[0];
                    break;
            }
            db.execSQL(sql);
            db.close();
            return mensaje;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Cursor lista_gastos() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM Gastos", null);
    }

    // CRUD para la tabla Ingresos, SIN URLFOTO
    public String administrar_ingresos(String accion, String[] datos) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mensaje = "ok", sql = "";
            switch (accion) {
                case "nuevo":
                    sql = "INSERT INTO Ingresos (IdUsuario, Categoria, Fecha, Concepto, Total) " +
                            "VALUES (" + datos[1] + ", '" + datos[2] + "', '" + datos[3] + "', '" + datos[4] + "', " + datos[5] + ")";
                    break;
                case "modificar":
                    sql = "UPDATE Ingresos SET " +
                            "IdUsuario = " + datos[1] + ", Categoria = '" + datos[2] + "', Fecha = '" + datos[3] + "', " +
                            "Concepto = '" + datos[4] + "', Total = " + datos[5] + " WHERE IdIngreso = " + datos[0];
                    break;
                case "eliminar":
                    sql = "DELETE FROM Ingresos WHERE IdIngreso = " + datos[0];
                    break;
            }
            db.execSQL(sql);
            db.close();
            return mensaje;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Cursor lista_ingresos() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM Ingresos", null);
    }
}