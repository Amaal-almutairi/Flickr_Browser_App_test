package com.example.flickr_browser_app_test

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var IMAGE: ArrayList<imge>
    lateinit var myRV: RecyclerView
    lateinit var myAdap: RVAdap
    lateinit var imgev: ImageView
    lateinit var edimage: EditText
    lateinit var btnSearch: Button
    lateinit var mainid: ConstraintLayout
    lateinit var lin2:LinearLayout
    var edSearch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myRV = findViewById(R.id.rv)
        imgev = findViewById(R.id.imgv)
        edimage = findViewById(R.id.edimage)
        btnSearch = findViewById(R.id.btnsearch)
        IMAGE = arrayListOf()
        myAdap = RVAdap(this, IMAGE)
        mainid = findViewById(R.id.mainid)
        lin2 = findViewById(R.id.lin2)
        myRV.adapter = myAdap
        myRV.layoutManager = LinearLayoutManager(this)

        btnSearch.setOnClickListener {
            edSearch = edimage.text.toString()
            if (edSearch.isNotEmpty()) {
                getAPI()
            } else {
                Toast.makeText(this, "Search Field is Empty", Toast.LENGTH_SHORT).show()
            }
        }

        imgev.setOnClickListener {
            closeItem()
        }
    }

    suspend fun getItems(): String {
        var response = ""
        try {
            response =
                URL("https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=d747699e85ede014040367944595fffe&tags=${edSearch}&per_page=15&sort=relevance&format=json&nojsoncallback=1")
                    .readText(Charsets.UTF_8)
        } catch (e: Exception) {
            println("Error:$e")
        }
        return response
    }

    fun getAPI() {
        CoroutineScope(IO).launch {
            val data = async { getItems() }.await()

            if (data.isNotEmpty()) {
                Log.d("Date", "$data")
                println(data)
                displayphoto(data)
            } else
                Toast.makeText(this@MainActivity, "No Images Found ", Toast.LENGTH_SHORT).show()

        }
    }

    suspend fun displayphoto(data: String) {
        withContext(Main) {
            val jsonObj = JSONObject(data)
            val image = jsonObj.getJSONObject("photos").getJSONArray("photo")
            Log.d("image", "$image")
            println(image.getJSONObject(0))
            println(image.getJSONObject(0).getString("farm"))
            for (i in 0 until image.length()) {
                val title = image.getJSONObject(i).getString("title")
                val farmID = image.getJSONObject(i).getString("farm")
                val serverID = image.getJSONObject(i).getString("server")
                val id = image.getJSONObject(i).getString("id")
                val secret = image.getJSONObject(i).getString("secret")
                val imagelink = "https://live.staticflickr.com/${serverID}/${id}_${secret}_t.png"
                IMAGE.add(imge(title, imagelink))
            }
            Log.d("ImageV2", "displayphoto: $IMAGE")
            myAdap.notifyDataSetChanged()
        }
    }

    fun openItem(link: String) {
        Glide.with(this).load(link).into(imgev)
        myRV.isVisible = false
        lin2.isVisible = false
        imgev.isVisible = true
    }

    fun closeItem() {
        myRV.isVisible = true
        lin2.isVisible = true
        imgev.isVisible = false
    }


}



