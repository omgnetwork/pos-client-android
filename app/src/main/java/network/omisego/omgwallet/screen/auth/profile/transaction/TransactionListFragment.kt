package network.omisego.omgwallet.screen.auth.profile.transaction

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.pagination.PaginationList
import co.omisego.omisego.model.transaction.Transaction
import kotlinx.android.synthetic.main.fragment_transaction_list.*
import network.omisego.omgwallet.R
import network.omisego.omgwallet.base.LoadingRecyclerAdapter
import network.omisego.omgwallet.custom.MarginDividerDecorator
import network.omisego.omgwallet.databinding.FragmentTransactionListBinding
import network.omisego.omgwallet.databinding.ViewholderTransactionBinding
import network.omisego.omgwallet.extension.dpToPx
import network.omisego.omgwallet.extension.provideAndroidViewModel
import network.omisego.omgwallet.extension.state
import network.omisego.omgwallet.extension.toast
import network.omisego.omgwallet.state.TransactionListState

class TransactionListFragment : Fragment() {
    private lateinit var binding: FragmentTransactionListBinding
    private lateinit var viewModel: TransactionListViewModel
    private lateinit var adapter: LoadingRecyclerAdapter<Transaction, ViewholderTransactionBinding>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var currentPage: Int = 1
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var loading: Boolean = false
    private var isLastPage: Boolean = false
    private val dividerDecorator by lazy {
        val paddingSize = context?.dpToPx(16f)!!
        val margin = Rect(paddingSize, 0, paddingSize, 0)
        MarginDividerDecorator(context!!, margin)
    }

    companion object {
        val TOTAL_MOCK_LOADING_ITEM = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideAndroidViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_transaction_list,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
        loadTransaction(currentPage)
        subscribeLoadMore()
        subscribeTransactionInfo()
        swipeRefresh.setOnRefreshListener {
            currentPage = 1
            isLastPage = false
            adapter.clearItems()
            loadTransaction(currentPage)
        }
        viewModel.liveResult.observe(this, Observer {
            it?.handle(this::handleLoadTransactionSuccess, this::handleLoadTransactionFail)
        })
    }

    private fun handleLoadTransactionSuccess(listTransaction: PaginationList<Transaction>) {
        hideLoading()
        when (listTransaction.state) {
            TransactionListState.STATE_EMPTY_PAGE -> {
                recyclerView.visibility = View.GONE
                tvEmpty.visibility = View.VISIBLE
            }
            TransactionListState.STATE_OUT_BOUND_PAGE -> {
                isLastPage = true
            }
            TransactionListState.STATE_CONTENT_PAGE -> {
                recyclerView.visibility = View.VISIBLE
                tvEmpty.visibility = View.GONE
            }
        }
        currentPage = listTransaction.pagination.currentPage
        adapter.addItems(listTransaction.data)
    }

    private fun handleLoadTransactionFail(error: APIError) {
        context?.toast(error.description)
        hideLoading()
    }

    private fun hideLoading() {
        swipeRefresh.isRefreshing = false
        loading = false
    }

    private fun loadTransaction(page: Int) {
        recyclerView.visibility = View.VISIBLE
        recyclerView.removeItemDecoration(dividerDecorator)
        if (!swipeRefresh.isRefreshing) {
            adapter.addLoadingItems(TOTAL_MOCK_LOADING_ITEM)
        }
        loading = true
        viewModel.getTransaction(page)
    }

    private fun onLoadMore() {
        if (loading || isLastPage) return
        loadTransaction(currentPage + 1)
    }

    private fun subscribeTransactionInfo() {
        viewModel.liveTransactionFailedDescription.observe(this, Observer {
            if (!it?.isEmpty()!!) {
                context?.toast(it)
            }
        })
    }

    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context)
        adapter = LoadingRecyclerAdapter(R.layout.viewholder_transaction_loading, R.layout.viewholder_transaction, viewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = linearLayoutManager
    }

    private fun subscribeLoadMore() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!loading && totalItemCount <= (lastVisibleItem + TOTAL_MOCK_LOADING_ITEM)) {
                    onLoadMore()
                }
            }
        })
    }
}
