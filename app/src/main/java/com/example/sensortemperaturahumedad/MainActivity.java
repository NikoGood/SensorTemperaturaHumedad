package com.example.sensortemperaturahumedad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView temperaturaTextView; //Vista de textos en las que se mostrara los valores
    private TextView humedadTextView;
    private SensorManager sensorManager; // instancia que se utiliza para acceder a los sensores del dispositivo
    private Sensor temperaturaSensor; //objetos tipo sensor que representa los sensores de temperatura y humedad
    private Sensor humedadSensor;
    private Boolean temperaturaDisponible; // indican los sensores de temperatura y humedad estan disponibles
    private Boolean humedadDisponible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperaturaTextView = findViewById(R.id.Temp);
        humedadTextView = findViewById(R.id.Humed);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Verifica la disponibilidad de los sensores
        temperaturaDisponible = false;
        humedadDisponible = false;

        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            temperaturaSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            temperaturaDisponible = true;
        }else{
            temperaturaTextView.setText("El sensor de temperatura no está disponible");
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null){
            humedadSensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
            humedadDisponible = true;
        }else{
            humedadTextView.setText("El sensor de humedad no está disponible");
        }
    }
    //Detecta un cambio de valores de los sensores
    //verifica el tipo de sensor y actualiza los Textview correspondientes con los valores de temperatura y humedad
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            temperaturaTextView.setText(sensorEvent.values[0]+ " °C");
        }else if(sensorEvent.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY){
            humedadTextView.setText(sensorEvent.values[0] + " %");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    //Se registran los sensores de temperatura y humedad si estan disponibles
    @Override
    protected void onResume(){
        super.onResume();
        if (temperaturaDisponible){
            sensorManager.registerListener(this, temperaturaSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (humedadDisponible){
            sensorManager.registerListener(this, humedadSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    //Se anulan los registros de los sensores para evitar el consumo innecesario de energia cuando la actividad no esta en primer plano
    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}