package com.technob.myapplication

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.dashboardlayout.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FavoriteFragment : Fragment() {

    lateinit var progressBar: ProgressBar
    lateinit var progressLayout:RelativeLayout
    lateinit var tt1:TextView
    lateinit var recyclerfav:RecyclerView
    lateinit var layoutmanager: RecyclerView.LayoutManager
    lateinit var adapter: FavoriteAdapter
    var dBbooklist= listOf<BookEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        tt1=view.findViewById(R.id.tt1)
        recyclerfav=view.findViewById(R.id.recyclerfav)
        progressBar=view.findViewById(R.id.prog)
        progressLayout=view.findViewById(R.id.progress)

        layoutmanager=GridLayoutManager(activity as Context,2)
        dBbooklist=Retrievefav(activity as Context).execute().get()
        if (dBbooklist==null){
            tt1.visibility=View.VISIBLE
        }
        else if (activity!=null){
            tt1.visibility=View.GONE
            progressLayout.visibility=View.GONE
            adapter= FavoriteAdapter(activity as Context,dBbooklist)
            recyclerfav.adapter=adapter
            recyclerfav.layoutManager=layoutmanager

        }
        else{
            tt1.visibility=View.GONE
        }
        return view

    }
    class Retrievefav(val context :Context):AsyncTask<Void,Void,List<BookEntity>>(){
        override fun doInBackground(vararg p0: Void?): List<BookEntity> {
         val dB=Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
            return dB.bookDao().getallBooks()
        }

    }
}

