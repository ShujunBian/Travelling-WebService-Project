//
//  CDINetClient.h
//  CDI_iPad_App
//
//  Created by Gabriel Yeah on 13-3-30.
//  Copyright (c) 2013年 Gabriel Yeah. All rights reserved.
//

#import <Foundation/Foundation.h>

@class CDIEvent;

@interface TravelNetClient : NSObject

+ (TravelNetClient *)client;
//@RequestMapping(value = "CTrip/{hotelCityCode}/{AreaID}/{hotelName}/{isHotelStarRate}/{rating}"

- (void)searchHotelWith:(NSString *)hotelCityCode
                 areaID:(NSString *)areaName
              hotelName:(NSString *)hotelName
        isHotelStarRate:(int)isHotelStarRate
                 rating:(int)rating
         isSpecailHotel:(BOOL)isSpecailHotel
     succededCompletion:(void (^)(BOOL succeeded, id responseData))succededCompletion
       failedCompletion:(void (^) (void))failedCompletion;


- (void)searchHotelDetailWith:(NSString *)hotelID
                      APIType:(int)apiType
           succededCompletion:(void (^)(BOOL succeeded, id responseData))succededCompletion
             failedCompletion:(void (^) (void))failedCompletion;

- (void)searchHotelWithhotelName:(NSString *)hotelName
                       latitude:(NSString *)latitude
                     longtitude:(NSString *)longtitude
             succededCompletion:(void (^)(BOOL succeeded, id responseData))succededCompletion
               failedCompletion:(void (^) (void))failedCompletion;

@end
