package engine

import ast.Function
import ast.ReturnStmt
import ast.VarRef


class Runner {
    fun run(f: Function): List<State> {
        val interpreter = Interpreter()
        val memory = MyMemory()
        f.parameters.forEach{param -> memory.put(param.name, VarRef(param.name, param.type))}


        var states = listOf(
            State(
                memory,
                f.body + listOfNotNull(f.returnValue?.let{ReturnStmt(it)}),
                emptyList(),
                null
            )
        )
        val result = mutableListOf<State>()

        while (states.isNotEmpty()) {
            val nowState = states.first()
            states = states.drop(1)

            if (nowState.res != null) {
                result.add(nowState)
            }

            val nextState = interpreter.step(nowState)
            states = nextState + states
        }
        return result
    }
}
