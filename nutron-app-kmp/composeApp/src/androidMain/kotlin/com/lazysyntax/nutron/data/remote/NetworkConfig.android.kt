package com.lazysyntax.nutron.data.remote

actual val BASE_HOST: String
    //get() = "localhost"
    //get() = "10.0.2.2" //VM
    //get() = "192.168.1.38" // En Windows con red wifi
    //get() = "192.168.1.45" //En Mac con red Wifi
    get() = "172.20.10.2" //Mac red iphone