import java.io.IOException
import java.nio.file.Path

object Application {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val inputdir = args[0]
        val outdir = args[1]
        val fileFinderService = FileFinderService()
        val pdfService = PdfService(outdir)
        val files = fileFinderService.findFiles(inputdir)
        files.parallelStream()
                .forEach { pdf: Path? ->
                    if (pdf != null) {
                        pdfService.parsePdf(pdf)
                    }
                }
    }
}