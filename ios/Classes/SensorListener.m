#import "SensorListener.h"

@implementation SensorListener {
    CMMotionManager* motionManager;
    NSString* lastOrientation;
}

- (void)initMotionManager {
    if (!motionManager) {
        motionManager = [[CMMotionManager alloc] init];
    }
}

- (void)startOrientationListener:(void (^)(NSString* orientation)) orientationRetrieved {
    [self initMotionManager];
    if([motionManager isDeviceMotionAvailable] == YES){
        motionManager.deviceMotionUpdateInterval = 0.1;

        [motionManager startDeviceMotionUpdatesToQueue:[NSOperationQueue mainQueue] withHandler:^(CMDeviceMotion *data, NSError *error) {
            NSString *orientation;

            double normOfGravity = sqrt(data.gravity.x * data.gravity.y + data.gravity.y * data.gravity.y + data.gravity.z * data.gravity.z);
            int inclination = (int) round(acos((float) -1 * data.gravity.z / normOfGravity) * (180.0 / M_PI));

            if (inclination < 25) {

                orientation = FACE_UP;
            } else if (inclination > 140) {

                orientation = FACE_DOWN;
            } else {

                if(fabs(data.gravity.x)>fabs(data.gravity.y)){
                    // we are in landscape-mode
                    if(data.gravity.x>=0){
                        orientation = LANDSCAPE_RIGHT;
                    }
                    else{
                        orientation = LANDSCAPE_LEFT;
                    }
                }
                else{
                    // we are in portrait mode
                    if(data.gravity.y>=0){
                        orientation = PORTRAIT_DOWN;
                    }
                    else{
                        orientation = PORTRAIT_UP;
                    }
                }
            }

            if (self->lastOrientation == nil || ![orientation isEqualToString:(self->lastOrientation)]) {
                self->lastOrientation = orientation;
                orientationRetrieved(orientation);
            }
        }];
    }
}

- (void) getOrientation:(void (^)(NSString* orientation)) orientationRetrieved {

    [self startOrientationListener:^(NSString *orientation) {
        orientationRetrieved(orientation);

        // we have received a orientation stop the listener. We only want to return one orientation
        [self stopOrientationListener];
    }];
}

- (void)stopOrientationListener {
    if (motionManager != NULL && [motionManager isDeviceMotionActive] == YES) {
        [motionManager stopDeviceMotionUpdates];
    }
}


@end


