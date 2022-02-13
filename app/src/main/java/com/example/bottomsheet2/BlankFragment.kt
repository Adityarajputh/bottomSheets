package com.example.bottomsheet2

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.bottomsheet2.databinding.FragmentBlankBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import android.widget.PopupWindow
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomsheet2.databinding.PopUpItemBinding
import kotlin.math.max


class BlankFragment : Fragment() {
    private lateinit var binding: FragmentBlankBinding
    private val channelId = "channel_id"
    private var notificationId = 101
//    private var layoutManager: RecyclerView.LayoutManager? = null
//    private var adapter: SheetAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBlankBinding.inflate(layoutInflater)
        BottomSheetBehavior.from(binding.coordinatorChild).apply {
            this.isHideable = true
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }

        createNotificationChannel()

        return binding.root
    }



    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var keyboardStatus = false

        if (binding.actionBarText.requestFocus()) {
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

//        binding.actionBarText.requestFocus()
//        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
//        imm?.showSoftInput(requireView(),InputMethodManager.SHOW_FORCED)

        binding.menuIcon.setOnClickListener {
            it.preventDoubleClick()
            val bottomFragment = ModalBottomFragment()
            bottomFragment.show(childFragmentManager,bottomFragment.TAG)
        }

        binding.testBtn.setOnClickListener {
            getNotification()
            notificationId++
        }

        binding.parentLayout.setOnTouchListener OnTouchListener@{ view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    binding.actionBarText.clearFocus()
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm?.hideSoftInputFromWindow(view.windowToken,0)
                }
                MotionEvent.ACTION_UP -> {
                    binding.actionBarText.clearFocus()
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm?.hideSoftInputFromWindow(view.windowToken,0)
                }
            }
            return@OnTouchListener true
        }




        binding.windowPopUp.setOnClickListener {
            val popUp = showPopUp(binding.windowPopUp)
            popUp.isOutsideTouchable = true
            popUp.isFocusable = true
            popUp.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popUp.elevation = 10F


            val location = IntArray(2).apply {
                binding.windowPopUp.getLocationOnScreen(this)
           }

            val position = location[1]-(binding.windowPopUp.height*4.1)
            Log.d("position",position.toString())

            popUp.showAsDropDown(binding.windowPopUp,0,-800)
            Log.d("Is showing?",popUp.isShowing.toString())
            Log.d("location coordinate",location[0].toString())
            Log.d("location coordinate",location[1].toString())

        }

        binding.firstItem.setOnClickListener {
            Toast.makeText(activity,"Location Clicked",Toast.LENGTH_SHORT).show()
        }

        binding.secondItem.setOnClickListener {
            Toast.makeText(activity,"User Files Clicked",Toast.LENGTH_SHORT).show()
        }

        binding.thirdItem.setOnClickListener {
            Toast.makeText(activity,"Share Clicked",Toast.LENGTH_SHORT).show()
        }

        binding.fourthItem.setOnClickListener {
            Toast.makeText(activity,"Delete Clicked",Toast.LENGTH_SHORT).show()
        }

        binding.userSupport.setOnClickListener {
            val bottomSheetBehavior : BottomSheetBehavior<View> = BottomSheetBehavior.from(binding.coordinatorChild)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.closeImg.setOnClickListener {
            val bottomSheetBehavior : BottomSheetBehavior<View> = BottomSheetBehavior.from(binding.coordinatorChild)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                changeBackground()
            }
            Configuration.UI_MODE_NIGHT_NO -> {}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    fun changeBackground(){
        binding.coordinatorChild.background = resources.getDrawable(R.drawable.dark_mode_bg,null)

            binding.closeImg.setImageResource(R.drawable.ic_baseline_close_dark_mode)
//          val myDrawable = resources.getDrawable(R.drawable.ic_baseline_location,null)
//            myDrawable.setColorFilter(PorterDuffColorFilter(id, PorterDuff.Mode.SRC_ATOP))
            binding.locationImg.setImageResource(R.drawable.ic_baseline_location_dark_mode)
            binding.mediaImg.setImageResource(R.drawable.ic_baseline_perm_media_dark_mode)
            binding.deleteImg.setImageResource(R.drawable.ic_baseline_delete_dark_mode)
            binding.shareImg.setImageResource(R.drawable.ic_baseline_share_dark_mode)
    }

    fun View.preventDoubleClick() {
        this.isEnabled = false
        this.postDelayed( { this.isEnabled = true }, 1000)
    }

//    fun hideKeyboard(){
//        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//     imm.hideSoftInputFromWindow(view?.windowToken, 0)
//    }

    @SuppressLint("InlinedApi")
    fun getNotification(){

        val intent = Intent(requireContext(),MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent : PendingIntent = PendingIntent
            .getActivity(requireActivity(),0,intent,FLAG_IMMUTABLE)

        val bitmap = BitmapFactory.decodeResource(activity?.resources,R.drawable.ic_baseline_share_dark_mode)

        // For lower versions
        val builder = NotificationCompat.Builder(requireActivity(),channelId)
            .setSmallIcon(R.drawable.ic_baseline_share_dark_mode)
            .setContentTitle("Test Notification")
            .setContentText("Test Notification Content Text")
            .setContentIntent(pendingIntent)
            .setLargeIcon(bitmap)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireActivity())) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ coz it
        // is required as per docs.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = requireActivity()
                        .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}



    private fun showPopUp(anchor: View): PopupWindow {
        val inflater = anchor.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.pop_up_window, null)

        val recyclerView = view.findViewById<RecyclerView>(R.id.popUpRecycler)

        val layoutManager = GridLayoutManager(anchor.context, 4)
        recyclerView.layoutManager = layoutManager

        val adapter = PopUpAdapter(PopUpList.popUpList)
        recyclerView.adapter = adapter


//    adapter.selectedItem(selectedItem)

//    adapter.setOnClick(object : RecyclerviewCallbacks<FilterItem> {
//        override fun onItemClick(view: View, position: Int, item: FilterItem) {
//            selectedItem = position
//            Toast.makeText(this@MainActivity, "data = $item", Toast.LENGTH_SHORT).show()
//            dismissPopup()
//        }
//    })

    return PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
}



//private fun showPopupWindow(anchor: View) {
//
//
//    PopupWindow(anchor.context).apply {
//        isOutsideTouchable = true
////        val inflater = LayoutInflater.from(anchor.context)
//
//        contentView = inflater.inflate(R.layout.pop_up_window,anchor.rootView,false).apply {
//
//
//            measure(
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//            )
//        }
//    }.also { popupWindow ->
//        // Absolute location of the anchor view
//        val location = IntArray(2).apply {
//            anchor.getLocationOnScreen(this)
//        }
//        val size = Size(
//            popupWindow.contentView.measuredWidth,
//            popupWindow.contentView.measuredHeight
//        )
//        popupWindow.showAtLocation(
//            anchor,
//            Gravity.TOP or Gravity.START,
//            location[0] - (size.width - anchor.width) / 2,
//            location[1] - size.height
//        )
//    }
//}