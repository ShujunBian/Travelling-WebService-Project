//
//  HotelDetailViewController.h
//  TravelingApp
//
//  Created by Emerson on 14-4-10.
//  Copyright (c) 2014年 Emerson. All rights reserved.
//

#import <UIKit/UIKit.h>

@class HotelDetail;
@interface HotelDetailViewController : UIViewController

@property (strong, nonatomic) HotelDetail * hotelDetail;
@property (weak, nonatomic) IBOutlet UITextView *hotelDetailTextView;
@property (weak, nonatomic) IBOutlet UILabel *hotelTitleLabel;
@property (weak, nonatomic) IBOutlet UIImageView *hotelImageView;

@end
