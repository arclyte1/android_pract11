package com.example.pract11

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.print.PrintHelper
import com.example.pract11.databinding.FragmentImagePrinterBinding

class ImagePrinterFragment : Fragment() {

    private lateinit var binding: FragmentImagePrinterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_printer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentImagePrinterBinding.bind(view)

        val uri = Uri.parse(savedInstanceState!!.getString("uri"))

        binding.imageView.setImageURI(uri)

        binding.printButton.setOnClickListener {
            activity?.also { context ->
                PrintHelper(context).apply {
                    scaleMode = PrintHelper.SCALE_MODE_FIT
                }.also { printHelper ->
                    printHelper.printBitmap("image print", uri)
                }
            }
        }


    }
}