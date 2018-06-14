package com.a3jfernando.serialport.pages;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.a3jfernando.serialport.Adapters.AdapterListDevices;
import com.a3jfernando.serialport.Adapters.AdapterListTipoVariables;
import com.a3jfernando.serialport.Adapters.AdapterReportVariable;
import com.a3jfernando.serialport.MainActivity;
import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.Services.ApiService.LoginService;
import com.a3jfernando.serialport.Services.Utils.Const;
import com.a3jfernando.serialport.controllers.AppController;
import com.a3jfernando.serialport.controllers.TipoVariableController;
import com.a3jfernando.serialport.controllers.VariableController;
import com.a3jfernando.serialport.layouts.MenuControlActivity;
import com.a3jfernando.serialport.models.Tipo_Variable;
import com.a3jfernando.serialport.models.Usuario;
import com.a3jfernando.serialport.models.Variable;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;




import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPTableEvent;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReportVariables extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ///Services
    ///Conexion a la base de datos
    TipoVariableController tipoVariableControllerInstance;
    VariableController variableControllerInstance;

    //UI Elements
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.listViewVariables)
    ListView listViewReporVariable;

    @BindView(R.id.txtResultados)
    TextView txtResultados;

    @BindView(R.id.txt_TextDateTimeInicial)
    EditText txt_TextDateTimeInicial;

    @BindView(R.id.btnImageSelectFechaInicial)
    ImageView btnImageSelectFechaInicial;

    @BindView(R.id.txt_TxtDateTimeFinal)
    EditText txt_TextDateTimeFinal;

    @BindView(R.id.btnImageSelectFechaFinal)
    ImageView btnImageSelectFechaFinal;

    @BindView(R.id.spinnerVariables)
    MaterialBetterSpinner spinnerVariables;

    private ProgressDialog pDialog;
    ///Calendar
    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar dateTime = Calendar.getInstance();

    //Array list
    public ArrayList<Variable> getsetvariable;

    ////Select de sectores
    private ArrayList<Tipo_Variable> getsettipovariable;
    ///public ArrayAdapter<String> adapters;
    public ArrayAdapter<String> adaptersTipoVariable;
    public ArrayList<String> arrayListTipoVariable= new ArrayList<>();

    //Verification Date Selected
    boolean fecha_inicial, fecha_final;
    long tipo_variable_id;
    String nombre_tipo_variable;

    //PDF
    private final static String NOMBRE_DIRECTORIO = "/INMUNIZADOR";
    private final static String NOMBRE_DOCUMENTO = "Reporte.pdf";
    private final static String ETIQUETA_ERROR = "ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_variables);
        ButterKnife.bind(this);
        loadInstances();
        setToolbar();
        dialogProgress();
        //Controls
        listTipoVariables();
        loadAdapterSpinner();
       ///// listReportVariables();
        loadEventsOnClickListener();
        fecha_inicial=true;
        fecha_final=true;
        updateDate();
    }

    private void loadEventsOnClickListener() {
        btnImageSelectFechaInicial.setOnClickListener(this);
        btnImageSelectFechaFinal.setOnClickListener(this);
        spinnerVariables.setOnItemClickListener(this);
    }

    private void loadAdapterSpinner() {
        adaptersTipoVariable = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,  arrayListTipoVariable);
        spinnerVariables.setAdapter(adaptersTipoVariable);
    }

    private void dialogProgress() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Generando PDF...");
        pDialog.setCancelable(false);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)// Habilitar Up Button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadInstances() {
        this.variableControllerInstance= VariableController.getInstance(this);
        this.tipoVariableControllerInstance= TipoVariableController.getInstance(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tipo_variable_id= getsettipovariable.get(position).getId_Tipo_Variable();
        nombre_tipo_variable=getsettipovariable.get(position).getNombre_Tipo_Variable();
        listReportVariables();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnImageSelectFechaInicial:
                updateDate();
                fecha_inicial=true;
                fecha_final=false;
                break;
            case R.id.btnImageSelectFechaFinal:
                updateDate();
                fecha_inicial=false;
                fecha_final=true;
                break;
        }
    }

    ///Update Date
    private void updateDate(){
        new DatePickerDialog(this,d, dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateTextLabel();
        }
    };

    private void updateTextLabel(){
        //String fecha1= formatDateTime.format(dateTime.getTime());
        //Verificar seleccion de fechas
        if(fecha_inicial==true && fecha_final==true){
            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
            String formatted = format1.format(dateTime.getTime());
            txt_TextDateTimeInicial.setText(formatted);
            txt_TextDateTimeFinal.setText(formatted);
        }else if(fecha_inicial==true && fecha_final==false){
            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
            String formatted = format1.format(dateTime.getTime());
            txt_TextDateTimeInicial.setText(formatted);
        }else{
            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
            String formatted = format1.format(dateTime.getTime());
            txt_TextDateTimeFinal.setText(formatted);
        }
        listReportVariables();
    }


    //Cargar Reporte
    ///*------------------------------------------------------------------------------------------------------------*/
    private void listReportVariables() {
        if(tipo_variable_id==0) {
            txtResultados.setText("0 Resultados");
        }else{
            variableControllerInstance.abrirBaseDeDatos();
            String fechainicialselected= txt_TextDateTimeInicial.getText().toString();
            String fechafinalselected= txt_TextDateTimeFinal.getText().toString();
            getsetvariable = new ArrayList<Variable>();
            ///Array list para Lista de vehiculos
            ArrayList<Variable> listado =  variableControllerInstance.findVariablesByDateAndTipe(fechainicialselected,fechafinalselected,tipo_variable_id);
            txtResultados.setText(String.valueOf(listado.size())+" Resultados");

            ////For del listado para Sincronizacion
            for (Variable resultlist : listado) {
                Variable getsetvar= new Variable();
                getsetvar.setId_Variable(resultlist.getId_Variable());
                getsetvar.setData_Variable(resultlist.getData_Variable());
                getsetvar.setFecha_Variable(resultlist.getFecha_Variable());
                getsetvar.setHora_Variable(resultlist.getHora_Variable());
                getsetvar.setEstado_Sincronizacion_Variable(resultlist.getEstado_Sincronizacion_Variable());
                getsetvar.setId_Tipo_Variable(resultlist.getId_Tipo_Variable());
                getsetvar.setId_Equipo_Remoto(resultlist.getId_Equipo_Remoto());
                getsetvar.setNombre_Tipo_Variable(resultlist.getNombre_Tipo_Variable());
                getsetvar.setDescripcion_Tipo_Variable(resultlist.getDescripcion_Tipo_Variable());
                getsetvariable.add(getsetvar);
            }
            updateList();
            variableControllerInstance.cerrar();
        }
    }

    //Lista Tipo variables
    //Cargar Listas Notificaciones
    ///*------------------------------------------------------------------------------------------------------------*/
    private void listTipoVariables() {
        tipoVariableControllerInstance.abrirBaseDeDatos();
        getsettipovariable = new ArrayList<Tipo_Variable>();
        ///Array list para Lista de vehiculos
        ArrayList<Tipo_Variable> listado = tipoVariableControllerInstance.findAllTipoVariable();
        ////For del listado para Sincronizacion
        for (Tipo_Variable tipo : listado) {
            Tipo_Variable getsettipovar= new Tipo_Variable();
            getsettipovar.setId_Tipo_Variable(tipo.getId_Tipo_Variable());
            getsettipovar.setNombre_Tipo_Variable(tipo.getNombre_Tipo_Variable());
            getsettipovar.setPosicion_Variable(tipo.getPosicion_Variable());
            getsettipovar.setDescripcion_Variable(tipo.getDescripcion_Variable());
            getsettipovar.setEstado_Variable(tipo.getEstado_Variable());
            getsettipovar.setUnidad_Medida(tipo.getUnidad_Medida());
            getsettipovariable.add(getsettipovar);
            arrayListTipoVariable.add(tipo.getId_Tipo_Variable()+" ("+tipo.getNombre_Tipo_Variable()+")");
        }
        tipoVariableControllerInstance.cerrar();
    }


    private void  updateList(){
        AdapterReportVariable adapterr= new AdapterReportVariable(this,getsetvariable);
        listViewReporVariable.setAdapter(adapterr);
    }

    /*MENU*/
    /*-------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------------------------------------------------------------*/
    ///Menu
    /*Opciones del menu derecha*/
    /*--------------------------------------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {

            case R.id.emptyHistory:
                variableControllerInstance.abrirBaseDeDatos();
                variableControllerInstance.deleteVariables();
                variableControllerInstance.cerrar();

                listReportVariables();
                break;

            case R.id.menu_view_grafic:
                if(tipo_variable_id!=0){
                    Intent i = new Intent(this, ChartActivity.class);
                    i.putExtra("Fecha_Inicial",txt_TextDateTimeInicial.getText().toString());
                    i.putExtra("Fecha_Final",txt_TextDateTimeFinal.getText().toString());
                    i.putExtra("Tipo_Variable_Id",tipo_variable_id);
                    i.putExtra("Nombre_Tipo_Variable",nombre_tipo_variable);
                    startActivity(i);
                }else {
                    int color= Color.RED;
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.report_varibles_list), "Debe seleccionar un tipo variable", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGray));
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(color);
                    snackbar.show();
                }
                break;
            case R.id.menu_export_pdf:
                showProgressDialog();
                if(tipo_variable_id!=0){
                    createPDF();
                    openPdf();
                }else {
                    int color= Color.RED;
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.report_varibles_list), "Debe seleccionar un tipo variable", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGray));
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(color);
                    snackbar.show();
                    hideProgressDialog();
                }

                break;
            case R.id.menu_last_report:
                showProgressDialog();
                openPdf();
                break;


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
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }





    //DIALOG
    /*-----------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------*/
    //Dialog Progres
    /*--------------------------------------------------------------------------------------*/

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    ///PDF
    /*-------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------------------------------------------------------*/
    public void createPDF() {
        // Creamos el documento.
        Document documento = new Document();
        try {
            // Creamos el fichero con el nombre que deseemos.
            File f = crearFichero(NOMBRE_DOCUMENTO);
            // Creamos el flujo de datos de salida para el fichero donde
            // guardaremos el pdf.
            FileOutputStream ficheroPdf = new FileOutputStream(
                    f.getAbsolutePath());

            // Asociamos el flujo que acabamos de crear al documento.
            PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);

            // Incluimos el p�e de p�gina y una cabecera
            HeaderFooter cabecera = new HeaderFooter(new Phrase(
                    "Telecomunicaciones Inteligentes"), false);
            HeaderFooter pie = new HeaderFooter(new Phrase(
                    "Todos los derechos reservados."), false);
            documento.setHeader(cabecera);
            documento.setFooter(pie);
            // Abrimos el documento.
            documento.open();
            // A�adimos un t�tulo con la fuente por defecto.
            documento.add(new Paragraph("Reporte de medidas"));

            // A�adimos un t�tulo con una fuente personalizada.
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 28,
                    Font.BOLD, harmony.java.awt.Color.RED);
            documento.add(new Paragraph("Reporte variable: "+nombre_tipo_variable, font));

            // Insertamos una imagen que se encuentra en los recursos de la
            // aplicaci�n.
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.ic_icon_grafic);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image imagen = Image.getInstance(stream.toByteArray());
            documento.add(imagen);

            // Insertamos una tabla.
            PdfPTable tabla1 = new PdfPTable(6);
            PdfPTable tabla = new PdfPTable(6);

            Font fontTable = FontFactory.getFont(FontFactory.HELVETICA, 12,
                    Font.BOLD, harmony.java.awt.Color.RED);
            tabla1.setTableEvent(new DottedHeader());
            tabla1.setTotalWidth(PageSize.A4.getWidth()-10);
            tabla1.setLockedWidth(true);
            tabla1.getDefaultCell().setBorder(PdfPCell.ALIGN_CENTER);

            for (int i = 0; i < 1; i++) {
                ///tabla1.addCell("ENCABEZADO " + i);
                tabla1.addCell(new Paragraph("CODIGO", fontTable));
                tabla1.addCell(new Paragraph("VALOR", fontTable));
                tabla1.addCell(new Paragraph("FECHA", fontTable));
                tabla1.addCell(new Paragraph("HORA", fontTable));
                tabla1.addCell(new Paragraph("EQUIPO", fontTable));
                tabla1.addCell(new Paragraph("PROYECTO", fontTable));
            }
            documento.add(tabla1);
            documento.add(new Paragraph("\n"));
            tabla.setTotalWidth(PageSize.A4.getWidth()-10);
            tabla.setLockedWidth(true);

            int countFile= 0;
            for (Variable resultlist : getsetvariable) {
                countFile=countFile+1;


                SimpleDateFormat sdfEnd = new SimpleDateFormat("H:mm");
                Date dateObjEnd = null;
                try {
                    dateObjEnd = sdfEnd.parse(resultlist.getHora_Variable());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                tabla.addCell("" + countFile);
                tabla.addCell(resultlist.getData_Variable());
                tabla.addCell(resultlist.getFecha_Variable());
                tabla.addCell(new SimpleDateFormat("KK:mm a").format(dateObjEnd));
                tabla.addCell("Sensor de "+resultlist.getNombre_Tipo_Variable());
                tabla.addCell("Inmunizador Guadua");
            }

            documento.add(tabla);
            // Agregar marca de agua
            font = FontFactory.getFont(FontFactory.HELVETICA, 42, Font.BOLD,
                    harmony.java.awt.Color.GRAY);
            ColumnText.showTextAligned(writer.getDirectContentUnder(),
                    Element.ALIGN_CENTER, new Paragraph(
                            "Decoguadua", font), 297.5f, 421,
                    writer.getPageNumber() % 2 == 1 ? 45 : -45);

            Toast.makeText(getApplicationContext(), "REPORTE CREADO...", Toast.LENGTH_LONG).show();
            hideProgressDialog();

        } catch (DocumentException e) {

            Log.e(ETIQUETA_ERROR, e.getMessage());

        } catch (IOException e) {

            Log.e(ETIQUETA_ERROR, e.getMessage());

        } finally {
            // Cerramos el documento.
            documento.close();
        }
    }



    class DottedHeader implements PdfPTableEvent {

        public void tableLayout(PdfPTable table, float[][] widths,
                                float[] heights, int headerRows, int rowStart,
                                PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
            canvas.setLineDash(3f, 3f);
            float x1 = widths[0][0];
            float x2 = widths[0][widths.length];
            canvas.moveTo(x1, heights[0]);
            canvas.lineTo(x2, heights[0]);
            canvas.moveTo(x1, heights[headerRows]);
            canvas.lineTo(x2, heights[headerRows]);
            canvas.stroke();
        }
    }
    /**
     * Crea un fichero con el nombre que se le pasa a la funci�n y en la ruta
     * especificada.
     *
     * @param nombreFichero
     * @return
     * @throws IOException
     */
    public static File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }

    /**
     * Obtenemos la ruta donde vamos a almacenar el fichero.
     *
     * @return
     */
    public static File getRuta() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + NOMBRE_DIRECTORIO;
        File ruta = new File(path);
        if(!ruta.exists())
            ruta.mkdirs();
        // El fichero ser� almacenado en un directorio dentro del directorio
        // Descargas
       /* File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    NOMBRE_DIRECTORIO);

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }
*/
        return ruta;
    }

    public void openPdf()
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + NOMBRE_DIRECTORIO;
        File file = new File(path, NOMBRE_DOCUMENTO);
        intent.setDataAndType( Uri.fromFile( file ), "application/pdf" );
        startActivity(intent);
        hideProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
