//
//  MainNavigationController.swift
//  FutHero
//
//  Created by Frederik Lohner on 20/Feb/16.
//  Copyright © 2016 HackMTY. All rights reserved.
//

import UIKit

class MainNavigationController: UINavigationController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let mainViewController = StartScreen()
        self.setViewControllers([mainViewController], animated: false)
        self.navigationBar.tintColor = UIColor.grayColor()
    }
    
    override func preferredStatusBarStyle() -> UIStatusBarStyle {
        return .Default
    }
    
}