package com.ugb.miprimeraaplicacion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase DB que extiende SQLiteOpenHelper para manejar la base de datos de amigos.
 */
public class DB extends SQLiteOpenHelper {
    // Nombre de la base de datos
    private static final String DATABASE_NAME = "amigos";
    // Versión de la base de datos
    private static final int DATABASE_VERSION = 1;
    // Sentencia SQL para crear la tabla amigos
    private static final String SQLdb = "CREATE TABLE amigos (idAmigo INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, direccion TEXT, telefono TEXT, email TEXT, dui TEXT, urlFoto TEXT)";

    /**
     * Constructor de la clase DB.
     * @param context Contexto de la aplicación.
     */
    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Método llamado cuando la base de datos es creada por primera vez.
     * @param db Instancia de la base de datos.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ejecuta la sentencia SQL para crear la tabla amigos
        db.execSQL(SQLdb);
    }

    /**
     * Método llamado cuando la base de datos necesita ser actualizada.
     * @param db Instancia de la base de datos.
     * @param oldVersion Versión anterior de la base de datos.
     * @param newVersion Nueva versión de la base de datos.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Actualizar la estructura de la base de datos si es necesario
    }

    /**
     * Método para administrar amigos en la base de datos.
     * @param accion Acción a realizar (agregar, modificar, eliminar).
     * @param datos Datos del amigo.
     * @return Mensaje de resultado de la operación.
     */
    public String administrar_amigos(String accion, String[] datos) {
        try {
            // Obtiene una instancia de la base de datos en modo escritura
            SQLiteDatabase db = getWritableDatabase();
            String mensaje = "ok", sql = "";
            // Determina la acción a realizar y construye la sentencia SQL correspondiente
            switch (accion) {
                case "agregar":
                    sql = "INSERT INTO amigos (nombre, direccion, telefono, email, dui, urlFoto) VALUES ('" + datos[1] + "', '" + datos[2] + "', '" + datos[3] + "', '" + datos[4] + "', '" + datos[5] + "', '" + datos[6] + "')";
                    break;
                case "modificar":
                    sql = "UPDATE amigos SET nombre = '" + datos[1] + "', direccion = '" + datos[2] + "', telefono = '" + datos[3] + "', email = '" + datos[4] + "', dui = '" + datos[5] + "', urlFoto = '" + datos[6] + "' WHERE idAmigo = " + datos[0];
                    break;
                case "eliminar":
                    sql = "DELETE FROM amigos WHERE idAmigo = " + datos[0];
                    break;
            }
            // Ejecuta la sentencia SQL
            db.execSQL(sql);
            // Cierra la base de datos
            db.close();
            return mensaje;
        } catch (Exception e) {
            // Retorna el mensaje de error en caso de excepción
            return e.getMessage();
        }
    }
}