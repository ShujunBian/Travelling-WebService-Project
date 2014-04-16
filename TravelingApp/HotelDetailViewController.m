//
//  HotelDetailViewController.m
//  TravelingApp
//
//  Created by Emerson on 14-4-10.
//  Copyright (c) 2014年 Emerson. All rights reserved.
//

#import "HotelDetailViewController.h"
#import "TravelNetClient.h"
@interface HotelDetailViewController ()

@end

@implementation HotelDetailViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewWillAppear:(BOOL)animated
{
    TravelNetClient *client = [TravelNetClient client];
    void (^handleData)(BOOL succeeded, id responseData) = ^(BOOL succeeded, id responseData){
        if (!succeeded) {
            NSLog(@"Failed!");
        }
        else {
            if ([responseData isKindOfClass:[NSArray class]]) {
                for (NSDictionary * hotelDict in responseData) {
                    NSLog(@"%@",hotelDict);
                    [_hotelsArray addObject:[self getHotelByDicitionary:hotelDict]];
                }
                [self.mainViewDelegate didFectchHotelDataWithArray:_hotelsArray];
            }
            else
                NSLog(@"response is not NSArray");
        }
    };
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
