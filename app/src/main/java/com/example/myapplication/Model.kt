package com.example.myapplication


import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception


class Model {

    private var expressionStr: String = ""
    private fun setExpression(str: String): String {
        expressionStr += str
        return expressionStr
    }

    fun clickAC(): String {
        expressionStr = ""
        return expressionStr
    }

    fun clickNumbers(symbol: String): String {
        return setExpression(symbol)
    }

    fun clickMinus(): String {
        return if (expressionStr.isEmpty() || expressionStr[expressionStr.length - 1] in "0123456789") {
            setExpression("-")
        } else {
            expressionStr =
                expressionStr.substring(0, expressionStr.length - 1) + "-"
            expressionStr
        }
    }

    fun clickPlus(): String {
        if (expressionStr.isNotEmpty()) {
            return if (expressionStr[expressionStr.length - 1] in "0123456789") {
                setExpression("+")
            } else {
                expressionStr = expressionStr.substring(0, expressionStr.length - 1) + "+"
                expressionStr
            }
        }
        return ""
    }

    fun clickDivision(): String {
        if (expressionStr.isNotEmpty()) {
            if (expressionStr[expressionStr.length - 1] in "0123456789") {
                return setExpression("/")
            } else if (expressionStr.isNotEmpty()) {
                expressionStr = expressionStr.substring(0, expressionStr.length - 1) + "/"
                return expressionStr
            }
        }
        return ""
    }

    fun clickMultiplication(): String {
        if (expressionStr.isNotEmpty()) {
            if (expressionStr[expressionStr.length - 1] in "0123456789") {
                return setExpression("*")
            } else if (expressionStr.isNotEmpty()) {
                expressionStr = expressionStr.substring(0, expressionStr.length - 1) + "*"
                return expressionStr
            }
        }
        return ""
    }

    fun clickComma(): String {
        val startExpression = expressionStr
        if (expressionStr.isNotEmpty() && expressionStr[expressionStr.length - 1] in "0123456789") {
            return setExpression(".")
        }
        return startExpression
    }

    fun clickPlusMinus(): String {
        val startExpression = expressionStr
        var cnt = 0
        var expCopy = expressionStr
        if (expCopy.isNotEmpty()) {
            var ind = (expCopy.length) - 1
            while (cnt < expCopy.length && expCopy[ind] in "0123456789.") {
                cnt++
                ind--
            }
            if (cnt > 0) {
                if (cnt == expCopy.length) {
                    expCopy = "-$expCopy"
                } else {
                    when {
                        expCopy[expCopy.length - cnt - 1] == '-' -> {
                            expCopy = when {
                                cnt == expCopy.length - 1 -> {
                                    expCopy.substring(1, expCopy.length)
                                }
                                expCopy[expCopy.length - cnt - 2] in "*/" -> {
                                    expCopy.substring(0, expCopy.length - cnt - 1) +
                                            expCopy.substring(
                                                expCopy.length - cnt,
                                                expCopy.length
                                            )
                                }
                                else -> {
                                    val numbers =
                                        expCopy.substring(expCopy.length - cnt, expCopy.length)
                                    val remainder =
                                        expCopy.substring(0, expCopy.length - cnt - 1)
                                    "$remainder+$numbers"
                                }
                            }
                        }
                        expCopy[expCopy.length - cnt - 1] == '+' -> {
                            val numbers =
                                expCopy.substring(expCopy.length - cnt, expCopy.length)
                            val remainder = expCopy.substring(0, expCopy.length - cnt - 1)
                            expCopy = "$remainder-$numbers"
                        }
                        else -> {
                            val numbers =
                                expCopy.substring(expCopy.length - cnt, expCopy.length)
                            val remainder = expCopy.substring(0, expCopy.length - cnt)
                            expCopy = "$remainder-$numbers"

                        }
                    }
                }
            }
            expressionStr = expCopy
            return expressionStr
        }
        return startExpression
    }

    fun clickPercent(): String {
        val startExpression = expressionStr
        if (expressionStr.isNotEmpty() && expressionStr[expressionStr.length - 1] in "0123456789") {
            var expCopy = expressionStr
            var ind = expCopy.length - 1
            while (expCopy[ind] in "0123456789." && ind != 0) {
                ind--
            }
            if (ind == 0) {
                val answer = expCopy.toDouble() / 100
                expressionStr = answer.toString()
                return expressionStr
            } else if (expCopy[ind] in "+-" && expCopy[ind - 1] !in "/*") {
                val strNum = expCopy.substring(ind + 1, expCopy.length)
                val intNum = strNum.toDouble() / 100
                val substringExpression =
                    ExpressionBuilder(expCopy.substring(0, ind)).build().evaluate() * intNum
                expCopy = expCopy.substring(0, ind + 1) + substringExpression.toString()
                expressionStr = expCopy
                return expressionStr
            } else if (expCopy[ind] in "/*" || expCopy[ind - 1] in "/*") {
                if (expCopy[ind - 1] in "/*") {
                    ind--
                }
                val strNum = expCopy.substring(ind + 1, expCopy.length)
                val intNum = strNum.toDouble() / 100
                expCopy = expCopy.substring(0, ind + 1) + intNum.toString()
                expressionStr = expCopy
                return expressionStr

            }

        }
        return startExpression

    }

    fun clickEquals(): String {
        try {
            val expr = ExpressionBuilder(expressionStr).build()
            val result = expr.evaluate()
            val longRes = result.toLong()
            if (result == longRes.toDouble()) {
                return if (result > 999999999) {
                    "bigResult"
                } else {
                    expressionStr = longRes.toString()
                    expressionStr
                }

            } else {
                if (result.toString().length > 11) {
                    val res = result.toString()
                    var counter = 0
                    while (res[counter] != '.') {
                        counter++
                    }
                    var answer: String
                    when (counter) {
                        1 -> {
                            answer = String.format("%.8f", result)
                        }
                        2 -> {
                            answer = String.format("%.7f", result)
                        }
                        3 -> {
                            answer = String.format("%.6f", result)
                        }
                        4 -> {
                            answer = String.format("%.5f", result)
                        }
                        5 -> {
                            answer = String.format("%.4f", result)
                        }
                        6 -> {
                            answer = String.format("%.3f", result)
                        }
                        7 -> {
                            answer = String.format("%.2f", result)
                        }
                        8 -> {
                            answer = String.format("%.1f", result)
                        }
                        else -> {
                            answer = String.format("%.0f", result)
                        }
                    }
                    answer =
                        answer.replace(oldChar = ',', newChar = '.', ignoreCase = false)

                    expressionStr = answer
                    return expressionStr
                } else {
                    expressionStr = result.toString()
                    return expressionStr
                }
            }

        } catch (err: Exception) {
            return "Error"

        }
    }


}