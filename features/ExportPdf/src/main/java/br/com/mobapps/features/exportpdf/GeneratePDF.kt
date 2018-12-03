package br.com.mobapps.features.exportpdf

import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import java.io.FileOutputStream

class GeneratePDF() {

    fun onGenerate() {
        val document = Document()

        PdfWriter.getInstance(document, FileOutputStream(""))

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