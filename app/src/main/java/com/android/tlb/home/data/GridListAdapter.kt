package com.android.tlb.home.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.tlb.databinding.GridListItemBinding
import com.android.tlb.home.data.model.ViewList

class GridListAdapter(
    private val requireActivity: FragmentActivity,
    private val itemList: List<ViewList>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val feeder = itemList[position]
        (holder as ListViewHolder).bind(feeder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListViewHolder(
            GridListItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    private inner class ListViewHolder(private var applicationBinding: GridListItemBinding) :
        RecyclerView.ViewHolder(applicationBinding.root) {

        fun bind(feed: ViewList) {
            applicationBinding.viewModel = feed

            itemView.setOnClickListener {
                //command.onCategoryClick(adapterPosition)
                /* val intent = Intent(requireActivity, DetailsActivity::class.java)
                 val gson = Gson()
                 intent.putExtra("feed", gson.toJson(feed))
                 requireActivity.startActivity(intent)*/
            }
        }
    }

}
