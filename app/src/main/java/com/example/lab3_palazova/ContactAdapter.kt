package com.example.lab3_palazova

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3_palazova.databinding.ListItemBinding
import io.getstream.avatarview.coil.loadImage


class ContactAdapter(private val contactList: MutableList<Contact>, private val updateUI: () -> Unit) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    // Метод видалення контакту
    fun removeContact(position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            contactList.removeAt(position) // Видаляємо зі списку
            notifyItemRemoved(position) // Повідомляємо адаптер
            updateUI() // оновлюємо ui
        }
    }

    // Створюємо новий ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // При натисканні на хрестик видаляємо елемент списку
        return ContactViewHolder(binding) { position ->
            removeContact(position)
        }
    }

    // Прив'язуємо дані до ViewHolder
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contactList[position])
    }

    override fun getItemCount(): Int = contactList.size

    // ViewHolder для одного елемента списку
    class ContactViewHolder(private val binding: ListItemBinding, private val onDeleteClick: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.contactName.text = contact.name
            binding.contactEmail.text = contact.email
            binding.contactPhone.text = "+380${contact.phone}" // contact.phone

            if (contact.imageUri != null) {
                binding.contactPhoto.loadImage(contact.imageUri)
            } else {
                binding.contactPhoto.loadImage(R.drawable.img_android)
            }

            // При натисканні на хрестик видаляємо елемент списку
            binding.imageButton.setOnClickListener {
                onDeleteClick(adapterPosition)
                Toast.makeText(binding.root.context, "Контакт видалено", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
