package com.matvey.newsapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.matvey.newsapp.NewsApplication
import com.matvey.newsapp.R
import com.matvey.newsapp.databinding.FragmentNewDetailsBinding
import com.matvey.newsapp.viewModel.NewDetailsViewModel
import com.matvey.newsapp.viewModel.NewDetailsViewModelFactory

class NewDetailsFragment : Fragment() {

    private val viewModel: NewDetailsViewModel by viewModels {
        NewDetailsViewModelFactory((activity?.application as NewsApplication).repository)
    }

    private lateinit var binding: FragmentNewDetailsBinding

    private var newId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            newId = it.getInt(NEW_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewDetailsBinding.inflate(layoutInflater)
        val view = binding.root

        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener { activity?.supportFragmentManager?.popBackStack() }
        }

        newId?.let { id ->
            viewModel.loadNewById(id)
            viewModel.new.observe(viewLifecycleOwner) {
                it?.let { new ->
                    binding.newAuthor.text = new.author
                    binding.newTitle.text = new.title
                    binding.newContent.text = new.content
                    binding.urlToFull.apply {
                        text = new.url
                        setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(new.url)
                            startActivity(intent)
                        }
                    viewModel.loadImage(new.urlToImage, binding.newImage)
                    }

                    binding.share.apply {
                        setOnClickListener {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, new.url)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            startActivity(shareIntent)
                        }
                    }
                }
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(newData: Bundle) =
            NewDetailsFragment().apply {
                arguments = newData
            }

        const val NEW_ID = "new_id"
    }
}