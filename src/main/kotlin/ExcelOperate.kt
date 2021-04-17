import jxl.Cell
import jxl.Sheet
import jxl.Workbook
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.io.File
import java.io.FileWriter

@Serializable
data class OutData(
    val zh: String,
    val zhHk: String,
    val en: String,
    val th: String,
    val la: String,
    val ms: String,
    val vn: String,
//    val ph: String,
//    val kh: String,
)

fun output(filePath: String, list: List<String>, lan: String) {
    println("\n========英文=========================")

    val writer = FileWriter(File(filePath, "output-$lan.txt"))

    list.forEach {
        writer.write("$it\n")
        println(it)
    }
    writer.use {
        it.flush()
        it.close()
    }
}

fun main() {
    val sheet: Sheet
    var cell0: Cell
    var cell1: Cell
    var cell2: Cell
    var cell3: Cell
    var cell4: Cell
    var cell5: Cell
    var cell6: Cell
    var cell7: Cell
    var cell8: Cell
    val list: MutableList<OutData> = mutableListOf()
    // 为要读取的excel文件名
    val rootDir = "/Users/stone/Documents/project_ij/excel-action"
    val book: Workbook = Workbook.getWorkbook(File("$rootDir/abc.xls"))

    // 获得第一个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
    sheet = book.getSheet(0)
    for (i in 0 until sheet.rows) {
        // 获取每一行的单元格, 不同列语言不同
        cell0 = sheet.getCell(0, i) // （列，行）
        cell1 = sheet.getCell(1, i) // （列，行）
        cell2 = sheet.getCell(2, i)
        cell3 = sheet.getCell(3, i)
        cell4 = sheet.getCell(4, i)
        cell5 = sheet.getCell(5, i)
        cell6 = sheet.getCell(6, i)
        cell7 = sheet.getCell(7, i)
        cell8 = sheet.getCell(8, i)
        if (cell1.contents.isEmpty()) { // 如果读取的数据为空
            break
        }
        list.add(OutData(cell0.contents, cell1.contents, cell2.contents, cell3.contents, cell4.contents,
            cell5.contents, cell6.contents /*, cell7.contents, cell8.contents*/))
    }
    val keyList = list.map {
        it.en.toLowerCase()
                .replace(" ", "_")
                .replace(" ", "_")
                .replace(",_", "_")
                .replace("?", "")
    }
    val filePath = "$rootDir/src/"

    val en = list.mapIndexed { index, it ->
        "<string name=\"${keyList[index]}\">${it.en}</string>"
    }
    output(filePath, en, "en")

    val zh = list.mapIndexed { index, it ->
        "<string name=\"${keyList[index]}\">${it.zh}</string>"
    }
    output(filePath, zh, "zh")

    val zhHk = list.mapIndexed { index, it ->
        "<string name=\"${keyList[index]}\">${it.zhHk}</string>"
    }
    output(filePath, zhHk, "zhHk")

    //th la ms vn
    val th = list.mapIndexed { index, it ->
        "<string name=\"${keyList[index]}\">${it.th}</string>"
    }
    output(filePath, th, "th")

    val la = list.mapIndexed { index, it ->
        "<string name=\"${keyList[index]}\">${it.la}</string>"
    }
    output(filePath, la, "la")

    val ms = list.mapIndexed { index, it ->
        "<string name=\"${keyList[index]}\">${it.ms}</string>"
    }
    output(filePath, ms, "ms")

    val vn = list.mapIndexed { index, it ->
        "<string name=\"${keyList[index]}\">${it.vn}</string>"
    }
    output(filePath, vn, "vn")


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