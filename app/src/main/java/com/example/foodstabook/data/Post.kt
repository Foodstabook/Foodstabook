package com.example.foodstabook.data

data class Post(
    var uid: String? = "",
    var username: String? = "",
    var title: String? = "",
    var hastags: String? = "",
    var place: String? = "",
    var rating: Int = 0,
    var description: String? = "",
    //var stars: MutableMap<String, Boolean> = HashMap()
) {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "UID" to uid,
            "Username" to username,
            "Title" to title,
            "Hashtags" to hastags,
            "Place" to place,
            "Rating" to rating,
            "Description" to description
        )
    }
}
