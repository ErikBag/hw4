package engine

import ast.Statement

class Statements(
    val memory: MyMemory,
    var nextStatements: List<Statement>,
    val pc: List<String>,
    var res: String? = null
) {
    override fun toString() = "{\n$memory\npc = ${pc.joinToString(" & ")}\nresult = $res\n}"
}