package com.technob.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_favorite_single_row.*


class FavoriteAdapter(val context: Context,val bookList: List<BookEntity>):RecyclerView.Adapter<FavoriteAdapter.FavouriteViewHolder>() {
   class FavouriteViewHolder(view: View): RecyclerView.ViewHolder(view){
       val txtBookName: TextView =view.findViewById(R.id.txtFavBookTitle)
       val txtBookAuthor: TextView =view.findViewById(R.id.txtFavBookAuthor)
       val txtBookPrice: TextView =view.findViewById(R.id.txtFavBookPrice)
       val txtBookRating: TextView =view.findViewById(R.id.txtFavBookRating)
       val bookimage:ImageView=view.findViewById(R.id.imgFavBookImage)
       val l1Content:LinearLayout=view.findViewById(R.id.l1)



   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_favorite_single_row ,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size

    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val book=bookList[position]

        holder.txtBookName.text=book.book_name
        holder.txtBookAuthor.text=book.book_author
        holder.txtBookPrice.text=book.book_price
        holder.txtBookRating.text=book.book_rating
        Picasso.get().load(book.book_image).error(R.drawable.default_book_cover).into(holder.bookimage)



    }
}