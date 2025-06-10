package com.ugb.miprimeraaplicacion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Graficos extends Activity {

    private View barLuz, barAgua, barTelefono, barPersonales, barEscolares, barHigiene;
    private TextView totalGeneral, gastosText, ingresosText;

    Button btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_graficos);

        inicializarTabs();
        inicializarVistasGrafico();
        configurarFiltros();
        actualizarDatos("mes");

        btn = findViewById(R.id.btnAgregarGastoDesdeGrafico);
        btn.setOnClickListener(view -> AbrirClaseAgregarGasto());

        btn = findViewById(R.id.btnIngresoAgregarDesdeGrafico);
        btn.setOnClickListener(view -> AbrirClaseAgregarIngresoDesdeGrafico());

    }

    private void inicializarTabs() {
        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("General");
        tab1.setContent(R.id.General);
        tab1.setIndicator("General");

        TabHost.TabSpec tab2 = tabHost.newTabSpec("Gastos");
        tab2.setContent(R.id.Gastos);
        tab2.setIndicator("Gastos");

        TabHost.TabSpec tab3 = tabHost.newTabSpec("Ingresos");
        tab3.setContent(R.id.Ingresos);
        tab3.setIndicator("Ingresos");

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#00796B"));
            TextView tv = tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            if (tv != null) {
                tv.setTextColor(Color.WHITE);
            }
        }

        tabHost.setOnTabChangedListener(tabId -> {
            for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                View tabView = tabHost.getTabWidget().getChildAt(i);
                tabView.setBackgroundColor(
                        tabHost.getCurrentTab() == i ?
                                Color.parseColor("#00695C") :
                                Color.parseColor("#00796B"));
            }
        });
    }
    private Void AbrirClaseAgregarGasto() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return null;
    }

    private Void AbrirClaseAgregarIngresoDesdeGrafico() {
        Intent intent = new Intent(this, agregar_ingresos.class);
        startActivity(intent);
        return null;
    }

    private void inicializarVistasGrafico() {
        barLuz = findViewById(R.id.barLuz);
        barAgua = findViewById(R.id.barAgua);
        barTelefono = findViewById(R.id.barTelefono);
        barPersonales = findViewById(R.id.barPersonales);
        barEscolares = findViewById(R.id.barEscolares);
        barHigiene = findViewById(R.id.barHigiene);

        totalGeneral = findViewById(R.id.totalGeneral);
        gastosText = findViewById(R.id.gastosText);
        ingresosText = findViewById(R.id.ingresosText);
    }

    private void configurarFiltros() {
        Button btnAnio = findViewById(R.id.btnAnio);
        Button btnMes = findViewById(R.id.btnMes);
        Button btnSemana = findViewById(R.id.btnSemana);
        Button btnDia = findViewById(R.id.btnDia);

        View.OnClickListener filtroListener = v -> {
            btnAnio.setSelected(false);
            btnMes.setSelected(false);
            btnSemana.setSelected(false);
            btnDia.setSelected(false);

            v.setSelected(true);

            String periodo = "mes";
            if (v == btnAnio) periodo = "año";
            else if (v == btnMes) periodo = "mes";
            else if (v == btnSemana) periodo = "semana";
            else if (v == btnDia) periodo = "día";

            actualizarDatos(periodo);
        };

        btnAnio.setOnClickListener(filtroListener);
        btnMes.setOnClickListener(filtroListener);
        btnSemana.setOnClickListener(filtroListener);
        btnDia.setOnClickListener(filtroListener);

        btnMes.setSelected(true);
    }

    private void actualizarDatos(String periodo) {
        int alturaLuz = 120, alturaAgua = 100, alturaTelefono = 90, alturaPersonales = 140, alturaEscolares = 70, alturaHigiene = 80;

        switch (periodo) {
            case "año":
                alturaLuz *= 12;
                alturaAgua *= 12;
                alturaTelefono *= 12;
                alturaPersonales *= 12;
                alturaEscolares *= 12;
                alturaHigiene *= 12;
                break;
            case "semana":
                alturaLuz /= 4;
                alturaAgua /= 4;
                alturaTelefono /= 4;
                alturaPersonales /= 4;
                alturaEscolares /= 4;
                alturaHigiene /= 4;
                break;
            case "día":
                alturaLuz /= 30;
                alturaAgua /= 30;
                alturaTelefono /= 30;
                alturaPersonales /= 30;
                alturaEscolares /= 30;
                alturaHigiene /= 30;
                break;
            case "mes":
            default:

                break;
        }

        actualizarAlturaBarra(barLuz, alturaLuz);
        actualizarAlturaBarra(barAgua, alturaAgua);
        actualizarAlturaBarra(barTelefono, alturaTelefono);
        actualizarAlturaBarra(barPersonales, alturaPersonales);
        actualizarAlturaBarra(barEscolares, alturaEscolares);
        actualizarAlturaBarra(barHigiene, alturaHigiene);

        double total = alturaLuz + alturaAgua + alturaTelefono + alturaPersonales + alturaEscolares + alturaHigiene;
        totalGeneral.setText(String.format("$%,.2f", total * 10.0));
        gastosText.setText(String.format("$%,.2f", total * 7.5));
        ingresosText.setText(String.format("$%,.2f", total * 2.5));
    }

    private void actualizarAlturaBarra(View barra, int alturaDp) {
        barra.getLayoutParams().height = dpToPx(alturaDp);
        barra.requestLayout();
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}