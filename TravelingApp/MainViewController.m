//
//  ViewController.m
//  TravelingApp
//
//  Created by Emerson on 14-4-9.
//  Copyright (c) 2014年 Emerson. All rights reserved.
//

#import "MainViewController.h"
#import "TravelNetClient.h"
#import "SearchResultViewController.h"
#import "Hotel.h"
#import "NSString+Encrypt.h"
#import "NSNotificationCenter+Addition.h"

@interface MainViewController ()

@property (nonatomic, strong) NSMutableArray * hotelsArray;

@end

@implementation MainViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.navigationController setNavigationBarHidden:YES];
	// Do any additional setup after loading the view, typically from a nib.
    UITapGestureRecognizer * touch = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(touchView)];
    [self.view addGestureRecognizer:touch];
    self.hotelsArray = [NSMutableArray arrayWithCapacity:20];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)touchView
{
    [self.view endEditing:YES];
}

- (IBAction)clickSearchButton:(id)sender {
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
    
    NSString * cityName;
    NSString * regionName;
    NSString * hotelName;
    int rating;
    
    if (![_cityNameTextField.text isEqualToString:@""]) {
        cityName = _cityNameTextField.text;
    }
    if (![_regionTextField.text isEqualToString:@""]) {
        regionName = _regionTextField.text;
    }
    else
        regionName = @"-1";
    
    if (![_hotelTextField.text isEqualToString:@""]) {
        hotelName = _hotelTextField.text;
    }
    else
        hotelName = @"-1";
    
    if (![_ratingTextField.text isEqualToString:@""]) {
        rating = _ratingTextField.text.intValue;
    }
    else
        rating = -1;
    
    [client searchHotelWith:cityName areaID:regionName
                  hotelName:hotelName isHotelStarRate:YES rating:rating isSpecailHotel:NO
         succededCompletion:handleData failedCompletion:nil];
    
    [self performSegueWithIdentifier:@"search" sender:sender];
}

- (Hotel *)getHotelByDicitionary:(NSDictionary *)dataDictionary
{
    Hotel * tempHotel = [[Hotel alloc]init];
    if ([dataDictionary objectForKey:@"hotelName"] != [NSNull null]) {
        tempHotel.hotelName = [[dataDictionary objectForKey:@"hotelName"]replaceUTF8];
    }
    if ([dataDictionary objectForKey:@"cityName"] != [NSNull null]) {
        tempHotel.cityName = [[dataDictionary objectForKey:@"cityName"]replaceUTF8];
    }
    if ([dataDictionary objectForKey:@"hotelAddress"] != [NSNull null]) {
        tempHotel.hotelAddress = [[dataDictionary objectForKey:@"hotelAddress"]replaceUTF8];
    }
    if ([dataDictionary objectForKey:@"hotelCtripId"] != [NSNull null]) {
        tempHotel.hotelCtripId = [[dataDictionary objectForKey:@"hotelCtripId"]replaceUTF8];
    }
    if ([dataDictionary objectForKey:@"hotelDZDPId"] != [NSNull null]) {
        tempHotel.hotelDZDPId = [[dataDictionary objectForKey:@"hotelDZDPId"]replaceUTF8];
    }
    if ([dataDictionary objectForKey:@"hotelHotwireId"] != [NSNull null]) {
        tempHotel.hotelHotwireId = [[dataDictionary objectForKey:@"hotelHotwireId"]replaceUTF8];
    }
    if ([dataDictionary objectForKey:@"hotelTCId"] != [NSNull null]) {
        tempHotel.hotelTCId = [[dataDictionary objectForKey:@"hotelTCId"]replaceUTF8];
    }
    if ([dataDictionary objectForKey:@"latitude"] != [NSNull null]) {
        tempHotel.latitude = [[dataDictionary objectForKey:@"latitude"]replaceUTF8];
    }
    if ([dataDictionary objectForKey:@"longtitude"] != [NSNull null]) {
        tempHotel.longtitude = [[dataDictionary objectForKey:@"longtitude"]replaceUTF8];
    }
    if ([dataDictionary objectForKey:@"regionName"] != [NSNull null]) {
        tempHotel.regionName = [[dataDictionary objectForKey:@"regionName"]replaceUTF8];
    }
    if ([dataDictionary objectForKey:@"searchingType"] != [NSNull null]) {
        tempHotel.searchingType = [[dataDictionary objectForKey:@"searchingType"] intValue];
    }
    return tempHotel;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.identifier isEqualToString:@"search"]) {
        SearchResultViewController * searchView = (SearchResultViewController *)segue.destinationViewController;
        self.mainViewDelegate = searchView;
    }
}
@end
