import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

enum NativeDeviceOrientation {
  portraitUp, portraitDown, landscapeLeft, landscapeRight, faceUp, faceDown, unknown
}

class DeviceOrientationReader {

  static DeviceOrientationReader _instance;

  final EventChannel _eventChannel;
  Stream<NativeDeviceOrientation> _stream;

  factory DeviceOrientationReader() {
    if (_instance == null) {

      final EventChannel _eventChannel = const EventChannel('com.github.surenyonjan/device_orientation_reader/orientationevent');
      _instance = new DeviceOrientationReader._private(_eventChannel);
    }
    return _instance;
  }

  DeviceOrientationReader._private(this._eventChannel);

  Stream<NativeDeviceOrientation> get orientationStream {
    if (_stream == null) {
      _stream = this._eventChannel.receiveBroadcastStream().map((dynamic event) {

        return this._fromString(event);
      });
    }

    return _stream;
  }

  NativeDeviceOrientation _fromString(String orientationString) {
    switch (orientationString) {
      case "PortraitUp":
        return NativeDeviceOrientation.portraitUp;
      case "PortraitDown":
        return NativeDeviceOrientation.portraitDown;
      case "LandscapeRight":
        return NativeDeviceOrientation.landscapeRight;
      case "LandscapeLeft":
        return NativeDeviceOrientation.landscapeLeft;
      case "FaceUp":
        return NativeDeviceOrientation.faceUp;
      case "FaceDown":
        return NativeDeviceOrientation.faceDown;
      case "Unknown":
      default:
        return NativeDeviceOrientation.unknown;
    }
  }
}
