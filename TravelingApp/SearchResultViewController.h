//
//  SearchResultViewController.h
//  TravelingApp
//
//  Created by Emerson on 14-4-9.
//  Copyright (c) 2014年 Emerson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainViewController.h"
@interface SearchResultViewController : UIViewController<MainViewControllerDelegate>

@property (weak, nonatomic) IBOutlet UIButton *backButton;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSMutableArray * hotelsArray;

@end
