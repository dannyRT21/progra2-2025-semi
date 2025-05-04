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

public class AdaptadorGastos extends BaseAdapter {

    Context context;

    ArrayList<Gastos> alGastos;

    Gastos misGastos;

    LayoutInflater inflater;

    public AdaptadorGastos(Context context, ArrayList<Gastos> alGastos) {
        this.context = context;
        this.alGastos = alGastos;
    }

    @Override
    public int getCount() {
        return alGastos.size();
    }

    @Override
    public Object getItem(int position) {
       return alGastos.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.fotos, parent, false);
        try {
            misGastos = alGastos.get(position);

        TextView tempVal = itemView.findViewById(R.id.lblCategoriaAdaptador);
        tempVal.setText(misGastos.getCategoria());

        tempVal = itemView.findViewById(R.id.lblFechaAdaptador);
        tempVal.setText(misGastos.getFecha());

        tempVal = itemView.findViewById(R.id.lblConceptoAdaptador);
        tempVal.setText(misGastos.getConcepto());

        tempVal = itemView.findViewById(R.id.lblTotalAdaptador);
        tempVal.setText(misGastos.getTotal());

            ImageView img = itemView.findViewById(R.id.imgFotoAdaptador);
            Bitmap bitmap = BitmapFactory.decodeFile(misGastos.getUrlFoto());
            img.setImageBitmap(bitmap);



        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return itemView;
    }
}


