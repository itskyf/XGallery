package com.team02.xgallery.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentPhotoBinding


class PhotoFragment : Fragment() {

    //    private lateinit var photoViewModel: PhotoViewModel
    var adapter: CustomItem? = null
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        photoViewModel =
//                ViewModelProvider(this).get(PhotoViewModel::class.java)
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        val view = binding.root
        // data to populate the RecyclerView with
//        val data = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48")

        val thumbnails = arrayOf<Int>(
            R.drawable.pic01_small,
            R.drawable.download,
            R.drawable.pic01_small,
            R.drawable.download,
            R.drawable.pic01_small,
            R.drawable.download,
            R.drawable.download,
            R.drawable.pic01_small,
            R.drawable.pic01_small,
            R.drawable.pic01_small,
            R.drawable.pic01_small,
            R.drawable.pic01_small,
            R.drawable.pic01_small,
            R.drawable.pic01_small
        )
//        var hashMap : HashMap<String, Array<Int>> =  HashMap<String, Array<Int>> ()
//        hashMap.put("13-03-2021" , arrayOf<Int>(R.drawable.pic01_small,R.drawable.download,R.drawable.pic01_small,R.drawable.download,R.drawable.pic01_small,R.drawable.download,R.drawable.download,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small))
//        hashMap.put("11-03-2021" , arrayOf<Int>(R.drawable.pic01_small,R.drawable.download,R.drawable.download,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small))
//        hashMap.put("12-03-2021" , arrayOf<Int>(R.drawable.pic01_small,R.drawable.download,R.drawable.download,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small))
//        hashMap.put("10-03-2021" , arrayOf<Int>(R.drawable.pic01_small,R.drawable.download,R.drawable.download,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small,R.drawable.pic01_small))

        binding.recyclerView.layoutManager = GridLayoutManager(activity, 3)
        adapter = CustomItem(thumbnails)
        binding.recyclerView.adapter = adapter

        return view
    }
}

//package com.example.projectandroid.ui.photo
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import com.example.projectandroid.R
//import com.example.projectandroid.ui.album.AlbumViewModel
//
//class PhotoFragment : Fragment() {
//
//    private lateinit var photoViewModel: PhotoViewModel
//
//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        photoViewModel =
//                ViewModelProvider(this).get(PhotoViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_photo, container, false)
//        val textView: TextView = root.findViewById(R.id.text_photo)
//        photoViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//        return root
//    }
//}