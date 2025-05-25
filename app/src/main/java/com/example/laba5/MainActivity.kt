package com.example.laba5

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laba5.adapt.MyAdapterHint
import com.example.laba5.adapt.MyAdapterResult
import com.example.laba5.db.AppDatabase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    var onSearch: Boolean = false
    var currentDep: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        val userManager = UserManager(this)
        var onLaunch = true
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //val database = AppDatabase.getInstance(application).airportDao()
        setContentView(R.layout.activity_main)
        viewModel.getAirport("f")
        val edit = findViewById<EditText>(R.id.SearchInput)
        val adaptHint = MyAdapterHint(emptyList(), viewModel)
        val recycleHint = findViewById<RecyclerView>(R.id.hint)
        recycleHint.layoutManager = LinearLayoutManager(this)
        recycleHint.adapter = adaptHint

        val adaptRes = MyAdapterResult(emptyList(), viewModel)
        val recycleRes = findViewById<RecyclerView>(R.id.result)
        recycleRes.layoutManager = LinearLayoutManager(this)
        recycleRes.adapter = adaptRes
        userManager.userInputFlow.asLiveData().observe(this) {
            if (onLaunch) {
                edit.setText(it)
                onLaunch = false
            }
        }
        viewModel.routes.observe(this) { rotesList ->
            if (rotesList.isNotEmpty()) {
                currentDep = rotesList[0].from_code
                edit.setText(currentDep)
                recycleHint.visibility = View.GONE
                recycleRes.visibility = View.VISIBLE
                Log.d("MyTag", "is GONE")
            }
            adaptRes.updateData(rotesList)
        }

        viewModel.favorite.observe(this) { favList ->
            if (favList.isNotEmpty()) {
                recycleHint.visibility = View.GONE
                recycleRes.visibility = View.VISIBLE
                Log.d("MyTag", "is favorite")
            }
            if(!onSearch)
                adaptRes.updateData(favList)
        }

        viewModel.airport.observe(this) { airportList ->
            Log.d("MyTag", "isChange hint")
            if (onSearch)
                adaptHint.updateData(airportList)
        }
        adaptHint.updateData(emptyList())
        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //resultName.text=p0.toString()
                onSearch = true
                if (p0 != null) {
                    if (p0.length > 0)
                        viewModel.getAirport(p0.toString())
                    else {
                        onSearch = false
                        adaptHint.updateData(emptyList())
                        Log.d("MyTag_Main","get favorite")
                        viewModel.getFavorite()
                    }
                    lifecycleScope.launch {
                        userManager.storeUserImput(p0.toString())
                    }

                }
                if (p0.toString() != currentDep) {
                    recycleHint.visibility = View.VISIBLE
                    recycleRes.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                //resultName.text=p0.toString()
                onSearch = true
                if (p0 != null) {
                    if (p0.length > 0)
                        viewModel.getAirport(p0.toString())
                    else {
                        onSearch = false
                        adaptHint.updateData(emptyList())
                    }
                }

            }

        })
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}