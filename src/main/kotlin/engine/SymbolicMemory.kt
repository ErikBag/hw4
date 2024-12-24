package engine

import ast.Expression

class MyMemory {
    private val memory = mutableMapOf<String, Expression>()

    fun put(param_name: String, expr: Expression) {
        memory[param_name] = expr
    }

    fun get(param_name: String): Expression {
        return memory[param_name] ?: throw RuntimeException("$param_name not in memory")
    }

    fun copy(): MyMemory {
        return MyMemory().also { newMemory ->
            for ((k, v) in memory) {
                newMemory.put(k, v)
            }
        }
    }

    override fun toString(): String = memory.map { (key, value) -> "$key = $value" }.joinToString("\n")
}
