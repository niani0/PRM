package com.example.mp3test.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mp3test.adapters.ArticleAdapter
import com.example.mp3test.data.DataSource
import com.example.mp3test.data.UserDatabase
import com.example.mp3test.databinding.FragmentListBinding
import com.example.mp3test.models.Article
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import kotlin.concurrent.thread


class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: ArticleAdapter
    private lateinit var articles: ArrayList<Article>
    private lateinit var db: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = UserDatabase.open(requireContext())
        thread {
            DataSource.loggedInUser = db.users.getUserByLogin(login = DataSource.email)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {

        thread {
            DataSource.loggedInUser = db.users.getUserByLogin(login = DataSource.email)
        }

        adapter = ArticleAdapter().apply {
            setOnItemClickListener(object : ArticleAdapter.OnItemClickListener {
                override fun onItemClick(article: Article) {
                    thread {
                        DataSource.loggedInUser?.articleTitles?.add(article.title)
                        DataSource.setWithoutDuplicates = HashSet(DataSource.loggedInUser?.articleTitles)
                        DataSource.loggedInUser?.articleTitles?.clear()
                        DataSource.loggedInUser?.articleTitles?.addAll(DataSource.setWithoutDuplicates)
                        if (article.readMoreUrl != "null") {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.readMoreUrl))
                            startActivity(intent)
                        }else{
                            requireActivity().runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                "Article is not available",
                                Toast.LENGTH_SHORT
                            ).show()
                                }
                        }
                        DataSource.loggedInUser.let {
                            if (it != null) {
                                db.users.updateUser(it)
                            }
                        }
                        requireActivity().runOnUiThread {
                            adapter.replace(articles)
                        }
                    }
                }
            })
        }

        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(requireContext())

        val url = "http://knopers.com.pl:5000/news?category=science"

        downloadUrl(url)

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun downloadUrl(url: String) {

        GlobalScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            try {
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()
                responseData?.let { parser(it) }

            } catch (e: IOException) {
                print(e)
            }
        }
    }

    private fun parser(data: String) {
        articles = ArrayList()
        try {
            val jsonData = JSONObject(data)

            val dataArray = jsonData.getJSONArray("data")

            for (i in 0 until dataArray.length()) {
                val item = dataArray.getJSONObject(i)

                val article = Article(
                    item.getString("author"),
                    item.getString("content"),
                    item.getString("date"),
                    item.getString("id"),
                    item.getString("imageUrl").substringBefore("?"),
                    item.getString("readMoreUrl"),
                    item.getString("time"),
                    item.getString("title"),
                    item.getString("url")
                )

                articles.add(article)
            }
                requireActivity().runOnUiThread {
                    adapter.replace(articles)
                }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}