package com.github.surenyonjan.device_orientation_reader;

import android.util.Log;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.view.Surface;
import android.view.WindowManager;

public class OrientationReader {

  public OrientationReader(Context context) {
    this.context = context;
  }

  private final Context context;

  public SensorManager getSensorManager() {
    return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
  }

  public Orientation calculateDeviceOrientation(float[] mGravity) {

    Orientation actualOrientation;

    float[] inclineGravity = mGravity.clone();

    double norm_Of_g = Math.sqrt(inclineGravity[0] * inclineGravity[0] + inclineGravity[1] * inclineGravity[1] + inclineGravity[2] * inclineGravity[2]);

    // Normalize the accelerometer vector
    inclineGravity[0] = (float) (inclineGravity[0] / norm_Of_g);
    inclineGravity[1] = (float) (inclineGravity[1] / norm_Of_g);
    inclineGravity[2] = (float) (inclineGravity[2] / norm_Of_g);

    //Checks if device is flat on ground or not
    int inclination = (int) Math.round(Math.toDegrees(Math.acos(inclineGravity[2])));

    Log.i("Inclination", String.format("%d", inclination));
    if (inclination < 25) {
      return Orientation.FaceUp;
    } else if (inclination > 140) {
      return Orientation.FaceDown;
    }

    final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
    final int orientation = context.getResources().getConfiguration().orientation;

    switch (orientation) {
      case Configuration.ORIENTATION_PORTRAIT:
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
          actualOrientation = Orientation.PortraitUp;
        } else {
          actualOrientation = Orientation.PortraitDown;
        }
        break;
      case Configuration.ORIENTATION_LANDSCAPE:
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
          actualOrientation = Orientation.LandscapeLeft;
        } else {
          actualOrientation = Orientation.LandscapeRight;
        }
        break;
      default:
        actualOrientation = Orientation.Unknown;
    }

    return actualOrientation;
  }

  public enum Orientation {
    PortraitUp,
    PortraitDown,
    LandscapeLeft,
    LandscapeRight,
    FaceUp,
    FaceDown,
    Unknown
  }
}