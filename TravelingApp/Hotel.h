//
//  Hotel.h
//  TravelingApp
//
//  Created by Emerson on 14-4-14.
//  Copyright (c) 2014年 Emerson. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Hotel : NSObject

@property (nonatomic, strong) NSString * hotelName;
@property (nonatomic, strong) NSString * longtitude;
@property (nonatomic, strong) NSString * latitude;
@property (nonatomic, strong) NSString * cityName;
@property (nonatomic, strong) NSString * regionName;
@property (nonatomic, strong) NSString * hotelAddress;

@property (nonatomic, strong) NSString * hotelCtripId;
@property (nonatomic, strong) NSString * hotelHotwireId;
@property (nonatomic, strong) NSString * hotelDZDPId;
@property (nonatomic, strong) NSString * hotelTCId;

@property (nonatomic) int searchingType;

@end
