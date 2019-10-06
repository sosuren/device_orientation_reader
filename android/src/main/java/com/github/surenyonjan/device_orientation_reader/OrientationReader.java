package com.github.surenyonjan.device_orientation_reader;

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

    if (inclination < 25) {
      actualOrientation = Orientation.FaceUp;
    } else if (inclination > 140) {
      actualOrientation = Orientation.FaceDown;
    } else {

      if (Math.abs(mGravity[0]) > Math.abs(mGravity[1])) {
        // we are in landscape-mode
        if (mGravity[0] >= 0) {
          actualOrientation = Orientation.LandscapeRight;
        } else {
          actualOrientation = Orientation.LandscapeLeft;
        }
      } else {
        // we are in portrait mode
        if (mGravity[1] >= 0) {
          actualOrientation = Orientation.PortraitDown;
        } else {
          actualOrientation = Orientation.PortraitUp;
        }
      }
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