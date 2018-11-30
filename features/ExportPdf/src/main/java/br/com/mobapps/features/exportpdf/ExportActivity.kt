package br.com.mobapps.features.exportpdf

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import br.com.mobapps.dynamicfeature.base.BaseSplitActivity
import com.itextpdf.text.*
import kotlinx.android.synthetic.main.activity_export.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Chunk




class ExportActivity: BaseSplitActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_export)

        btnGeneratePDF.setOnClickListener(::onButtonGeneratePdfPressed)
    }

    private fun onButtonGeneratePdfPressed(view: View) {
        val destination = getExternalFilesDir(null)

        val document = Document()

        PdfWriter.getInstance(document, FileOutputStream(destination))

        document.open()

        document.pageSize = PageSize.A4
        document.addCreationDate();
        document.addAuthor("Mobills")
        document.addCreator("Juarez Pereira")

        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)

        val mOrderDetailsTitleChunk = Chunk("Order Details")
        val mOrderDetailsTitleParagraph = Paragraph(mOrderDetailsTitleChunk)
        mOrderDetailsTitleParagraph.alignment = Element.ALIGN_CENTER
        document.add(mOrderDetailsTitleParagraph)

        val mOrderIdChunk = Chunk("Order No:")
        val mOrderIdParagraph = Paragraph(mOrderIdChunk)
        document.add(mOrderIdParagraph)
        document.add(Paragraph(""))
        document.add(Chunk(lineSeparator))
        document.add(Paragraph(""))
        document.close()

    }

}