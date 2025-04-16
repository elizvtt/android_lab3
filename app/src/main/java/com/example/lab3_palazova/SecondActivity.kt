package com.example.lab3_palazova

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.lab3_palazova.databinding.ActivitySecondBinding
import java.io.File

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var contact: Contact

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        // Якщо фото зроблено показуємо його, якщо ні - стандартна картинка
        if (isSuccess && contact.imageUri != null) {
            binding.imageView2.setImageURI(contact.imageUri)
        } else {
            binding.imageView2.setImageResource(R.drawable.img2)
            contact.imageUri = null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contact = Contact() // Створюємо об'єкт контакту

        // Обробник натискання кнопки "Take Photo"
        binding.buttonTakePhoto.setOnClickListener {
            val photoFile = File(getExternalFilesDir(null), "contact_photo_${System.currentTimeMillis()}.jpg")
            val photoUri = FileProvider.getUriForFile(this,"com.example.lab3_palazova.fileprovider",photoFile)
            contact.imageUri = photoUri
            takePhotoLauncher.launch(photoUri)
        }

        // Обробник натискання кнопка "Cancel"
        binding.buttonCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        // Обробник натискання кнопки "Add"
        binding.buttonAdd.setOnClickListener {
            val name = binding.inputEditTextName.text.toString()
            val email = binding.inputEditTextEmail.text.toString()
            val phone = binding.inputEditTextNumber.text.toString()

            // Регулярні вирази для пошти та номеру
            val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
            val phoneRegex = "^\\d{9}$".toRegex()

            when {
                name.isEmpty() || email.isEmpty() || phone.isEmpty() -> {
                    Toast.makeText(this, "Заповніть усі поля", Toast.LENGTH_SHORT).show()
                }
                !email.matches(emailRegex) -> {
                    Toast.makeText(this, "Невірний формат email", Toast.LENGTH_SHORT).show()
                }
                !phone.matches(phoneRegex) -> {
                    Toast.makeText(this, "Невірний формат номеру", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    contact.name = name
                    contact.email = email
                    contact.phone = phone

                    // Повертаємо результат у MainActivity
                    val resultIntent = Intent().apply {
                        putExtra("CONTACT_NAME", contact.name)
                        putExtra("CONTACT_EMAIL", contact.email)
                        putExtra("CONTACT_PHONE", contact.phone)
                        contact.imageUri?.let { uri ->
                            putExtra("CONTACT_PHOTO_URI", uri.toString())
                        }
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                    Toast.makeText(this, "Контакт створено", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Контракт для запуску та обробки результату
    class Contract : ActivityResultContract<Unit, Contact?>() {
        // Інтент для запуску другої активності
        override fun createIntent(context: Context, input: Unit): Intent {
            return Intent(context, SecondActivity::class.java)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Contact? {
            return if (resultCode == Activity.RESULT_OK) {
                // Зберігаємо дані з інтенту
                val name = intent?.getStringExtra(EXTRA_NAME) ?: ""
                val email = intent?.getStringExtra(EXTRA_EMAIL) ?: ""
                val phone = intent?.getStringExtra(EXTRA_PHONE) ?: ""
                val photoUri = intent?.getStringExtra(EXTRA_PHOTO_URI)?.let { Uri.parse(it) }

                Contact(name, email, phone, photoUri) // Створюємо об'єкт контакту
            } else {
                null
            }
        }

        companion object {
            const val EXTRA_NAME = "CONTACT_NAME"
            const val EXTRA_EMAIL = "CONTACT_EMAIL"
            const val EXTRA_PHONE = "CONTACT_PHONE"
            const val EXTRA_PHOTO_URI = "CONTACT_PHOTO_URI"
        }
    }
}



