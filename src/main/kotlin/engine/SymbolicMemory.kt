package engine

class MyMemory {
    private val memory = mutableMapOf<String, String>()

    fun put(key: String, value: String) {
        memory[key] = value
    }

    fun get(key: String): String {
        return memory[key] ?: throw Exception("$key not in memory")
    }

    fun clone(): MyMemory {
        val newMemory = MyMemory()
        memory.forEach { (k, v) -> newMemory.put(k, v) }
        return newMemory
    }

    override fun toString(): String {
        var sb = ""
        for ((key, value) in memory) {
            sb += "$key = $value\n"
        }
        return sb
    }
}
