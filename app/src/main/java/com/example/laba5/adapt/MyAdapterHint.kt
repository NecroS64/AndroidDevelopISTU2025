package com.example.laba5.adapt

import com.example.laba5.MainViewModel
import com.example.laba5.db.Airport


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laba5.MainActivity
import com.example.laba5.R

class MyAdapterHint(var data: List<Airport>, private val viewModel: MainViewModel) : RecyclerView.Adapter<MyAdapterHint.MyViewHolder>() {


    private var expandedPosition: Int? = null

    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
                val bodyView = row.findViewById<LinearLayout>(R.id.bodyHint)
                val iata = row.findViewById<TextView>(R.id.iata_code)
                val nameView = row.findViewById<TextView>(R.id.name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.hint, parent, false)
        return MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (data.isNotEmpty()) {
            val actualPosition = holder.adapterPosition

            // Анимация расширения или сворачивания

            // Заполнение данных
            val hint = data[position]
            holder.iata.text=hint.iata_code
                //Log.d("MyTag_HintAdapt", hint.iata_code)
            holder.nameView.text = hint.name
            holder.bodyView.setOnClickListener{
                Log.d("MyTag_HintAdapt",hint.iata_code)
                viewModel.getDestination(hint.iata_code)

            }
        }
    }



    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Airport>) {
        Log.d("MyTag_HintAdapt","isChange hint")
        Log.d("MyTag_HintAdapt","${data.size.toString()}")
        data = newData
        notifyDataSetChanged() // Для оптимизации используйте DiffUtil
    }
}