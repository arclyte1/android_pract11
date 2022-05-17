package com.example.pract11

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import java.io.FileOutputStream
import java.io.IOException


class PdfDocumentAdapter(var context: Context?) : PrintDocumentAdapter() {

    private var pageHeight = 0
    private var pageWidth = 0
    private var myPdfDocument: PdfDocument? = null
    private var totalpages = 4

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal,
        callback: LayoutResultCallback,
        metadata: Bundle?
    ) {
        myPdfDocument = PdfDocument()
        pageHeight = newAttributes.mediaSize!!.heightMils / 1000 * 72
        pageWidth = newAttributes.mediaSize!!.widthMils / 1000 * 72
        if (cancellationSignal.isCanceled) {
            callback.onLayoutCancelled()
            return
        }
        if (totalpages > 0) {
            val builder = PrintDocumentInfo.Builder("print_output.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(totalpages)
            val info = builder.build()
            callback.onLayoutFinished(info, true)
        } else {
            callback.onLayoutFailed("Page count is zero.")
        }
    }

    override fun onWrite(
        pageRanges: Array<PageRange?>?,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal,
        callback: WriteResultCallback
    ) {
        for (i in 0 until totalpages) {
            if (pageInRange(pageRanges, i)) {
                val newPage = PageInfo.Builder(
                    pageWidth,
                    pageHeight, i
                ).create()
                val page = myPdfDocument!!.startPage(newPage)
                if (cancellationSignal.isCanceled) {
                    callback.onWriteCancelled()
                    myPdfDocument!!.close()
                    myPdfDocument = null
                    return
                }
                drawPage(page, i)
                myPdfDocument!!.finishPage(page)
            }
        }
        try {
            myPdfDocument!!.writeTo(
                FileOutputStream(
                    destination.fileDescriptor
                )
            )
        } catch (e: IOException) {
            callback.onWriteFailed(e.toString())
            return
        } finally {
            myPdfDocument!!.close()
            myPdfDocument = null
        }
        callback.onWriteFinished(pageRanges)
    }

    private fun pageInRange(pageRanges: Array<PageRange?>?, page: Int): Boolean {
        for (i in pageRanges!!.indices) {
            if (page >= pageRanges[i]!!.start &&
                page <= pageRanges[i]!!.end
            ) return true
        }
        return false
    }

    private fun drawPage(
        page: PdfDocument.Page,
        pagenumber: Int
    ) {
        var pagenumber = pagenumber
        val canvas: Canvas = page.canvas
        pagenumber++ // Make sure page numbers start at 1
        val titleBaseLine = 72
        val leftMargin = 54
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 40F
        canvas.drawText(
            "Test Print Document Page $pagenumber",
            leftMargin.toFloat(),
            titleBaseLine.toFloat(),
            paint
        )
        paint.textSize = 14F
        canvas.drawText(
            "This is some test content to verify that custom document printing works",
            leftMargin.toFloat(),
            (titleBaseLine + 35).toFloat(),
            paint
        )
        if (pagenumber % 2 == 0) paint.color = Color.RED else paint.color = Color.GREEN
        val pageInfo = page.info
        canvas.drawCircle(
            (pageInfo.pageWidth / 2).toFloat(),
            (pageInfo.pageHeight / 2).toFloat(),
            150F,
            paint
        )
    }


}