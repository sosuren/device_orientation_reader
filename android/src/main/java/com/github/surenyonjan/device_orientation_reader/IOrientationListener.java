package com.github.surenyonjan.device_orientation_reader;

public interface IOrientationListener {

  interface OrientationCallback {
    void receive(OrientationReader.Orientation orientation);
  }

  void startOrientationListener();

  void stopOrientationListener();
}