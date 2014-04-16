//
//  CDINetClient.m
//  CDI_iPad_App
//
//  Created by Gabriel Yeah on 13-3-30.
//  Copyright (c) 2013年 Gabriel Yeah. All rights reserved.
//

#import "TravelNetClient.h"
#import "AFHTTPClient.h"
#import "JSONKit.h"
#import "NSString+Encrypt.h"

#define kBaseURLString @"http://localhost:8080/HotelWebApplication/webService/"

static TravelNetClient *sharedClient;

@interface TravelNetClient ()

@property (nonatomic, strong) AFHTTPClient *afClient;
@property (nonatomic, strong) id responseData;

@end

@implementation TravelNetClient

+ (TravelNetClient *)client {
    if(!sharedClient) {
        sharedClient = [[TravelNetClient alloc] init];
    }
    return sharedClient;
}

#pragma mark - Fetch Data Methods
- (void)searchHotelWith:(NSString *)hotelCityCode
                 areaID:(NSString *)areaName
              hotelName:(NSString *)hotelName
        isHotelStarRate:(int)isHotelStarRate
                 rating:(int)rating
         isSpecailHotel:(BOOL)isSpecailHotel
     succededCompletion:(void (^)(BOOL, id))succededCompletion failedCompletion:(void (^)(void))failedCompletion
{
    NSString *path;
    if (isSpecailHotel) {
        path = [[NSString stringWithFormat:@"getHotel/All/%@/%@/%@/%d/-1/1",hotelCityCode,areaName,hotelName,rating] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    }
    else
        path = [[NSString stringWithFormat:@"getHotel/All/%@/%@/%@/%d/-1/-1",hotelCityCode,areaName,hotelName,rating] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    [self getPath:path suceededCompletion:succededCompletion failedCompletion:failedCompletion];
}

- (void)searchHotelDetailWith:(NSString *)hotelID APIType:(int)apiType
           succededCompletion:(void (^)(BOOL, id))succededCompletion
             failedCompletion:(void (^)(void))failedCompletion
{
    NSString * path;
    switch (apiType) {
        case 0:
        {
            path = [[NSString stringWithFormat:@"getHotelInfo/CTrip/%@",hotelID] replaceUTF8];
            break;
        }
        case 1:
        {
            path = [[NSString stringWithFormat:@"getHotelInfo/Hotwire/%@",hotelID] replaceUTF8];
            break;
        }
        case 2:
        {
            path = [[NSString stringWithFormat:@"getHotelInfo/DZDP/%@",hotelID] replaceUTF8];
            break;
        }
        case 3:
        {
            path = [[NSString stringWithFormat:@"getHotelInfo/TC/%@",hotelID] replaceUTF8];
            break;
        }
        default:
            break;
    }
    [self getPath:path suceededCompletion:succededCompletion failedCompletion:failedCompletion];
}


#pragma mark - Basic Methods
- (void)getPath:(NSString *)path
suceededCompletion:(void (^)(BOOL succeeded, id responseData))suceededCompletion
failedCompletion:(void(^)(void))failedCompletion
{
    BlockARCWeakSelf weakSelf = self;
    NSLog(@"The URL is %@",path);
    [self.afClient getPath:path parameters:nil
                   success:^(AFHTTPRequestOperation *operation, id responseObject) {
                       [weakSelf handleResponseData:responseObject withCompletion:suceededCompletion];
                   } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                       NSLog(@"GET FAILED");
                   }];
}

- (void)postPath:(NSString *)path
      dictionary:(NSDictionary *)dict
suceededCompletion:(void (^)(BOOL succeeded, id responseData))suceededCompletion
failedCompletion:(void(^)(void))failedCompletion
{
    BlockARCWeakSelf weakSelf = self;
    [self.afClient postPath:path parameters:dict success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [weakSelf handleResponseData:responseObject withCompletion:suceededCompletion];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];
}

- (void)putPath:(NSString *)path
     dictionary:(NSDictionary *)dict
     completion:(void (^)(BOOL succeeded, id responseData))completion
{
    BlockARCWeakSelf weakSelf = self;
    [self.afClient putPath:path parameters:dict success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [weakSelf handleResponseData:responseObject withCompletion:completion];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        if (completion) {
            completion(NO, nil);
        }
    }];
}

- (void)deletePath:(NSString *)path
        dictionary:(NSDictionary *)dict
        completion:(void (^)(BOOL succeeded, id responseData))completion
{
    BlockARCWeakSelf weakSelf = self;
    [self.afClient deletePath:path parameters:dict success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [weakSelf handleResponseData:responseObject withCompletion:completion];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        if (completion) {
            completion(NO, nil);
        }
    }];
}

- (void)handleResponseData:(id)responseData
            withCompletion:(void (^)(BOOL succeeded, id responseData))completion
{
    self.responseData = (NSString *)responseData;
    JSONDecoder *decoder = [JSONDecoder decoder];
    self.responseData = [decoder objectWithData:responseData];
    if ([self.responseData isKindOfClass:[NSArray class]]) {
        if (completion) {
            completion(YES, self.responseData);
        }
    }
}


#pragma mark - Properties
- (AFHTTPClient *)afClient
{
    if (!_afClient) {
        NSURL *baseURL = [NSURL URLWithString:kBaseURLString];
        _afClient = [AFHTTPClient clientWithBaseURL:baseURL];
        _afClient.parameterEncoding = AFJSONParameterEncoding;
    }
    return _afClient;
}

@end
