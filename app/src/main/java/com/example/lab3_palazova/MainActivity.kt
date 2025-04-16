package com.example.lab3_palazova

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.lab3_palazova.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contactAdapter: ContactAdapter
    private val contactList = mutableListOf<Contact>()

    private val createContactLauncher = registerForActivityResult(SecondActivity.Contract()) { contact ->
        contact?.let {
            contactList.add(it)  // Додаємо контакт у список
            contactAdapter.notifyItemInserted(contactList.size - 1)  // Додаємо контакт в адаптер
            updateUI()  // Оновлюємо ui
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ініціалізація RecyclerView
        contactAdapter = ContactAdapter(contactList, ::updateUI)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = contactAdapter

        // Обробник натискання на кнопку
        binding.button.setOnClickListener {
            createContactLauncher.launch(Unit)
        }
        updateUI() // Оновлюємо ui
    }

    // Скриваємо картинку та текст ящко додається контакт
    private fun updateUI() {
        if (contactList.isEmpty()) {
            binding.emptyText.visibility = View.VISIBLE
            binding.emptyImage.visibility = View.VISIBLE
        } else {
            binding.emptyText.visibility = View.GONE
            binding.emptyImage.visibility = View.GONE
        }
    }
}

