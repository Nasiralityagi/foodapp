package com.android.tlb.home.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.tlb.databinding.TrendingPagerListItemBinding
import com.android.tlb.home.data.model.ViewList

class TrendingListPagerAdapter(
    private val requireActivity: FragmentActivity,
    private val mCategoryList: List<ViewList>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return mCategoryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val feeder = mCategoryList[position]
        (holder as RecyclerViewHolder).bind(feeder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val applicationBinding = TrendingPagerListItemBinding.inflate(layoutInflater, parent, false)
        return RecyclerViewHolder(applicationBinding)
    }

    inner class RecyclerViewHolder(private var applicationBinding: TrendingPagerListItemBinding) :
        RecyclerView.ViewHolder(applicationBinding.root) {

        fun bind(feed: ViewList) {
            applicationBinding.viewModel = feed

            /* itemView.setOnClickListener {
                 listener.onCategoryClick(adapterPosition)
                 val intent = Intent(requireActivity, DetailsActivity::class.java)
                 val gson = Gson()
                 intent.putExtra("feed", gson.toJson(feed))
                 requireActivity.startActivity(intent)
             }*/
        }
    }
}
