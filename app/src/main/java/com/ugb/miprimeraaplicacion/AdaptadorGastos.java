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
    private final ArrayList<Gastos> alGastos;
    private LayoutInflater inflater;

    public AdaptadorGastos(Context context, ArrayList<Gastos> alGastos) {
        this.context = context;
        this.alGastos = alGastos;
        this.inflater = LayoutInflater.from(context);
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fotos, parent, false);
            holder = new ViewHolder();
            holder.lblCategoria = convertView.findViewById(R.id.lblCategoriaAdaptador);
            holder.lblFecha = convertView.findViewById(R.id.lblFechaAdaptador);
            holder.lblConcepto = convertView.findViewById(R.id.lblConceptoAdaptador);
            holder.lblTotal = convertView.findViewById(R.id.lblTotalAdaptador);
            holder.imgFoto = convertView.findViewById(R.id.imgFotoAdaptador);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Gastos gasto = alGastos.get(position);
        holder.lblCategoria.setText(gasto.getCategoria());
        holder.lblFecha.setText(gasto.getFecha());
        holder.lblConcepto.setText(gasto.getConcepto());
        holder.lblTotal.setText(gasto.getTotal());

        String urlFoto = gasto.getUrlFoto();
        if (urlFoto != null && !urlFoto.isEmpty()) {
            File file = new File(urlFoto);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(urlFoto);
                holder.imgFoto.setImageBitmap(bitmap);
            } else {
                holder.imgFoto.setImageResource(R.mipmap.ic_launcher_round);
            }
        } else {
            holder.imgFoto.setImageResource(R.mipmap.ic_launcher_round);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView lblCategoria, lblFecha, lblConcepto, lblTotal;
        ImageView imgFoto;
    }
}
