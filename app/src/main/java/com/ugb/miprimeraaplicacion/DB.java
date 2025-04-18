package com.ugb.miprimeraaplicacion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mandarino"; // Cambiamos el nombre de la base de datos
    private static final int DATABASE_VERSION = 1;
    private static final String SQLdb = "CREATE TABLE productos (idProducto TEXT, codigo TEXT, nombre TEXT, presentacion TEXT, marca TEXT, precio REAL, urlFoto TEXT)"; // Cambiamos el nombre de la tabla y los campos
    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLdb);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Actualizar la estrucutra de la base de datos si es necesario
    }
    public String administrar_productos(String accion, String[] datos) { // Cambiamos el nombre del método
        try{
            SQLiteDatabase db = getWritableDatabase();
            String mensaje = "ok", sql = "";
            switch (accion) {
                case "agregar":
                    sql = "INSERT INTO productos (codigo, nombre, presentacion, marca, precio, urlFoto) VALUES ('"+ datos[1] +"', '" + datos[2] + "', '" + datos[3] + "', '" + datos[4] + "', " + datos[5] + ", '" + datos[6] + "')"; // Ajustamos los campos
                    break;
                case "modificar":
                    sql = "UPDATE productos SET codigo = '" + datos[1] + "', nombre = '" + datos[2] + "', presentacion = '" + datos[3] + "', marca = '" + datos[4] + "', precio = " + datos[5] + ", urlFoto = '" + datos[6] + "' WHERE idProducto = " + datos[0]; // Ajustamos los campos
                    break;
                case "eliminar":
                    sql = "DELETE FROM productos WHERE idProducto = " + datos[0];
                    break;
            }


            db.execSQL(sql);
            db.close();
            return mensaje;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public Cursor lista_productos() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM productos", null); // Cambiamos el nombre de la tabla
    }
}