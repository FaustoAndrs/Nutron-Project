package com.lazysyntax.nutron

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

//<key>NSCameraUsageDescription</key>
//<string>Camera access is required for barcode scanning</string>