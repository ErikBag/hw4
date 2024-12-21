package engine

import ast.Assignment
import ast.BinOp
import ast.BoolConstant
import ast.Expression
import ast.IfStmt
import ast.IntConstant
import ast.ReturnStmt
import ast.UnOp
import ast.VarRef
import java.util.Collections.emptyList

class Interpreter {
    fun interpret (statements: Statements): List<Statements> {
        val nextStatements = statements.nextStatements.firstOrNull() ?: return emptyList()
        statements.nextStatements = statements.nextStatements.drop(1)
        if(nextStatements is IfStmt) {
            return interpretIfStmt(statements, nextStatements)
        }

        if(nextStatements is Assignment) {
                val symValue = Evaluator(statements.memory).evaluate(nextStatements.value)
                statements.memory.put(nextStatements.name, "(${symValue})")
                return listOf(statements)
        }

        if(nextStatements is ReturnStmt) {
                val symValue = Evaluator(statements.memory).evaluate(nextStatements.returnExpr)
                statements.res = symValue
                return listOf(statements)
        }

        return emptyList()
    }

    private fun interpretIfStmt(statements: Statements, nextStatements : IfStmt): List<Statements> {
        val condition = Evaluator(statements.memory).evaluate(nextStatements.condition)
        val trueState = Statements(
            statements.memory.clone(),
            nextStatements.thenBlock + statements.nextStatements,
            statements.pc + listOf(condition)
        )
        val falseState = Statements(
            statements.memory.clone(),
            nextStatements.elseBlock + statements.nextStatements,
            statements.pc + listOf("!($condition)")
        )
        return listOf(trueState, falseState)
    }

    private class Evaluator(val memory: MyMemory) {
        fun evaluate(expression: Expression): String {
            if(expression is BinOp) {
                return evaluateBinOp(expression)
            }

            if(expression is BoolConstant) {
                return expression.value.toString()
            }

            if(expression is IntConstant) {
                return expression.value.toString()
            }

            if(expression is UnOp) {
                return "${expression.kind}(${evaluate(expression.subExpr)})"
            }

            if(expression is VarRef) {
                return memory.get(expression.identifier)
            }
            else {
                throw Exception("ErrorExpression in program")
            }
        }

        private fun evaluateBinOp(expression: Expression): String{
            var res = ""
            var rightExpr = expression
            while (rightExpr is BinOp) {
                res += evaluate(rightExpr.lhs)
                res += " ${rightExpr.kind} "
                rightExpr = rightExpr.rhs
            }

            res += evaluate(rightExpr)
            return res
        }
    }
}
