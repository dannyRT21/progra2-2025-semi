package com.ugb.miprimeraaplicacion;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

class Graficos  extends Activity {

    // Variables para las barras del gráfico
    private View barLuz, barAgua, barTelefono, barPersonales, barEscolares, barHigiene;
    private TextView totalGeneral, gastosText, ingresosText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Asegúrate que coincida con tu XML

        // Inicializar TabHost
        inicializarTabs();

        // Inicializar vistas del gráfico
        inicializarVistasGrafico();

        // Configurar botones de filtro
        configurarFiltros();

        // Configurar datos de ejemplo
        configurarDatosEjemplo();
    }

    private void inicializarTabs() {
        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        // Tab 1 - General
        TabHost.TabSpec tab1 = tabHost.newTabSpec("General");
        tab1.setContent(R.id.General);
        // Cambiado a texto simple ya que los drawables pueden causar error si no existen
        tab1.setIndicator("General");

        // Tab 2 - Gastos
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Gastos");
        tab2.setContent(R.id.Gastos);
        tab2.setIndicator("Gastos");

        // Tab 3 - Ingresos
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Ingresos");
        tab3.setContent(R.id.Ingresos);
        tab3.setIndicator("Ingresos");

        // Añadir pestañas al TabHost
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);

        // Personalizar el color de las pestañas
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

    private void inicializarVistasGrafico() {
        // Barras del gráfico
        barLuz = findViewById(R.id.barLuz);
        barAgua = findViewById(R.id.barAgua);
        barTelefono = findViewById(R.id.barTelefono);
        barPersonales = findViewById(R.id.barPersonales);
        barEscolares = findViewById(R.id.barEscolares);
        barHigiene = findViewById(R.id.barHigiene);

        // Textos de totales
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
            // Resetear todos los botones
            btnAnio.setSelected(false);
            btnMes.setSelected(false);
            btnSemana.setSelected(false);
            btnDia.setSelected(false);

            // Marcar el botón seleccionado
            v.setSelected(true);

            // Filtrar datos
            String periodo = "";
            if (v == btnAnio) periodo = "año";
            else if (v == btnMes) periodo = "mes";
            else if (v == btnSemana) periodo = "semana";
            else if (v == btnDia) periodo = "día";


        };

        btnAnio.setOnClickListener(filtroListener);
        btnMes.setOnClickListener(filtroListener);
        btnSemana.setOnClickListener(filtroListener);
        btnDia.setOnClickListener(filtroListener);

        // Seleccionar "Por mes" por defecto
        btnMes.setSelected(true);
    }

    private void configurarDatosEjemplo() {
        actualizarDatos("mes");
    }

    private void actualizarDatos(String periodo) {
        // Valores de ejemplo basados en el período
        int alturaLuz = 120;
        int alturaAgua = 100;
        int alturaTelefono = 90;
        int alturaPersonales = 140;
        int alturaEscolares = 70;
        int alturaHigiene = 80;

        // Ajustar valores según el período seleccionado
        switch (periodo) {
            case "año":
                alturaLuz *= 12;
                alturaAgua *= 12;
                // ... otros ajustes
                break;
            case "mes":
                // Valores por defecto (mensuales)
                break;
            case "semana":
                alturaLuz /= 4;
                alturaAgua /= 4;
                // ... otros ajustes
                break;
            case "día":
                alturaLuz /= 30;
                alturaAgua /= 30;
                // ... otros ajustes
                break;
        }

        // Aplicar cambios a las vistas
        actualizarAlturaBarra(barLuz, alturaLuz);
        actualizarAlturaBarra(barAgua, alturaAgua);
        actualizarAlturaBarra(barTelefono, alturaTelefono);
        actualizarAlturaBarra(barPersonales, alturaPersonales);
        actualizarAlturaBarra(barEscolares, alturaEscolares);
        actualizarAlturaBarra(barHigiene, alturaHigiene);

        // Actualizar totales (ejemplo simplificado)
        double total = alturaLuz + alturaAgua + alturaTelefono +
                alturaPersonales + alturaEscolares + alturaHigiene;
        totalGeneral.setText(String.format("$%,.2f", total * 10.0)); // Factor de conversión ejemplo
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