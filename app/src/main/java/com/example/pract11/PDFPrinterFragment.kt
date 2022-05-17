package com.example.pract11

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.print.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pract11.databinding.FragmentPDFPrinterBinding


class PDFPrinterFragment : Fragment() {

    private lateinit var binding: FragmentPDFPrinterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_p_d_f_printer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPDFPrinterBinding.bind(view)

        val uri = Uri.parse(arguments?.getString("uri"))

        doPrint()

    }

    private fun doPrint() {
        activity?.also { context ->
            // Get a PrintManager instance
            val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
            // Set job name, which will be displayed in the print queue
            val jobName = "${context.getString(R.string.app_name)} Document"
            // Start a print job, passing in a PrintDocumentAdapter implementation
            // to handle the generation of a print document
            printManager.print(jobName, PdfDocumentAdapter(context), null)
        }
    }
}