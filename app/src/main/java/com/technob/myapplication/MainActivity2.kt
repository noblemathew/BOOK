package com.technob.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject

class MainActivity2 : AppCompatActivity() {
    var bookid:String?="100"
    lateinit var bookname:TextView
    lateinit var imageView: ImageView
    lateinit var bookauthor:TextView
    lateinit var bookprice:TextView
    lateinit var bookrating:TextView
    lateinit var txt:TextView
    lateinit var prgressbar:ProgressBar
    lateinit var relativeLayout: RelativeLayout
    lateinit var relativeLayout2: RelativeLayout
    lateinit var toolbar: Toolbar
    lateinit var favbutton:Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"
        favbutton=findViewById(R.id.favoritebutton)
        prgressbar = findViewById(R.id.prog)
        relativeLayout = findViewById(R.id.progress)
        bookname = findViewById(R.id.t1)
        bookauthor = findViewById(R.id.t2)
        bookprice = findViewById(R.id.t3)
        bookrating = findViewById(R.id.t4)
        relativeLayout2=findViewById(R.id.layout2)
        imageView = findViewById(R.id.img1)
        txt = findViewById(R.id.abouttextview)
        relativeLayout.visibility = View.VISIBLE
        prgressbar.visibility = View.VISIBLE

        if (intent != null) {
            bookid = intent.getStringExtra("book_id")
        } else {
            finish()
        }
        if (bookid == "100") {
            Toast.makeText(
                this@MainActivity2,
                "Some unexpected error has occurred due to parsing",
                Toast.LENGTH_SHORT
            ).show()
        }
        val queue = Volley.newRequestQueue(this@MainActivity2)
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookid)
        if (ConnectionManager().checkconnectivity(this@MainActivity2)){
        val jsonRequest =
            object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                try {
                    relativeLayout.visibility = View.GONE
                    val success = it.getBoolean("success")
                    if (success) {
                        val data = it.getJSONObject("book_data")
                        val bookimageurl = data.getString("image")
                        Picasso.get().load(data.getString("image"))
                            .error(R.drawable.default_book_cover).into(imageView)
                        bookname.text = data.getString("name")
                        bookauthor.text = data.getString("author")
                        bookprice.text = data.getString("price")
                        bookrating.text = data.getString("rating")
                        txt.text = data.getString("description")

                        val bookEntity = BookEntity(
                            bookid?.toInt() as Int,
                            bookname.text.toString(),
                            bookauthor.text.toString(),
                            bookprice.text.toString(),
                            bookrating.text.toString(),
                            txt.text.toString(),
                            bookimageurl
                        )
                        val checkfav=DBasync(applicationContext,bookEntity,1).execute()
                        val isFav=checkfav.get()
                        if(isFav){
                            favbutton.text="REMOVE FROM FAVOURITES"
                            val favcolor=ContextCompat.getColor(applicationContext,R.color.favbutton)
                            favbutton.setBackgroundColor(favcolor)

                        }
                        else{
                            favbutton.text="ADD TO FAVOURITES"
                            val favcolor=ContextCompat.getColor(applicationContext,R.color.rescolor)
                            favbutton.setBackgroundColor(favcolor)
                        }
                        favbutton.setOnClickListener {
                            if (!DBasync(applicationContext,bookEntity,1).execute().get()) {
                                val async = DBasync(applicationContext, bookEntity, 2).execute()
                                val result = async.get()
                                if (result) {
                                    Toast.makeText(
                                        this@MainActivity2,
                                        "Book Added to Favourites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    favbutton.text = "REMOVE FROM FAVOURITES"
                                    val favcolor = ContextCompat.getColor(
                                        applicationContext,
                                        R.color.favbutton)
                                    favbutton.setBackgroundColor(favcolor)
                                }
                            else{
                                Toast.makeText(this@MainActivity2,"Some Error Occurred1",Toast.LENGTH_SHORT).show()

                            }
                            }else{
                                val async = DBasync(applicationContext, bookEntity, 3).execute()
                                val result = async.get()
                                if (!result) {
                                    Toast.makeText(
                                        this@MainActivity2,
                                        "Book Removed From Favourites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    favbutton.text = "ADD TO FAVOURITES"
                                    val favcolor=ContextCompat.getColor(applicationContext,R.color.rescolor)
                                    favbutton.setBackgroundColor(favcolor)
                                }
                                else{
                                    Toast.makeText(this@MainActivity2,"Some Error Occurred2",Toast.LENGTH_SHORT).show()

                                }


                            }
                        }
                    }
                    else {
                        Toast.makeText(
                            this@MainActivity2,
                            "Oops..Some Error Occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity2,
                        "Oops..Some Error Occurred ",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }, Response.ErrorListener {
                Toast.makeText(
                    this@MainActivity2,
                    "Volley Error",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Context-type"] = "application/json"
                    headers["token"] = "e61f01ffc640e6"
                    return headers
                }
            }
        queue.add(jsonRequest)
    }
        else{
            val dialog= AlertDialog.Builder(this@MainActivity2)
            dialog.setTitle("FAILED")
            dialog.setMessage("OOPS...INTERNET CONNECTION NOT FOUND")
            dialog.setPositiveButton("OPEN SETTINGS"){text,listener->
                val settingintent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingintent)
                finish()
            }
            dialog.setNegativeButton("EXIT"){text,listener->
                ActivityCompat.finishAffinity(this@MainActivity2)
            }
            dialog.create()
            dialog.show()
        }

    }
    class DBasync(val context: Context,val bookEntity: BookEntity,val mode:Int):AsyncTask<Void,Void,Boolean>(){
        override fun doInBackground(vararg p0: Void?): Boolean {
            val dB=Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
            when(mode){
                1->{
                    val book:BookEntity?=dB.bookDao().getbookbyId(bookEntity.book_id.toString())
                    dB.close()
                    return book!=null
                }
                2->{
                    dB.bookDao().insertBook(bookEntity)
                    dB.close()
                    return true
                }
                3->{dB.bookDao().deleteBook(bookEntity)
                dB.close()
                return false}
            }
            return true
        }

    }
}