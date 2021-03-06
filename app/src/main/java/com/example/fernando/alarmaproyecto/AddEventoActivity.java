package com.example.fernando.alarmaproyecto;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.icu.text.UnicodeSetSpanner;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddEventoActivity extends AppCompatActivity {

    Spinner opciones, tiempos;
    EditText descripcion, cantidad;
    Switch todo_dia;
    ArrayList<String> lista, listaAux;
    Button agregar, cancelar;
    TextView hora_inicio, hora_fin, fecha_inicio, fecha_fin;
    String selectedDate = "";
    int hora, minuto;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.acciones, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.atras:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_evento);
        opciones=(Spinner)findViewById(R.id.opciones);
        descripcion=(EditText)findViewById(R.id.descripcion);
        todo_dia=(Switch)findViewById(R.id.todo_dia);
        lista=new ArrayList<>();
        listaAux=new ArrayList<>();
        hora_inicio=(TextView)findViewById(R.id.hora_inicio);
        fecha_inicio=(TextView)findViewById(R.id.fecha_inicio);
        hora_fin=(TextView)findViewById(R.id.hora_fin);
        fecha_fin=(TextView)findViewById(R.id.fecha_fin);
        fecha_fin.setText("12/08/2018");
        fecha_inicio.setText("10/05/2018");
        hora_inicio.setText("12:00 am");
        hora_fin.setText("01:00 pm");

        todo_dia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    SimpleDateFormat fecha=new SimpleDateFormat("dd/MM/yyyy");
                    Date d=new Date();
                    fecha_fin.setText(fecha.format(d));
                    fecha_inicio.setText(fecha.format(d));
                    hora_inicio.setText("Todo el día");
                    hora_fin.setText("Todo el día");
                }
                else {
                    fecha_fin.setText("12/08/2018");
                    fecha_inicio.setText("10/05/2018");
                    hora_inicio.setText("12:00 am");
                    hora_fin.setText("01:00 pm");
                }
            }
        });

        opciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==3) {
                    final AlertDialog.Builder dialog=new AlertDialog.Builder(AddEventoActivity.this);
                    view=getLayoutInflater().inflate(R.layout.dialogo, null);
                    tiempos=(Spinner)view.findViewById(R.id.tiempos);
                    cantidad=(EditText)view.findViewById(R.id.cantidad);
                    llenarTiempos();

                    dialog.setTitle("Personalizado");
                    dialog.setMessage("Agregue un tiempo determinado.");

                    dialog.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!cantidad.getText().toString().isEmpty() && Integer.parseInt(cantidad.getText().toString())>0 && !tiempos.getSelectedItem().toString().equals("Tiempo")) {
                                Toast.makeText(getBaseContext(), "Agregado con exito.", Toast.LENGTH_SHORT).show();
                                agregarTiempo();
                            }
                            else
                                Toast.makeText(getBaseContext(), "Ingrese el tiempo.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    /*

                    agregar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!cantidad.getText().toString().isEmpty() && !tiempos.getSelectedItem().toString().equals("Tiempo")) {
                                Snackbar.make(null, "Agregado con exito.", Snackbar.LENGTH_SHORT).show();
                                agregarTiempo();
                            }
                            else
                                Snackbar.make(null, "Ingrese el tiempo.", Snackbar.LENGTH_SHORT).show();
                        }
                    });

                    */
                    dialog.setView(view);
                    AlertDialog mDialogo=dialog.create();
                    mDialogo.show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        llenar();
    }

    public void fecha(View view) {
        fecha_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 porque enero es cero
                        String d = "" + day, m = "" + (month + 1);
                        if (day < 10) d = "0" + day;
                        if ((month + 1) < 10) m = "0" + (month + 1);

                        selectedDate = d + "/" + m + "/" + year;

                        fecha_inicio.setText(selectedDate);
                        Snackbar.make(v, "Mensaje fecha", Snackbar.LENGTH_SHORT).show();
                    }
                });
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        fecha_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 porque enero es cero
                        String d = "" + day, m = "" + (month + 1);
                        if (day < 10) d = "0" + day;
                        if ((month + 1) < 10) m = "0" + (month + 1);

                        selectedDate = d + "/" + m + "/" + year;

                        fecha_fin.setText(selectedDate);
                        Snackbar.make(v, "Mensaje fecha", Snackbar.LENGTH_SHORT).show();
                    }
                });
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    public void hora(View view) {
        hora_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ponerHora(v, 1);
            }
        });

        hora_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ponerHora(v, 2);
            }
        });
    }

    private void ponerHora(final View v, final int id) {
        Calendar calendar=Calendar.getInstance();
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                hora=hour;
                final String horario;
                if(hora>12) {
                    hora -= 12;
                    horario="pm";
                }
                else
                    horario="am";

                minuto=minutes;
                if(id==1)
                    hora_inicio.setText(hora+":"+minuto+" "+horario);
                else
                    hora_fin.setText(hora+":"+minuto+" "+horario);
                Snackbar.make(v, "Mensaje hora", Snackbar.LENGTH_SHORT).show();
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    private void agregarTiempo() {
        finish();
    }

    private void llenar() {
        lista=new ArrayList<>();
        lista.add("A la misma hora");
        lista.add("30 minutos antes");
        lista.add("1 hora antes");
        lista.add("Personalizado");

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        opciones.setAdapter(adapter);
    }

    private void llenarTiempos() {
        listaAux=new ArrayList<>();
        listaAux.add("Tiempo");
        listaAux.add("Minuto(s)");
        listaAux.add("Hora(s)");
        listaAux.add("Dia(s)");

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaAux);
        tiempos.setAdapter(adapter);
    }
}
