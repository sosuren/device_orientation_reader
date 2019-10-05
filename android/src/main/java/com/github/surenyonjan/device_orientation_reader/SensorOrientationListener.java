package com.github.surenyonjan.device_orientation_reader;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;

public class SensorOrientationListener implements IOrientationListener {

  private final OrientationReader orientationReader;
  private final OrientationCallback callback;
  private OrientationReader.Orientation lastOrientation = null;

  private SensorManager sensorManager;
  private Sensor acceleroMeter;
  private SensorEventListener sensorEventListener;

  public SensorOrientationListener(OrientationReader orientationReader, OrientationCallback callback) {
    this.orientationReader = orientationReader;
    this.callback = callback;
  }

  @Override
  public void startOrientationListener() {

    if (this.sensorEventListener != null) return;
    sensorManager = this.orientationReader.getSensorManager();
    acceleroMeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    sensorEventListener = new SensorEventListener() {

      @Override
      public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
          OrientationReader.Orientation newOrientation = orientationReader.calculateDeviceOrientation(sensorEvent.values);

          if (!newOrientation.equals(lastOrientation)) {
            lastOrientation = newOrientation;
            callback.receive(newOrientation);
          }
        }
      }

      @Override
      public void onAccuracyChanged(Sensor sensor, int i) {

      }
    };

    sensorManager.registerListener(sensorEventListener, acceleroMeter, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  public void stopOrientationListener() {
    if (sensorEventListener == null) return;
    sensorManager.unregisterListener(sensorEventListener);
    sensorEventListener = null;
    acceleroMeter = null;
    sensorManager = null;
  }
}