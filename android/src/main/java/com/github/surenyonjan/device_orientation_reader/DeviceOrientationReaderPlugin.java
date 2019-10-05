package com.github.surenyonjan.device_orientation_reader;

import android.content.Context;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** DeviceOrientationReaderPlugin */
public class DeviceOrientationReaderPlugin implements EventChannel.StreamHandler {

  private static final String EVENT_CHANNEL = "com.github.surenyonjan/device_orientation_reader/orientationevent";

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final EventChannel eventChannel = new EventChannel(registrar.messenger(), EVENT_CHANNEL);
    eventChannel.setStreamHandler(new DeviceOrientationReaderPlugin(registrar.activeContext()));
  }

  private final OrientationReader reader;
  private IOrientationListener listener;

  private DeviceOrientationReaderPlugin(Context context) {
    this.reader = new OrientationReader(context);
  }

  @Override
  public void onListen(Object parameters, final EventChannel.EventSink eventSink) {

    // initialize the callback. It is the same for both listeners.
    IOrientationListener.OrientationCallback callback = new IOrientationListener.OrientationCallback() {

      @Override
      public void receive(OrientationReader.Orientation orientation) {
        eventSink.success(orientation.name());
      }
    };

    this.listener = new SensorOrientationListener(this.reader, callback);
    this.listener.startOrientationListener();
  }

  @Override
  public void onCancel(Object o) {
    if (this.listener != null) {
      this.listener.stopOrientationListener();
      this.listener = null;
    }
  }
}
