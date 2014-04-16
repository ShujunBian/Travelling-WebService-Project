//
//  PostionSearchViewController.h
//  TravelingApp
//
//  Created by Emerson on 14-4-9.
//  Copyright (c) 2014年 Emerson. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PostionSearchViewController : UIViewController

@property (weak, nonatomic) IBOutlet UITextField *ratingTextField;
@property (weak, nonatomic) IBOutlet UITextField *nameTextField;
@property (weak, nonatomic) IBOutlet UIButton *filterButton;
@property (weak, nonatomic) IBOutlet UIButton *backButton;
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@end
