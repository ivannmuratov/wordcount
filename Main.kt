import java.io.File

// Cловарь для хранения слов и их кол-ва в тексте
val map = mutableMapOf<String, Int>()

fun main() {
    // Читаем файл с текстом и получаем список строк
    val textRus = File("МёртвыеДуши.txt").readLines()

    val rusTextClear = cleanText(textRus)
    for (string in rusTextClear) { // Проходим каждую строку в тексте
        val words = string.lowercase().split(" ") // Переводим слова в нижний регистр и делим по пробелу
        words.forEach { word -> putToMap(word) }
    }
    writeToFile("resultRus.txt") // Записываем в файл
    findMax() // Находим самое часто встречаемое слово
    checkOnes() // Находим отношение слов по одному раз ко всем словам
}

// Функция для добавления в словарь
fun putToMap(word: String) {
    if (word.isBlank()) { // Если слово - пробел не добавляем
        return
    }
    if (map.contains(word)) { // Если слово уже встречалось увеличиваем число появлений на 1
        map[word] = map.getValue(word).plus(1)
        return
    }
    map[word] = 1 // Если слово встретилось впервые - добавляем в словарь и устанавливаем число появлений в 1
}

// Функция удаления знаков препинания из текста
// Использует технологию регулярных выражений
fun cleanText(text: List<String>): List<String> =
    text.map {
        it.replace(Regex("[[--]*.,!?\"':«»—;]"), " ") // с [--] Есть какие-то проблемы, пока оставил так
        // -{2} почему-то выдаёт другой результат
    }

// Функция для записи в файл.
fun writeToFile(fileName: String) {
    val file = File(fileName) // Создаём файл с названием, переданным в качестве аргумента функции
    file.bufferedWriter().use { out -> // Используем Java-класс BufferedWriter, для записи содержимого словаря в файл
        map.forEach {
            out.write("${it.key}: ${it.value}\n")
        }
    }
}
// Функция для нахождения самого часто встречаемого слова
fun findMax(): String {
    var mx = -1 // mx - переменная отвечающая за хранение максимального значений появлений слов
    var mxString = "" // Переменная для хранения строки с максимальным значением
    map.forEach {
        if (it.value > mx) {
            mx = it.value
            mxString = it.key
        }
    }
    println("Самое часто встречаемое слово '$mxString': $mx")
    return mxString
}

// Функция для нахождения отношений слов по 1 ко всем словам
fun checkOnes(): Double {
    var sumOnes = 0.0 // Сумма появлений слов по 1
    var sumAll = 0.0 // Сумма появлений всех слов
    map.forEach {
        sumAll += it.value
        if (it.value == 1)
            sumOnes += it.value
    }
    val percent =  (sumOnes / sumAll) * 100.0 // Находим отношение в процентах
    println("Доля слов, которые встречаются только один раз: ${sumOnes.toInt()}/${sumAll.toInt()} = $percent%")
    // Переводим переменные sumAll и sumOnes в Int, чтобы логично отображалось кол-во слов
    return percent
}