package com.example.pract11

import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.pract11.databinding.FragmentWebPrinterBinding

class WebPrinterFragment : Fragment() {

    private lateinit var binding: FragmentWebPrinterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web_printer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWebPrinterBinding.bind(view)

        val uri = arguments?.getString("uri")
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = false
        }
        binding.webView.loadUrl(uri!!)

        binding.button2.setOnClickListener {
            (activity?.getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->
                val jobName = "${getString(R.string.app_name)} HTML Document"
                val printAdapter = binding.webView.createPrintDocumentAdapter(jobName)

                printManager.print(
                    jobName,
                    printAdapter,
                    PrintAttributes.Builder().build()
                )
            }
        }
    }
}