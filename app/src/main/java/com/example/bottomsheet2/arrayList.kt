package com.example.bottomsheet2

object arrayList {
    val itemList = arrayListOf<Items>(
        Items(R.drawable.ic_baseline_location,"Location"),
        Items(R.drawable.ic_baseline_perm_media,"User Files"),
        Items(R.drawable.ic_baseline_share_24,"Share"),
        Items(R.drawable.ic_baseline_delete_24,"Delete")
    )

    val itemListDark = arrayListOf<Items>(
        Items(R.drawable.ic_baseline_location_dark_mode,"Location"),
        Items(R.drawable.ic_baseline_perm_media_dark_mode,"User Files"),
        Items(R.drawable.ic_baseline_share_dark_mode,"Share"),
        Items(R.drawable.ic_baseline_delete_dark_mode,"Delete")
    )

}