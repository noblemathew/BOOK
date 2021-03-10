package com.technob.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var recycler:RecyclerView
    lateinit var button:Button
    lateinit var linlayout:LinearLayoutManager
    lateinit var recycleadapter:DashboardAdapter
    lateinit var progressbar:ProgressBar
    lateinit var relativeLayout: RelativeLayout
    val bookInfoList = arrayListOf<Book>()
    var ratingcomparator= Comparator<Book>{book1,book2 ->
        if (book1.bookrating.compareTo(book2.bookrating,true)==0){
            book1.bookname.compareTo(book2.bookname,true)
        }
        else{
            book1.bookrating.compareTo(book2.bookrating,true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view=inflater.inflate(R.layout.fragment_dashboard, container, false)
        progressbar=view.findViewById(R.id.prog)
        relativeLayout=view.findViewById(R.id.progress)
        relativeLayout.visibility=View.VISIBLE
        recycler=view.findViewById(R.id.recycler)
        linlayout= LinearLayoutManager(activity)
        val queue=Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v1/book/fetch_books/"
        if (ConnectionManager().checkconnectivity(activity as Context)){
            val jsonObjectRequest=object :JsonObjectRequest(Request.Method.GET,url,null, Response.Listener{
                try {
                    relativeLayout.visibility=View.GONE
                    val success = it.getBoolean("success")
                    if (success) {
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {

                            val bookJson = data.getJSONObject(i)
                            val bookobject = Book(
                                bookJson.getString("book_id"),
                                bookJson.getString("name"),
                                bookJson.getString("author"),
                                bookJson.getString("rating"),
                                bookJson.getString("price"),
                                bookJson.getString("image")

                            )
                            bookInfoList.add(bookobject)
                            recycleadapter = DashboardAdapter(activity as Context, bookInfoList)
                            recycler.adapter = recycleadapter
                            recycler.layoutManager = linlayout


                        }
                    } else {
                        Toast.makeText(
                            activity as Context,
                            "Oops..Some Error Occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }catch (e:JSONException){
                    Toast.makeText(activity as Context,"Some unexpected error occurred",Toast.LENGTH_SHORT).show()
                }
            },Response.ErrorListener {
                if (activity!=null){
                Toast.makeText(activity as Context,"Volley Error Occurred",Toast.LENGTH_SHORT).show()}}){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Context-type"]="application/json"
                    headers["token"]="e61f01ffc640e6"
                    return headers
                }

            }
            queue.add(jsonObjectRequest)
        }else{
            val dialog=AlertDialog.Builder(activity as Context)
            dialog.setTitle("FAILED")
            dialog.setMessage("OOPS...INTERNET CONNECTION NOT FOUND")
            dialog.setPositiveButton("OPEN SETTINGS"){text,listener->
                val settingintent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingintent)
                activity?.finish()
            }
            dialog.setNegativeButton("EXIT"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item?.itemId
        if (id==R.id.action_sort){
            Collections.sort(bookInfoList,ratingcomparator)
            bookInfoList.reverse()
            recycleadapter.notifyDataSetChanged()

        }
        return super.onOptionsItemSelected(item)
    }
}