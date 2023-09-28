package com.example.dietproapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.dietproapp.NavigasiActivity
import com.example.dietproapp.databinding.ActivityDetailHomeNewsBinding
import com.inyongtisto.myhelper.extension.intentActivity

class DetailHomeNewsActivity : AppCompatActivity() {

    private var _binding: ActivityDetailHomeNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailHomeNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val articleUrl = intent.getStringExtra("article_url")

        val webView = binding.webView
        webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // Load JavaScript code to extract and display text
                    val javascript = """
                        var body = document.body.innerText;
                        document.body.innerHTML = '<div id="content">' + body + '</div>';
                    """.trimIndent()
                    evaluateJavascript(javascript, null)
                }
            }
            if (articleUrl != null) {
                loadUrl(articleUrl)
            }
        }

        mainButton()
    }

    private fun mainButton() {
        binding.toolbar.setOnClickListener {
            intentActivity(NavigasiActivity::class.java)
        }

        binding.imgShare.setOnClickListener {
            val articleUrl = intent.getStringExtra("article_url")
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            share.putExtra(Intent.EXTRA_TEXT, articleUrl)
            startActivity(Intent.createChooser(share, "Bagikan ke : "))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
