//
//  NSNotificationCenter+Addition.m
//  CDI_iPad_App
//
//  Created by Gabriel Yeah on 13-1-24.
//  Copyright (c) 2013年 Gabriel Yeah. All rights reserved.
//

#import "NSNotificationCenter+Addition.h"

#define kDidFetchHotelDetail @"kDidFetchHotelDetail"

@implementation NSNotificationCenter (Addition)

+ (void)postDidFetchHotelNameNotification
{
    [[NSNotificationCenter defaultCenter] postNotificationName:kDidFetchHotelDetail object:nil userInfo:nil];
}

+ (void)registerpostDidFetchHotelNameNotificationWithSelector:(SEL)aSelector target:(id)aTarget
{
    NSNotificationCenter *center = [NSNotificationCenter defaultCenter];
    [center addObserver:aTarget selector:aSelector
                   name:kDidFetchHotelDetail
                 object:nil];
}

+ (void)unregister:(id)target
{
    [[NSNotificationCenter defaultCenter] removeObserver:target];
}
@end
