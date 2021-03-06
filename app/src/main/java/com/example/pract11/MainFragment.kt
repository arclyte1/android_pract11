package com.example.pract11

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pract11.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result!!.data!!.data
            val type = requireActivity().contentResolver.getType(Uri.parse(data.toString()))!!
            Log.d("data", type)
            val bundle = bundleOf("uri" to data.toString())

            Log.d("type", type)

            if (type.startsWith("image"))
                findNavController().navigate(R.id.action_mainFragment_to_imagePrinterFragment, bundle)

            if (type == "text/html")
                findNavController().navigate(R.id.action_mainFragment_to_webPrinterFragment, bundle)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        binding.pickImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(intent)
        }

        binding.selectHtmlButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "text/html"
            resultLauncher.launch(intent)
        }

        binding.openUrlButton.setOnClickListener {
            val url = binding.urlEditText.text.toString()
            val bundle = bundleOf("uri" to url)
            findNavController().navigate(R.id.action_mainFragment_to_webPrinterFragment, bundle)
        }

        binding.pickPdfButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            resultLauncher.launch(intent)
        }
    }
}