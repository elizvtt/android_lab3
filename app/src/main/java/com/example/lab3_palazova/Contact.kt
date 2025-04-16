package com.example.lab3_palazova

import android.net.Uri

data class Contact(
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var imageUri: Uri? = null
)