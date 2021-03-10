package com.technob.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "Books")

data class BookEntity(
    @PrimaryKey val book_id: Int,
    @ColumnInfo(name = "book_name") val book_name: String,
    @ColumnInfo(name = "book_author") val book_author: String,
    @ColumnInfo(name = "book_price") val book_price: String,
    @ColumnInfo(name = "book_rating") val book_rating: String,
    @ColumnInfo(name = "book_desc") val book_desc:String,
    @ColumnInfo(name = "book_image") val book_image: String


){

}