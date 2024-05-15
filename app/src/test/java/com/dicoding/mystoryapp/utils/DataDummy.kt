package com.dicoding.mystoryapp.utils

import com.dicoding.mystoryapp.data.remote.StoryListItem

object DataDummy {

    fun generateDummyResponse(): List<StoryListItem> {
        val items: MutableList<StoryListItem> = arrayListOf()
        for (i in 0..100) {
            val quote = StoryListItem(
                "id$i",
                "author + $i",
                "quote $i",
                "www.photo.com/$i",
                i + 1.toFloat(),
                i + 2.toFloat()
            )
            items.add(quote)
        }
        return items
    }
}