package com.a3jfernando.serialport.pages;

import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.Services.UsbService;
import com.a3jfernando.serialport.controllers.VariableController;
import com.a3jfernando.serialport.models.Tipo_Variable;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartLineRealTime extends AppCompatActivity implements
        OnChartValueSelectedListener{


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.activity_chart_line_real_time)
    LinearLayout activity_chart_line_real_time;
    ///Services
    VariableController variableControllerInstance;


    private LineChart mChart;

    private UsbService usbService;

    private EditText DatoTramaChart;
    private EditText eTPositionVariable;
    private EditText DataHumedad;
    private TextView txtNombreTipoVariable;
    private TextClock txtRealTimeClock;

    private Integer count= 0;
    String humedadglobal;



    ///Variables Globales Tipo variables
    private String Nombre_Tipo_Variable,Descripcion_Variable,Unidad_Medida;
    private long Id_Tipo_Variable;
    private boolean Estado_Variable;
    long Posicion_Variable;
    /*----------------------------------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chart_line_real_time);
        ButterKnife.bind(this);
        setToolbar();
        loadInstances();


        try {
            Posicion_Variable= getIntent().getExtras().getLong("Posicion_Variable");
            Nombre_Tipo_Variable= getIntent().getExtras().getString("Nombre_Tipo_Variable");
            Descripcion_Variable= getIntent().getExtras().getString("Descripcion_Variable");
            Id_Tipo_Variable= getIntent().getExtras().getLong("Id_Tipo_Variable");

            loadPropertiesChart();
        }catch (Exception e){

        }
    }

    private void loadPropertiesChart() {
        mChart=new LineChart(this);
        eTPositionVariable= new EditText(this);
        DatoTramaChart= new EditText(this);
        DataHumedad= new EditText(this);
        txtNombreTipoVariable= new TextView(this);
        txtRealTimeClock= new TextClock(this);
        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mChart.setLayoutParams(lpView);

        ///Agregar a la vista
        activity_chart_line_real_time.addView(txtRealTimeClock);
        activity_chart_line_real_time.addView(txtNombreTipoVariable);
        activity_chart_line_real_time.addView(DatoTramaChart);
        activity_chart_line_real_time.addView(DataHumedad);
        activity_chart_line_real_time.addView(eTPositionVariable);
        activity_chart_line_real_time.addView(mChart,lpView);

        mChart.setOnChartValueSelectedListener(this);

        ///Ocultar EdiText
        DatoTramaChart.setVisibility(View.GONE);
        DataHumedad.setVisibility(View.GONE);
        eTPositionVariable.setVisibility(View.GONE);
        //Text Clock
        txtRealTimeClock.setFormat12Hour("hh:mm:ss a");
        txtRealTimeClock.setTextColor(Color.parseColor("#c11003"));
        txtRealTimeClock.setGravity(Gravity.RIGHT);
        txtRealTimeClock.setTextSize(30);
        txtRealTimeClock.setTypeface(null, Typeface.BOLD);

        txtNombreTipoVariable.setTextSize(20);
        txtNombreTipoVariable.setText("MIDIENDO: "+Nombre_Tipo_Variable);
        txtNombreTipoVariable.setTextColor(Color.parseColor("#c11003"));
        txtNombreTipoVariable.setTypeface(null, Typeface.NORMAL);
        ///customize line chart
        mChart.setDescription("");
        mChart.setNoDataTextDescription("No data for the moment");

        ///Enable value
        mChart.setHighlightPerTapEnabled(true);


        ///enable touch gestures
        mChart.setTouchEnabled(true);

        ///we want also enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        ///enable pinch zo avoid scaling x and y axis separately
        mChart.setPinchZoom(true);

        //altrenative backgrond color
        mChart.setBackgroundColor(Color.LTGRAY);


        //now, we work on data
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        //add data to line chart
        mChart.setData(data);

        ///get legend object
        Legend l= mChart.getLegend();



        LimitLine upper_limit = new LimitLine(230f, "230 Limite Superior");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(10f);

        LimitLine lower_limit = new LimitLine(0f, "0 Limite Inferior");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(10f, 10f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(10f);

        ///Customi legend
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);

        YAxis yl = mChart.getAxisLeft();
        yl.setTextColor(Color.WHITE);
        yl.setAxisMaxValue(320f);
        yl.setDrawGridLines(true);
        yl.setDrawLimitLinesBehindData(true);
        yl.addLimitLine(upper_limit);
        yl.addLimitLine(lower_limit);
        yl.setAxisMinValue(-50f);
        //leftAxis.setYOffset(20f);
        yl.enableGridDashedLine(10f, 10f, 0f);
        yl.setDrawZeroLine(false);
        // limit lines are drawn behind data (and not on top)
        yl.setDrawLimitLinesBehindData(true);


        YAxis yl2 = mChart.getAxisRight();
        yl2.setEnabled(false);
        mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
        ///mChart = (LineChart) findViewById(R.id.chart1);

        ////customize line char

    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)// Habilitar Up Button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadInstances() {
        this.variableControllerInstance=  VariableController.getInstance(this);
    }

    /*----------------------------------------------------------------------------------------------------------------------*/



    ///Escucha Los valores enviados por Serial Port desde Menu Activity
    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Tipo_Variable tipo_variable = intent.getExtras().getParcelable("tipo_variable");
            String data= intent.getStringExtra("data_medida");
            int posicionLlegada= tipo_variable.getPosicion_Variable();
            if(posicionLlegada==Posicion_Variable){
                loadDataInChart(data);
                //saveDataVariable(data);
            }
        }
    };
    /*----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();
        this.registerReceiver(mNotificationReceiver, new IntentFilter("DATA_SERIAL"));

    }
    /*----------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(mNotificationReceiver);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //this.unregisterReceiver(mNotificationReceiver);
    }

    private void loadDataInChart(final String data) {
       //// progressStatus=progres;
        // Set the progress status zero on each button click
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Double humedadgrafic=0.0;
                if(isParceableDouble(data)){
                    humedadgrafic = Double.parseDouble(data);
                }else{
                    humedadgrafic=0.0;
                }
                addEntry(humedadgrafic);///Chart is notified of update in addEntry method
            }
        }).start();
    }

    //Verifica se puede parcear el valor
    private static boolean isParceableDouble(String cadena){
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }





    //Agregar data al grafico
    /*----------------------------------------------------------------------------------------------------------------------*/
    private void addEntry(Double humedadgrafic){
        try {
            LineData data = mChart.getData();
            if(data!=null){
                ILineDataSet set =data.getDataSetByIndex(0);
                if (set == null) {
                    set = createSet();
                    data.addDataSet(set);
                }

                // create a data object with the datasets
                //  LineData data = new LineData(xVals, dataSets);
               /// LineData data = new LineData(xVals, dataSets);
                // set data
                mChart.setData(data);
                //add a new random value
               // data.addXValue("");

                data.addXValue(getTimePhone());
                data.addEntry(new Entry( Float.parseFloat(String.valueOf(humedadgrafic)) ,set.getEntryCount()), 0);

                ////data.addEntry(new Entry( Float.parseFloat(String.valueOf(humedadgrafic)+12) ,set.getEntryCount()), 0);
                ///notify chart data have changed
                ///data.notifyDataChanged();
                // let the chart know it's data has changed
                mChart.notifyDataSetChanged();

                //limit number of visible entries
                // limit the number of visible entries
                /// mChart.setVisibleXRangeMaximum(6);

                mChart.setVisibleXRangeMaximum(6);
                // mChart.setVisibleYRange(30, AxisDependency.LEFT);
                // move to the latest entry
                mChart.moveViewToX(data.getXValCount()-7);

                ///scroll tthe last entry
                /// mChart.moveViewToX(data.getXValCount()-7);
            }
        }catch (Exception e){
        }
    }
    
    ///
    //Get Date Phone
    /*----------------------------------------------------------------------------------------------------------------------*/
    private String getTimePhone()
    {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formatteDate = df.format(date);
        return formatteDate;
    }


    ///method to create set
     /*----------------------------------------------------------------------------------------------------------------------*/
    private LineDataSet createSet(){

        LineDataSet set = new LineDataSet(null, "SPL Db");
        set.setDrawCubic(true);
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        //set.setCircleSize(4f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        // set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(10f);
        /// set.setDrawValues(true);
        set.setDrawCircleHole(false);
        //// set.setValueTextSize(9f);
        set.setDrawFilled(true);
        return set;
    }



    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        ///Log.i("Entry selected", e.toString());
        Toast.makeText(ChartLineRealTime.this, "Data: "+e.getVal(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }


    ///Metodo que permite no recargar la pagina al devolverse
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
            case android.R.id.home:
                // Obtener intent de la actividad padre
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // Comprobar si DetailActivity no se creó desde CourseActivity
                if (NavUtils.shouldUpRecreateTask(this, upIntent)
                        || this.isTaskRoot()) {

                    // Construir de nuevo la tarea para ligar ambas actividades
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Terminar con el método correspondiente para Android 5.x
                    this.finishAfterTransition();
                    return true;
                }

                //Para versiones anterios a 5.x
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    // Terminar con el método correspondiente para Android 5.x
                    onBackPressed();
                    return true;
                }

                // Dejar que el sistema maneje el comportamiento del up button
                return false;
        }
    }
}
