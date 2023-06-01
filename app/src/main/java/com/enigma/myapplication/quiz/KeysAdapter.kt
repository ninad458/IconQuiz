package com.enigma.myapplication.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.enigma.myapplication.R
import com.enigma.myapplication.quiz.KeysAdapter.ButtonViewHolder

class KeysAdapter(
    private val keyClickListener: KeyClickListener
) : RecyclerView.Adapter<ButtonViewHolder>() {

    private var buttonList: List<KeyModel> = mutableListOf()

    // ViewHolder class
    class ButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonItem: Button = view.findViewById(R.id.btn_key)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_button, parent, false)
        return ButtonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val button = buttonList[position]
        holder.buttonItem.text = button.text.toString()
        holder.buttonItem.isEnabled = !button.disabled

        // Handle button click event
        holder.buttonItem.setOnClickListener {
            // Perform action when the button is clicked
            // You can access the button ID using button.getId()
            keyClickListener.addEntry(button)
        }
    }

    override fun getItemCount(): Int {
        return buttonList.size
    }

    fun setData(map: List<KeyModel>) {
        buttonList = map
        notifyDataSetChanged()
    }
}