//
//  PostionSearchViewController.m
//  TravelingApp
//
//  Created by Emerson on 14-4-9.
//  Copyright (c) 2014年 Emerson. All rights reserved.
//

#import "PostionSearchViewController.h"
#import "PositionSearchTableViewCell.h"
#import "TravelNetClient.h"
#import "NSString+Encrypt.h"
#import "Hotel.h"
@interface PostionSearchViewController ()

@property (nonatomic, strong) NSMutableArray * hotelArray;

@end

@implementation PostionSearchViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    _hotelArray = [NSMutableArray arrayWithCapacity:20];
    // Do any additional setup after loading the view.
    
    self.locationManager = [[CLLocationManager alloc] init];
    self.locationManager.delegate = self;
    self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    self.locationManager.distanceFilter = 1.0f;
	// Do any additional setup after loading the view.
    [self montiorLocation];
    
    UITapGestureRecognizer * touch = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(touchView)];
    [self.view addGestureRecognizer:touch];

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

- (void)montiorLocation
{
    [self.locationManager startUpdatingLocation];
}

-(void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation {
    NSLog(@"location latitude is %f longitude is %f",newLocation.coordinate.latitude,newLocation.coordinate.longitude);
    
    
    [self positionSearchClientWithLatitude:[NSString stringWithFormat:@"%f",newLocation.coordinate.latitude] andLongtitude:[NSString stringWithFormat:@"%f",newLocation.coordinate.longitude]];
    
    [self.locationManager stopUpdatingLocation];
}

- (void)positionSearchClientWithLatitude:(NSString *)latitude andLongtitude:(NSString *)longtitude
{
    TravelNetClient *client = [TravelNetClient client];
    void (^handleData)(BOOL succeeded, id responseData) = ^(BOOL succeeded, id responseData){
        if (!succeeded) {
            NSLog(@"Failed!");
        }
        else {
            if ([responseData isKindOfClass:[NSArray class]]) {
                for (NSDictionary * hotelDict in responseData) {
                    [_hotelArray addObject:[self getHotelByDicitionary:hotelDict]];
                }
                NSLog(@"It is a Array");
            }
            [self.tableView reloadData];
        }
    };
    
    NSString * hotelName;
    if (![_nameTextField.text isEqualToString:@""]) {
        hotelName = _nameTextField.text;
    }
    else
        hotelName = @"-1";
    
    [client searchHotelWithhotelName:hotelName latitude:latitude longtitude:longtitude succededCompletion:handleData failedCompletion:nil];
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

#pragma mark - tableView delegate and datasource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [self.hotelArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"SearchHotelCell";
    Hotel * hotel = [_hotelArray objectAtIndex:[indexPath row]];
    PositionSearchTableViewCell *cell = (PositionSearchTableViewCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    [cell.hotelTitleLabel setText:hotel.hotelName];
    [cell.hotelAddressLabel setText:hotel.hotelAddress];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}



- (IBAction)clickBackButton:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)clickFilterButton:(id)sender {
    [_hotelArray removeAllObjects];
    [self montiorLocation];
}

@end
