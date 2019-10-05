import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:device_orientation_reader/device_orientation_reader.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  DeviceOrientationReader deviceOrientationReader = DeviceOrientationReader();

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Device Orientation example app'),
        ),
        body: StreamBuilder(
          stream: deviceOrientationReader.orientationStream,
          builder: (BuildContext context, AsyncSnapshot<NativeDeviceOrientation> asyncData) {

            Widget widget;
            if (!asyncData.hasData) {
              widget = CircularProgressIndicator();
            } else {
              widget = Text(asyncData.data.toString());
            }

            return Center(
              child: widget,
            );
          },
        ),
      ),
    );
  }
}
