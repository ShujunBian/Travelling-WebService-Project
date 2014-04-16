//
//  HotelDetailViewController.m
//  TravelingApp
//
//  Created by Emerson on 14-4-10.
//  Copyright (c) 2014年 Emerson. All rights reserved.
//

#import "HotelDetailViewController.h"
#import "TravelNetClient.h"
#import "HotelDetail.h"
#import "UIImageView+Addition.h"
#import "NSString+Encrypt.h"

@interface HotelDetailViewController ()

//@property (nonatomic, strong) NSMutableArray * _hotel
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

- (IBAction)clickBackButton:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)viewWillAppear:(BOOL)animated
{
    TravelNetClient *client = [TravelNetClient client];
    void (^handleData)(BOOL succeeded, id responseData) = ^(BOOL succeeded, id responseData){
        if (!succeeded) {
            NSLog(@"Failed!");
        }
        else {
            if ([responseData isKindOfClass:[NSDictionary class]]) {
                NSLog(@"It is a dicitionary");
                
                if ([responseData objectForKey:@"hotelName"] != [NSNull null]) {
                    _hotelDetail.hotelName = [[responseData objectForKey:@"hotelName"]replaceUTF8];
                }
                if ([responseData objectForKey:@"description"] != [NSNull null]) {
                    _hotelDetail.hotelDecrpition = [[responseData objectForKey:@"description"]replaceUTF8];
                }
                if ([responseData objectForKey:@"pictureURL"] != [NSNull null]) {
                    _hotelDetail.imageUrl = [[responseData objectForKey:@"pictureURL"]replaceUTF8];
                }
                
                [self resetViewByData];
            }
            if ([responseData isKindOfClass:[NSArray class]]) {
                NSLog(@"It is a Array");
            }
        }
    };
    [client searchHotelDetailWith:_hotelDetail.hotelId APIType:_hotelDetail.hotelApiType succededCompletion:handleData failedCompletion:nil];
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

- (void)resetViewByData
{
    [_hotelTitleLabel setText:_hotelDetail.hotelName];
    [_hotelDetailTextView setText:_hotelDetail.hotelDecrpition];
    [_hotelImageView loadImageFromURL:_hotelDetail.imageUrl completion:^(BOOL succeed){
        [self fadeInWithView:_hotelImageView WithDuration:0.3 completion:nil];
    }];
}

- (void)fadeInWithView:(UIView *) view
          WithDuration:(float)duration
            completion:(void (^)(void))completion
{
    view.alpha = 0;
    [UIView animateWithDuration:duration delay:0 options:UIViewAnimationOptionCurveEaseInOut animations:^{
        view.alpha = 1;
    } completion:^(BOOL finished) {
        if(completion)
            completion();
    }];
}

@end
