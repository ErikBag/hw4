package engine

import ast.Statement
import ast.Expression

class State(
    val memory: MyMemory,
    var nextStates: List<Statement>,
    val pc: List<Expression>,
    var res: Expression?
) {
    override fun toString() = "{\n$memory\npc = (${pc.joinToString(") & (")})\nresult = $res\n}\n"
}