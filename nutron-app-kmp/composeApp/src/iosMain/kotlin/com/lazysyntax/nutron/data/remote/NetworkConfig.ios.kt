package com.lazysyntax.nutron.data.remote

actual val BASE_HOST: String
    // get() = "localhost" //vm
    // get() = "192.168.1.38" //En Windows con red Wifi
    // get() = "192.168.1.45" //En Mac con red Wifi
    get() = "172.20.10.2" // En Mac con punto de acceso IPhone
