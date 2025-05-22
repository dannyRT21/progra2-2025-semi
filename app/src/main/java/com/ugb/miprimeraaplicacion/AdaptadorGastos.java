package com.ugb.miprimeraaplicacion;

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

import java.io.File;
import java.util.ArrayList;

public class AdaptadorGastos extends BaseAdapter {

    private Context context;
    private ArrayList<Gastos> alGastos;
    private LayoutInflater inflater;

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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fotos, parent, false);
        }

        Gastos gasto = alGastos.get(position);

        TextView lblCategoria = convertView.findViewById(R.id.lblCategoriaAdaptador);
        TextView lblFecha = convertView.findViewById(R.id.lblFechaAdaptador);
        TextView lblConcepto = convertView.findViewById(R.id.lblConceptoAdaptador);
        TextView lblTotal = convertView.findViewById(R.id.lblTotalAdaptador);
        ImageView imgFoto = convertView.findViewById(R.id.imgFotoAdaptador);

        lblCategoria.setText(gasto.getCategoria());
        lblFecha.setText(gasto.getFecha());
        lblConcepto.setText(gasto.getConcepto());
        lblTotal.setText(gasto.getTotal());

        String urlFoto = gasto.getUrlFoto();
        if (urlFoto != null && !urlFoto.isEmpty()) {
            File file = new File(urlFoto);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(urlFoto);
                imgFoto.setImageBitmap(bitmap);
            } else {
                imgFoto.setImageResource(R.mipmap.ic_launcher_round);
                Toast.makeText(context, "No se pudo cargar la imagen, archivo no encontrado.", Toast.LENGTH_SHORT).show();
            }
        } else {
            imgFoto.setImageResource(R.mipmap.ic_launcher_round);
            Toast.makeText(context, "Ruta de imagen no válida.", Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }
}