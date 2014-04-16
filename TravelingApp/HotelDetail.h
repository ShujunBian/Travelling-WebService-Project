//
//  HotelDetail.h
//  TravelingApp
//
//  Created by Emerson on 14-4-16.
//  Copyright (c) 2014年 Emerson. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface HotelDetail : NSObject

@property (nonatomic, strong) NSString * hotelName;
@property (nonatomic, strong) NSString * hotelId;
@property (nonatomic, strong) NSString * hotelDecrpition;
@property (nonatomic) int hotelApiType;
@property (nonatomic, strong) NSString * imageUrl;

@end
