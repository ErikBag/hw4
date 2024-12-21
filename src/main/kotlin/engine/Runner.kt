package engine

import ast.Function
import ast.ReturnStmt

class Runner {
    fun run(f: Function): List<Statements> {
        val interpreter = Interpreter()

        val memory = MyMemory()
        f.parameters.forEach{memory.put(it.name, "'${it.name}'")}

        val initStatement = Statements(
            memory, 
            f.body + listOfNotNull(f.returnValue?.let { ReturnStmt(it) }), 
            emptyList())


        var statements = listOf(initStatement)

        val result = mutableListOf<Statements>()

        while (statements.isNotEmpty()) {
            val curStatement = statements.first()
            statements = statements.drop(1)

            if (curStatement.res != null) {
                result.add(curStatement)
            }

            val nextStatement = interpreter.interpret(curStatement)
            statements = nextStatement + statements
        }
        return result
    }
}
