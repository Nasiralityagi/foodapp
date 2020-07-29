package com.tirade.android.widget
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.tlb.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet.view.*

class BottomSheetCameraGallery: BottomSheetDialogFragment() {

    private var mBottomSheetListener: BottomSheetListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)

        //handle clicks
        v.camera.setOnClickListener {
            mBottomSheetListener!!.onOptionClick("CAMERA")
            dismiss() //dismiss bottom sheet when item click
        }
        v.gallery.setOnClickListener {
            mBottomSheetListener!!.onOptionClick("GALLERY")
            dismiss()
        }
        v.cancel.setOnClickListener {
            dismiss()
        }

        return v
    }

    interface BottomSheetListener {
        fun onOptionClick(text: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mBottomSheetListener = context as BottomSheetListener?
        }
        catch (e: ClassCastException){
            throw ClassCastException(context.toString())
        }
    }

}