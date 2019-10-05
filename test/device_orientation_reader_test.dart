import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:device_orientation_reader/device_orientation_reader.dart';

void main() {
  const MethodChannel channel = MethodChannel('device_orientation_reader');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

}
