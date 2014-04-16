//
//  ViewController.h
//  TravelingApp
//
//  Created by Emerson on 14-4-9.
//  Copyright (c) 2014年 Emerson. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol MainViewControllerDelegate <NSObject>

@required

- (void)didFectchHotelDataWithArray:(NSMutableArray *)hotelsArray;

@end


@interface MainViewController : UIViewController<NSURLSessionDelegate>
@property (weak, nonatomic) IBOutlet UITextField *cityNameTextField;
@property (weak, nonatomic) IBOutlet UITextField *regionTextField;
@property (weak, nonatomic) IBOutlet UITextField *hotelTextField;
@property (weak, nonatomic) IBOutlet UITextField *ratingTextField;
@property (weak, nonatomic) IBOutlet UITextField *keywordTextField;
@property (weak, nonatomic) IBOutlet UITextField *limitTextField;
@property (weak, nonatomic) IBOutlet UIButton *searchButton;
@property (weak, nonatomic) id<MainViewControllerDelegate> mainViewDelegate;

@end
