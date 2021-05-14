package com.matvey.newsapp.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.matvey.newsapp.NewsApplication
import com.matvey.newsapp.R
import com.matvey.newsapp.model.database.News
import com.matvey.newsapp.viewModel.NewsListViewModel
import com.google.android.material.snackbar.Snackbar
import com.matvey.newsapp.databinding.FragmentNewsListBinding
import com.matvey.newsapp.viewModel.NewsListViewModelFactory

class NewsListFragment : Fragment() {

    private lateinit var binding: FragmentNewsListBinding

    private val viewModel: NewsListViewModel by viewModels {
        NewsListViewModelFactory((activity?.application as NewsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.loadNews()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = FragmentNewsListBinding.inflate(layoutInflater)
        val view = binding.root

        val adapter = NewsListAdapter.getAdapter(
            loader = { url, imageView ->
                (activity?.application as NewsApplication).repository.loadImage(url, imageView)
            },
            object : OnItemClickListener {
            override fun onItemClicked(new: News) {
                activity!!.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                            NewDetailsFragment.newInstance(Bundle().apply {
                                putInt(NewDetailsFragment.NEW_ID, new.id) }))
                        .addToBackStack("details")
                        .commit()
            }
        })

        binding.newsRecyclerview.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.loadNews()
        }

        binding.search.addTextChangedListener {
            text: Editable? -> viewModel.loadNews(text.toString())
        }

        viewModel.news.observe(viewLifecycleOwner) { news ->
            news?.let {
                adapter.submitList(it)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            binding.loading.visibility = when (isLoading) {
                true -> View.VISIBLE
                false -> View.GONE
            }
            binding.swipeRefreshLayout.visibility = when (isLoading) {
                true -> View.GONE
                false -> View.VISIBLE
            }
        }

        viewModel.showSnackBar.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NewsListFragment()
    }
}