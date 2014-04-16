//
//  NSNotificationCenter+Addition.h
//  CDI_iPad_App
//
//  Created by Gabriel Yeah on 13-1-24.
//  Copyright (c) 2013年 Gabriel Yeah. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSNotificationCenter (Addition)


+ (void)postDidFetchHotelNameNotification;
+ (void)registerpostDidFetchHotelNameNotificationWithSelector:(SEL)aSelector target:(id)aTarget;

+ (void)unregister:(id)target;

@end
