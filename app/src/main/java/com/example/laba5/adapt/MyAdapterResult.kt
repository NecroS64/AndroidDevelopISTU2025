package com.example.laba5.adapt

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laba5.MainViewModel
import com.example.laba5.R
import com.example.laba5.db.Airport
import com.example.laba5.db.Favorite
import com.example.laba5.db.FlightCombination
import kotlin.random.Random

class MyAdapterResult(var data: List<FlightCombination>, private val viewModel: MainViewModel) : RecyclerView.Adapter<MyAdapterResult.MyViewHolder>() {


    private var expandedPosition: Int? = null

    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        //val bodyView = row.findViewById<LinearLayout>(R.id.bodyHint)
        val iataDep = row.findViewById<TextView>(R.id.iata_codeDep)
        val iataArr = row.findViewById<TextView>(R.id.iata_codeArr)
        val nameDepView = row.findViewById<TextView>(R.id.nameDep)
        val nameArrView = row.findViewById<TextView>(R.id.namArre)
        val star = row.findViewById<ImageView>(R.id.imageFav)
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.result, parent, false)
        return MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (data.isNotEmpty()) {
            val actualPosition = holder.adapterPosition

            // Анимация расширения или сворачивания

            // Заполнение данных
            val hint = data[position]
            holder.iataDep.text=hint.from_code
            holder.iataArr.text=hint.to_code
            //Log.d("MyTag_HintAdapt", hint.to_code)
            holder.nameDepView.text = hint.from_name
            holder.nameArrView.text = hint.to_name
            if(hint.isFavorite)
            {
                holder.star.setImageResource(R.drawable.starfav)
            }
            else holder.star.setImageResource(R.drawable.star)
            holder.star.setOnClickListener{
                Log.d("MyTag_ResAdapt",hint.to_code)

                if(hint.isFavorite)
                {
                    Log.d("MyTag_ResAdapt","transform to not favorite")
                    holder.star.setImageResource(R.drawable.star)
                    hint.isFavorite=false
                    viewModel.DeleteFavorite(Favorite(
                        departureCode = hint.from_code,
                        destinationCode = hint.to_code
                    ))
                    viewModel.getFavorite()
                }
                else {
                    holder.star.setImageResource(R.drawable.starfav)
                    Log.d("MyTag_ResAdapt","transform to favorite")
                    hint.isFavorite=true
                    viewModel.InsertFavorite(Favorite(
                        departureCode = hint.from_code,
                        destinationCode = hint.to_code
                    ))
                    viewModel.getFavorite()
                }


            }
        }
    }



    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<FlightCombination>) {
//        Log.d("MyTag_ResAdapt","isChange hint")
        Log.d("MyTag_ResAdapt","${data.size.toString()}")
        data = newData
        notifyDataSetChanged() // Для оптимизации используйте DiffUtil
    }
}