//
//  SearchResultTableViewCell.h
//  TravelingApp
//
//  Created by Emerson on 14-4-15.
//  Copyright (c) 2014年 Emerson. All rights reserved.
//

#import <UIKit/UIKit.h>
@class Hotel;
@interface SearchResultTableViewCell : UITableViewCell

@property (nonatomic, strong) Hotel * hotel;
@property (weak, nonatomic) IBOutlet UILabel *hotelNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *hotelAddressLabel;

@end
