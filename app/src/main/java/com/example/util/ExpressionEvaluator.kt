package com.example.util

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.*

object ExpressionEvaluator {

    fun formatNumber(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        val rounded = (round(value * 100000000.0) / 100000000.0)
        return try {
            val bd = BigDecimal(rounded.toString()).stripTrailingZeros()
            bd.toPlainString()
        } catch (_: Exception) {
            rounded.toString().removeSuffix(".0")
        }
    }

    /**
     * Evaluates standard arithmetic expressions e.g. "12 + 34 × 2"
     */
    fun evaluateStandard(expression: String): Result<Double> {
        val sanitized = expression
            .replace('×', '*')
            .replace('÷', '/')
            .trim()

        return evaluateExpression(sanitized, isRadMode = true)
    }

    /**
     * Evaluates scientific expressions including sin, cos, tan, sinh, cosh, tanh, lg, sqrt, 1/, pi, e
     */
    fun evaluateScientific(expression: String, isRadMode: Boolean): Result<Double> {
        var sanitized = expression
            .replace('×', '*')
            .replace('÷', '/')
            .replace("π", "PI")
            .replace("√", "sqrt")
            .replace("lg(", "log10(")

        // Auto close unclosed parentheses
        val openCount = sanitized.count { it == '(' }
        val closeCount = sanitized.count { it == ')' }
        if (openCount > closeCount) {
            sanitized += ")".repeat(openCount - closeCount)
        }

        return evaluateExpression(sanitized, isRadMode)
    }

    private fun evaluateExpression(expr: String, isRadMode: Boolean): Result<Double> {
        return try {
            val parser = MathParser(expr, isRadMode)
            val result = parser.parse()
            if (result.isNaN() || result.isInfinite()) {
                Result.failure(ArithmeticException("Math error"))
            } else {
                Result.success(result)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private class MathParser(private val str: String, private val isRadMode: Boolean) {
        private var pos = -1
        private var ch = 0

        private fun nextChar() {
            ch = if (++pos < str.length) str[pos].code else -1
        }

        private fun eat(charToEat: Int): Boolean {
            while (ch == ' '.code) nextChar()
            if (ch == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        fun parse(): Double {
            nextChar()
            val x = parseExpression()
            if (pos < str.length) throw RuntimeException("Unexpected: " + ch.toChar())
            return x
        }

        // Grammar:
        // expression = term | expression `+` term | expression `-` term
        // term = factor | term `*` factor | term `/` factor | term `%` factor
        // factor = `+` factor | `-` factor | `(` expression `)` | number
        //        | functionName factor | factor `^` factor
        private fun parseExpression(): Double {
            var x = parseTerm()
            while (true) {
                when {
                    eat('+'.code) -> x += parseTerm()
                    eat('-'.code) -> x -= parseTerm()
                    else -> return x
                }
            }
        }

        private fun parseTerm(): Double {
            var x = parseFactor()
            while (true) {
                when {
                    eat('*'.code) -> x *= parseFactor()
                    eat('/'.code) -> {
                        val divisor = parseFactor()
                        if (divisor == 0.0) throw ArithmeticException("Divide by zero")
                        x /= divisor
                    }
                    eat('%'.code) -> {
                        val divisor = parseFactor()
                        x = if (divisor != 0.0) x % divisor else x / 100.0
                    }
                    else -> return x
                }
            }
        }

        private fun parseFactor(): Double {
            if (eat('+'.code)) return +parseFactor()
            if (eat('-'.code)) return -parseFactor()

            var x: Double
            val startPos = pos
            if (eat('('.code)) {
                x = parseExpression()
                eat(')'.code)
            } else if ((ch in '0'.code..'9'.code) || ch == '.'.code) {
                while ((ch in '0'.code..'9'.code) || ch == '.'.code) nextChar()
                x = str.substring(startPos, pos).toDouble()
            } else if (ch in 'a'.code..'z'.code || ch in 'A'.code..'Z'.code) {
                while (ch in 'a'.code..'z'.code || ch in 'A'.code..'Z'.code || (ch in '0'.code..'9'.code)) nextChar()
                val func = str.substring(startPos, pos)
                if (eat('('.code)) {
                    val arg = parseExpression()
                    eat(')'.code)
                    x = applyFunction(func, arg)
                } else {
                    x = when (func.uppercase()) {
                        "PI" -> Math.PI
                        "E" -> Math.E
                        else -> throw RuntimeException("Unknown identifier: $func")
                    }
                }
            } else {
                throw RuntimeException("Unexpected: " + if (ch != -1) ch.toChar() else "EOF")
            }

            return x
        }

        private fun applyFunction(func: String, arg: Double): Double {
            return when (func.lowercase()) {
                "sin" -> if (isRadMode) sin(arg) else sin(Math.toRadians(arg))
                "cos" -> if (isRadMode) cos(arg) else cos(Math.toRadians(arg))
                "tan" -> if (isRadMode) tan(arg) else tan(Math.toRadians(arg))
                "sinh" -> sinh(arg)
                "cosh" -> cosh(arg)
                "tanh" -> tanh(arg)
                "sqrt" -> {
                    if (arg < 0) throw ArithmeticException("Negative sqrt")
                    sqrt(arg)
                }
                "log10" -> {
                    if (arg <= 0) throw ArithmeticException("Non-positive log")
                    log10(arg)
                }
                "inv", "1/" -> {
                    if (arg == 0.0) throw ArithmeticException("Divide by zero")
                    1.0 / arg
                }
                else -> throw RuntimeException("Unknown function: $func")
            }
        }
    }
}
