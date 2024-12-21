import ast.ASTBuilder
import engine.Runner
import generated.mygrammarLexer
import generated.mygrammarParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.FileInputStream
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.DriverManager.println
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    if (args.size != 2) {
        println("So many arguments\nProvide two arguments: <inputPath> <outputPath>")
        return
    }
    val inputPath = Paths.get(args[0])
    val outputPath = Paths.get(args[1])
    if (!Files.exists(inputPath)) {
        System.err.println("File not found: $inputPath")
        exitProcess(1)
    }

    val input_text = CharStreams.fromStream(FileInputStream(inputPath.toFile()))
    val parser = mygrammarParser(CommonTokenStream(mygrammarLexer(input_text)))
    if (parser.numberOfSyntaxErrors > 0) {
        System.err.println("fatal: encountered syntax errors")
        exitProcess(1)
    }

    val astbBuilder = ASTBuilder.create()
    ParseTreeWalker().walk(astbBuilder, parser.function())

    val result = Runner().run(astbBuilder.getFunction())

    PrintWriter(outputPath.toFile()).use { writer ->
        result.forEach {
            writer.println(it)
        }
    }
}
