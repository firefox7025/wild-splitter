import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ForkJoinPool

object Application {
    private var commonPool: ForkJoinPool = ForkJoinPool.commonPool()

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val inputdir = args[0]
        val outdir = args[1]
        val fileFinderService = FileFinderService()
        val files = fileFinderService.findFiles(inputdir)
        files.parallelStream()
                .forEach { pdf: Path ->
                    val pdfRunner = PdfRunner()
                    pdfRunner.outputDir = Paths.get(outdir)
                    pdfRunner.pdf = pdf
                    commonPool.submit(pdfRunner)
                }

        while(commonPool.runningThreadCount != 0) {
            Thread.sleep(10000);
        }
    }
}