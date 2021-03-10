package com.technob.myapplication
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class DashboardAdapter(
    val context: Context,

    val itemlist: ArrayList<Book>
):RecyclerView.Adapter<DashboardAdapter.Dashboard>(){



    class Dashboard(view: View):RecyclerView.ViewHolder(view){


        val txtbookname:TextView=view.findViewById(R.id.t1)
        val txtbookauthor:TextView=view.findViewById(R.id.t2)
        val txtbookprice:TextView=view.findViewById(R.id.t3)
        val txtrating:TextView=view.findViewById(R.id.t5)
        val bookimage:ImageView=view.findViewById(R.id.img1)
        val lin1:RelativeLayout=view.findViewById(R.id.layout1)
        val txt6:TextView=view.findViewById(R.id.t6)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Dashboard {
        val  view=LayoutInflater.from(parent.context).inflate(R.layout.dashboardlayout,parent,false)
        return Dashboard(view)
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    override fun onBindViewHolder(holder: Dashboard, position: Int) {
        val book=itemlist[position]
        holder.txt6.text=book.bookid
        holder.txtbookname.text=book.bookname
        holder.txtbookauthor.text=book.bookauthor
        holder.txtrating.text=book.bookrating
        holder.txtbookprice.text=book.bookprice
        //holder.bookimage.setImageResource(book.bookimg)
        Picasso.get().load(book.bookimg).error(R.drawable.default_book_cover).into(holder.bookimage)

        holder.lin1.setOnClickListener{
            val intent=Intent(context,MainActivity2::class.java)
            intent.putExtra("book_id",book.bookid)
            context.startActivity(intent)
        }



    }

}

private fun ImageView.setImageResource(s5: String) {

}
