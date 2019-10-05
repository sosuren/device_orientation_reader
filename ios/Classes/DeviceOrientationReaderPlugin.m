#import "DeviceOrientationReaderPlugin.h"
#import "SensorListener.h"
#import "IOrientationListener.h"

NSString* const EVENT_CHANNEL = @"com.github.surenyonjan/device_orientation_reader/orientationevent";

@interface DeviceOrientationReaderPlugin ()
@property id observer;
@property (copy) void (^orientationRetrieved)(NSString *orientation);
@end

@implementation DeviceOrientationReaderPlugin

id<IOrientationListener> listener;

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterEventChannel* eventChannel = [FlutterEventChannel eventChannelWithName:EVENT_CHANNEL binaryMessenger:[registrar messenger]];
  DeviceOrientationReaderPlugin* instance = [[DeviceOrientationReaderPlugin alloc] init];
  [eventChannel setStreamHandler:instance];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  } else {
    result(FlutterMethodNotImplemented);
  }
}

- (FlutterError* _Nullable)onListenWithArguments:(id _Nullable)arguments
                                       eventSink:(FlutterEventSink)events {

    listener = [[SensorListener alloc] init];
    _orientationRetrieved = ^(NSString *orientation){
        events(orientation);
    };
    [listener startOrientationListener:_orientationRetrieved];

    return NULL;
}

- (FlutterError* _Nullable)onCancelWithArguments:(id _Nullable)arguments {
    if(listener != NULL){
        [listener stopOrientationListener];
    }
    return NULL;
}

@end
