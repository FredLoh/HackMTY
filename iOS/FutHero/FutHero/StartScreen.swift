//
//  ViewController.swift
//  FutHero
//
//  Created by Frederik Lohner on 20/Feb/16.
//  Copyright © 2016 HackMTY. All rights reserved.
//

import UIKit
import Foundation
import SnapKit
class StartScreen: UIViewController {
    var button = UIButton()
    var texto = UILabel()
    
    override func viewDidLoad() {
        self.title = "Settings"
        self.view.backgroundColor = UIColor.whiteColor()
        self.view.addSubview(button)
        self.view.addSubview(texto)
        texto.text = "Buscar cancha"
        button.snp_makeConstraints { (make) -> Void in
            make.center.equalTo(self.view)
        }
        texto.snp_makeConstraints { (make) -> Void in
            make.centerX.equalTo(self.view)
            make.top.equalTo(self.view).offset(70)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

