package com.ugb.miprimeraaplicacion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdaptadorProductos extends BaseAdapter {



    Context context;
    ArrayList<productos> aProductos;

    productos misProductos;

    LayoutInflater inflater;

    public AdaptadorProductos(Context context, ArrayList<productos> aProducts) {
        this.context = context;
        this.aProductos = aProducts;
    }

    @Override
    public int getCount() {
        return aProductos.size();
    }

    @Override
    public Object getItem(int position) {
        return aProductos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vista = inflater.inflate(R.layout.fotos, parent, false);
        try {
          misProductos = aProductos.get(position);
            TextView tempVal = vista.findViewById(R.id.lblCodProducto);
            tempVal.setText(misProductos.getCodigo());

            tempVal = vista.findViewById(R.id.lblProducto);
            tempVal.setText(misProductos.getNombre());

            tempVal = vista.findViewById(R.id.lblpresentacion);
            tempVal.setText(misProductos.getPresentacion());

            tempVal = vista.findViewById(R.id.lblMarca);
            tempVal.setText(misProductos.getMarca());

            tempVal = vista.findViewById(R.id.lblPrecioUnidad);
            tempVal.setText(String.valueOf(misProductos.getPrecio()));

            tempVal = vista.findViewById(R.id.lblCosto);
            tempVal.setText(String.valueOf(misProductos.getCosto()));

            tempVal = vista.findViewById(R.id.lblStock);
            tempVal.setText(String.valueOf(misProductos.getStock()));




            ImageView img = vista.findViewById(R.id.imgFotoAdaptador);
            Bitmap imagenBitmap = BitmapFactory.decodeFile(misProductos.getUrlFoto());
            img.setImageBitmap(imagenBitmap);

        }catch (Exception e){
            Toast.makeText(context, "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return vista;
    }

}

