//
//  SearchResultViewController.m
//  TravelingApp
//
//  Created by Emerson on 14-4-9.
//  Copyright (c) 2014年 Emerson. All rights reserved.
//

#import "SearchResultViewController.h"
#import "NSNotificationCenter+Addition.h"
#import "SearchResultTableViewCell.h"
#import "HotelDetailViewController.h"
#import "Hotel.h"
#import "HotelDetail.h"

@interface SearchResultViewController ()

@end

@implementation SearchResultViewController

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
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)clickBackButton:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - tableView delegate and datasource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [self.hotelsArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"SearchHotelCell";
    Hotel * hotel = [_hotelsArray objectAtIndex:[indexPath row]];
    SearchResultTableViewCell *cell = (SearchResultTableViewCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    cell.hotel = hotel;
    [cell.hotelNameLabel setText:hotel.hotelName];
    [cell.hotelAddressLabel setText:hotel.hotelAddress];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

#pragma mark - MainViewDelegate
- (void)didFectchHotelDataWithArray:(NSMutableArray *)hotelsArray
{
    [self.hotelsArray removeAllObjects];
    self.hotelsArray = hotelsArray;
    [self.tableView reloadData];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier]isEqualToString:@"hotelDetail"]) {
        SearchResultTableViewCell *cell = (SearchResultTableViewCell *)sender;
        HotelDetailViewController * detailViewController = (HotelDetailViewController *)[segue destinationViewController];
        Hotel * tempHotel = cell.hotel;
        detailViewController.hotelDetail = [[HotelDetail alloc]init];
        [detailViewController.hotelDetail setHotelApiType:tempHotel.searchingType];
        switch (tempHotel.searchingType) {
            case 0: {
                NSLog(@"CTrip id is %@",tempHotel.hotelCtripId);
                [detailViewController.hotelDetail setHotelId:tempHotel.hotelCtripId];
                break;
            }
            case 1: {
                NSLog(@"Hotwire id is %@",tempHotel.hotelHotwireId);
                [detailViewController.hotelDetail setHotelId:tempHotel.hotelHotwireId];
                break;
            }
            case 2: {
                NSLog(@"DZDP id is %@",tempHotel.hotelDZDPId);
                [detailViewController.hotelDetail setHotelId:tempHotel.hotelDZDPId];
                break;
            }
            case 3: {
                NSLog(@"TC id is %@",tempHotel.hotelTCId);
                [detailViewController.hotelDetail setHotelId:tempHotel.hotelTCId];
                break;
            }
            default:
                break;
        }
    }
}
@end
