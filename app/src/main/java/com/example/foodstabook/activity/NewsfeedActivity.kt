package com.example.foodstabook.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodstabook.R
import com.example.foodstabook.databinding.ActivityNewsfeedBinding
import com.example.foodstabook.model.NewsfeedAdapter
import com.example.foodstabook.model.PostModel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class NewsfeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsfeedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsfeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val imageList = listOf<Int>(R.drawable.borscht, R.drawable.blueberry_crumble, R.drawable.bunny_chow,
        R.drawable.falafel, R.drawable.chicken_tikka_masala, R.drawable.ochazuke, R.drawable.shrimp_al_mojo_de_ajo,
        R.drawable.kimchi_jjigae, R.drawable.vegetable_terrine, R.drawable.ratatouille)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        val dummyData = ArrayList<PostModel>()

        val commentList = ArrayList<String>()

        commentList.add("Wow!")
        commentList.add("So tasty!")
        commentList.add("Not my cup of tea...")

        for (i in 1..10) {
            dummyData.add(PostModel("0", "testing123", imageList[i-1], SimpleDateFormat("26-01-2022"),
                "Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah",
                100+i, commentList))
        }


        // This will pass the ArrayList to our Adapter
        val adapter = NewsfeedAdapter(dummyData)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
    }
}