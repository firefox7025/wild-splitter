import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO

class PdfRunner : Runnable {
    var outputDir: Path = Paths.get(".")
        get() = field
        set(value) { field = value }
    var pdf: Path = Paths.get(".")
        get() = field
        set(value) { field = value }

    override fun run() {
        try {
            PDDocument.load(pdf.toFile()).use { doc ->
                val pdfRenderer = PDFRenderer(doc)
                val pageCount = doc.numberOfPages
                for (i in 0 until pageCount) {
                    val bufferedImage = pdfRenderer.renderImage(i)
                    try {
                        val source: LuminanceSource = BufferedImageLuminanceSource(bufferedImage)
                        val bitmap = BinaryBitmap(HybridBinarizer(source))
                        val result = MultiFormatReader().decode(bitmap)
                        val text = result.text
                        if (!text.contains("BATCH|")) { //I have gotten here, therefore a not found exception wasn't thrown
                            val theoreticalTitleImage = pdfRenderer.renderImageWithDPI(i + 1, 300f)
                            ImageIO.write(theoreticalTitleImage, "jpg", createFile(outputDir).toFile())
                        }
                    } catch (e: NotFoundException) {
                    } catch (e: Exception) {
                        println(e.toString())
                    }
                }
            }
        } catch (e: Exception) {
            println(e.toString())
        }
    }

    @Throws(IOException::class)
    private fun createFile(outputDir: Path): Path {
        val newImage = Paths.get(outputDir.toString(), UUID.randomUUID().toString() + ".jpg")
        Files.createFile(newImage)
        return newImage
    }
}