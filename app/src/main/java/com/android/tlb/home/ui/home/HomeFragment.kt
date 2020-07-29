package com.android.tlb.home.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.tlb.R
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(), TrashTalkHomeListener, TrashTalkHome.SearchListener {

    private var homeData = ArrayList<Data>()
    private var dataListHome: ArrayList<Data> = ArrayList()
    private var newsFeedData = ArrayList<com.tirade.android.core.trashtalk.home.data.model.newsfeed.Data>()
    private var dataListNewsFeed: ArrayList<com.tirade.android.core.trashtalk.home.data.model.newsfeed.Data> = ArrayList()
    private lateinit var viewModel: TrashTalkHomeViewModel
    private lateinit var binding: TrashTalkHomeFragmentBinding
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500 //delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.
    var handler: Handler? = null


    private lateinit var trashTalkHomeListAdapter: TrashTalkHomeListAdapter
    private lateinit var newsFeedListAdapter: NewsFeedListAdapter
    private lateinit var categorySelected: String

    override fun onFailure(message: String) {
        progress_bar.hide()
        root_layout.snackBar(message)
    }

    override fun onCategoryClick(position: Int) {
        //context?.toast("Position $position")
    }

    override fun onTrashClick() {
        context?.goActivity(TrashActivity::class.java)
    }

    override fun onShoutOutClick() {
        val intent = Intent(context, ShoutOutActivity::class.java)
        intent.putExtra("type", "shout out")
        context?.startActivity(intent)
    }

    override fun onRepresentClick() {
        val intent = Intent(context, ShoutOutActivity::class.java)
        intent.putExtra("type", "represent")
        context?.startActivity(intent)
    }

    private fun getListItems(): ArrayList<UserData> {
        val listItems = ArrayList<UserData>()
        for (i in 0 until homeData.size) {
            val userData = UserData()
            val data = homeData[i]
            userData.post_id = data.post_id
            userData.user_id = data.user_id
            userData.username = data.username
            userData.category = data.category
            userData.user_profile = data.user_profile
            userData.checked =  false
            listItems.add(userData)
        }
        return listItems
    }

    override fun onDebateClick() {
        try {
            val intent = Intent(context, DebateActivity::class.java)
            val listItems = getListItems()
            val newList: ArrayList<UserData> =
                listItems.distinctBy { it.user_id } as ArrayList<UserData>
            intent.putExtra("bundle", newList)
            context?.startActivity(intent)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onWblowerClick() {
        context?.goActivity(WblowerActivity::class.java)
    }

    override fun onJokesClick() {
        context?.goActivity(JokesActivity::class.java)
    }

    override fun onForumsClick() {
        context?.goActivity(ForumsActivity::class.java)
    }

    override fun onShameClick() {
        context?.goActivity(ShameActivity::class.java)
    }

    override fun onPostActionResponse(value: String) {
        trashTalkHomeListAdapter.notifyDataSetChanged()
    }

    override fun onPostActionClick(post_id:Int,user_id:Int,value: String) {
        viewModel.onPostAction(post_id,user_id,value)
    }

    override fun onSuccessComment(list: Boolean) {
        if (list)
            refresh()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        // With ViewModelFactory
        viewModel = ViewModelProvider(this@TrashTalkHomeFragment, TrashTalkModelFactory()).get(
            TrashTalkHomeViewModel::class.java
        )

        binding.viewModel = viewModel
        viewModel.homeListListener = this

        handler = Handler()
        timer = Timer()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cross.setOnClickListener {
            etSearch.setText("")
            searchView.visibility = View.GONE
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                trashTalkHomeListAdapter.filter.filter(etSearch.text.toString())
            }
        })

        (activity as TrashTalkHome?)!!.registerSearchListner(this)

        setRecyclerViewHome(dataListHome)
        setRecyclerViewNewsFeed(dataListNewsFeed)
        swipeRefresh.setOnRefreshListener {
            refresh()
        }
        if (arguments != null) {
            categorySelected = requireArguments().getString(categoryName)!!
        }
        callHomeData()
        callNewsFeed()
    }

    private fun callHomeData() {
        viewModel.getHomeListItems()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                homeData.clear()
                homeData.addAll(it)
                setRecyclerViewHome(it)
            }
        })
    }

    private fun callNewsFeed() {
        viewModel.getNewsFeedListItems()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                newsFeedData.clear()
                newsFeedData.addAll(it)
                setRecyclerViewNewsFeed(it)
            }
        })
    }

    private fun refresh() {
        swipeRefresh.isRefreshing = true

        callHomeData()
        callNewsFeed()

        setRecyclerViewHome(dataListHome)
        setRecyclerViewNewsFeed(dataListNewsFeed)
        trashTalkHomeListAdapter.notifyDataSetChanged()
        newsFeedListAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            val type = data!!.getStringExtra("type")
            refresh()
        }
    }

    private fun setRecyclerViewHome(dataList: ArrayList<Data>) {
        if (arguments != null) {
            categorySelected = requireArguments().getString(categoryName)!!
        }
        trashTalkHomeListAdapter = TrashTalkHomeListAdapter(this, requireActivity(), categorySelected)
        val categoryLinearLayoutManager = LinearLayoutManager(context)
        categoryLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_view_home.layoutManager = categoryLinearLayoutManager
        trashTalkHomeListAdapter.setAppList(dataList)
        recycler_view_home.adapter = trashTalkHomeListAdapter
        swipeRefresh.isRefreshing = false
        recycler_view_home.setHasFixedSize(true)
    }


    private fun setRecyclerViewNewsFeed(dataList: ArrayList<com.tirade.android.core.trashtalk.home.data.model.newsfeed.Data>) {
        newsFeedListAdapter = NewsFeedListAdapter(this, requireActivity())
        view_pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        view_pager.adapter = newsFeedListAdapter
        newsFeedListAdapter.setAppList(dataList)
        swipeRefresh.isRefreshing = false
        timer?.schedule(object: TimerTask() { // task to be scheduled
            override fun run() {
                handler?.post( Runnable {
                    if(dataList.size == 0)
                        return@Runnable
                    if (currentPage == dataList.size-1) {
                        currentPage = 0
                    }
                    view_pager.setCurrentItem(currentPage++, true)
                } )
            }
        }, DELAY_MS, PERIOD_MS)
    }

    companion object {
        private const val categoryName = "param1"

        fun newInstance(param1: String): TrashTalkHomeFragment {
            val fragment = TrashTalkHomeFragment()
            val args = Bundle()
            args.putString(categoryName, param1)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onSearchClick() {
        searchView.visibility = View.VISIBLE
    }
}
