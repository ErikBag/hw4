package engine

import ast.*
import java.util.Collections.emptyList


class Interpreter {
    fun step(state: State): List<State> {
        val nextState = state.nextStates.firstOrNull() ?: return emptyList()
        state.nextStates = if (state.nextStates.isNotEmpty()) state.nextStates.drop(1) else state.nextStates
        when (nextState) {
            is IfStmt -> {
                val condition = Evaluator(state.memory).evaluate(nextState.condition)
                val thenState = State(
                    state.memory.copy(),
                    nextState.thenBlock + state.nextStates,
                    state.pc + listOf(condition),
                    null
                )
                val elseState = State(
                    state.memory.copy(),
                    nextState.elseBlock + state.nextStates,
                    state.pc + listOf(UnOp(UnOpKind.UO_Neg, condition, Type.T_BOOL)),
                    null
                )
                return listOf(thenState, elseState)
            }
            is Assignment -> {
                val expr = Evaluator(state.memory).evaluate(nextState.value)
                state.memory.put(nextState.name, expr)
                return listOf(state)
            }
            is ReturnStmt -> {
                val expr = Evaluator(state.memory).evaluate(nextState.returnExpr)
                state.res = expr
                return listOf(state)
            }
            else -> return emptyList()
        }
    }

    private class Evaluator(val memory: MyMemory) {
        fun evaluate(expr: Expression): Expression {
            return when (expr) {
                is BinOp -> return BinOp(
                    expr.kind,
                    evaluate(expr.lhs),
                    evaluate(expr.rhs),
                    expr.type
                )
                is UnOp -> return UnOp(
                    expr.kind,
                    evaluate(expr.subExpr),
                    expr.type
                )
                is VarRef -> memory.get(expr.identifier)
                is ErrorExpression -> throw RuntimeException("ErrorExpression")
                else -> return expr
            }
        }
    }
}
