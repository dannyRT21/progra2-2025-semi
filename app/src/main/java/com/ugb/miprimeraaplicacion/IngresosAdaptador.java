package com.ugb.miprimeraaplicacion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class IngresosAdaptador extends BaseAdapter {
    private Context context;
    private final ArrayList<Ingresos> alIngresos;
    private LayoutInflater inflater;

    public IngresosAdaptador(Context context, ArrayList<Ingresos> alIngresos) {
        this.context = context;
        this.alIngresos = alIngresos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return alIngresos.size();
    }

    @Override
    public Object getItem(int position) {
        return alIngresos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ingresosfts, parent, false);
            holder = new ViewHolder();
            holder.lblFuente = convertView.findViewById(R.id.lblCategoriaIngresosAdaptador);
            holder.lblFecha = convertView.findViewById(R.id.lblFechaIngresoAdaptador);
            holder.lblDescripcion = convertView.findViewById(R.id.lblConceptoIngresoAdaptador);
            holder.lblMonto = convertView.findViewById(R.id.lblTotalIngresoAdaptador);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Ingresos ingreso = alIngresos.get(position);
        holder.lblFuente.setText(ingreso.getFuenteIngreso());
        holder.lblFecha.setText(ingreso.getFechaIngreso());
        holder.lblDescripcion.setText(ingreso.getDescripcionIngreso());
        holder.lblMonto.setText(ingreso.getMontoIngreso());

        return convertView;
    }

    static class ViewHolder {
        TextView lblFuente;
        TextView lblFecha;
        TextView lblDescripcion;
        TextView lblMonto;
    }
}
