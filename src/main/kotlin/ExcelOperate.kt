import jxl.Cell
import jxl.Sheet
import jxl.Workbook
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.io.File
import java.io.FileWriter

@Serializable
internal data class OutData(
    val zh: String,
    val en: String,
    val th: String,
)

fun main() {
    val sheet: Sheet
    var cell1: Cell
    var cell2: Cell
    var cell3: Cell
    val list: MutableList<OutData> = mutableListOf()
    // 为要读取的excel文件名
    val rootDir = "/Users/stone/Documents/project_ij/excel-action"
    val book: Workbook = Workbook.getWorkbook(File("$rootDir/abc.xls"))

    // 获得第一个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
    sheet = book.getSheet(0)
    for (i in 0 until sheet.rows) {
        // 获取每一行的单元格
        cell1 = sheet.getCell(0, i) // （列，行）
        cell2 = sheet.getCell(1, i)
        cell3 = sheet.getCell(2, i)
        if (cell1.contents.isEmpty()) { // 如果读取的数据为空
            break
        }
        list.add(OutData(cell1.contents, cell2.contents, cell3.contents))
    }

    val filePath = "$rootDir/src/"
    val writer = FileWriter(File(filePath, "output-en.txt"))

    println("\n========英文=========================")
    val keySet = list.map {
        it.en.toLowerCase()
            .replace(" ", "_")
            .replace(" ", "_")
            .replace(",_", "_")
            .replace("?", "")
    }

    list.mapIndexed { index, it ->
        "<string name=\"${keySet[index]}\">${it.en}</string>"
    }.forEach {
        writer.write("$it\n")
        println(it)
    }
    writer.use {
        it.flush()
        it.close()
    }

    println("\n========中文=========================")
    val writerZh = FileWriter(File(filePath, "output-zh.txt"))
    list.mapIndexed { index, it ->
        "<string name=\"${keySet[index]}\">${it.zh}</string>"
    }.forEach {
        writerZh.write("$it\n")
        println(it)
    }
    writerZh.use {
        it.flush()
        it.close()
    }

    println("\n========泰文=========================")
    val writerTh = FileWriter(File(filePath, "output-th.txt"))
    list.mapIndexed { index, it ->
        "<string name=\"${keySet[index]}\">${it.th}</string>"
    }.forEach {
        writerTh.write("$it\n")
        println(it)
    }

    printJson(list)

    book.close()
}

private fun printJson(list: List<OutData>) {
    val element = buildJsonObject {
//                putJsonObject("owner") {
//                    put("name", "kotlin")
//                }
        putJsonArray("trans") {
            list.forEach {
                addJsonObject {
                    put("zh", it.zh)
                    put("en", it.en)
                    put("th", it.th)
                }
            }
        }
    }
    //json output
//    println(element.toString())

    val filePath = "/Users/stone/Documents/project_ij/excel-action/src/"
    val writer = FileWriter(File(filePath, "output-json.txt"))
    writer.use {
        it.write(element.toString())
        it.flush()
        it.close()
    }


}