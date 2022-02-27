package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception
import java.lang.reflect.Executable
import kotlin.math.exp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonNull.setOnClickListener { setExpression("0") }
        button1.setOnClickListener { setExpression("1") }
        button2.setOnClickListener { setExpression("2") }
        button3.setOnClickListener { setExpression("3") }
        button4.setOnClickListener { setExpression("4") }
        button5.setOnClickListener { setExpression("5") }
        button6.setOnClickListener { setExpression("6") }
        button7.setOnClickListener { setExpression("7") }
        button8.setOnClickListener { setExpression("8") }
        button9.setOnClickListener { setExpression("9") }
        buttonMinus.setOnClickListener {
            if (expression.text.length == 0 || expression.text[expression.text.length - 1] in "0123456789") {
                setExpression("-")
            } else {
                expression.text = expression.text.substring(0, expression.text.length - 1) + "-"
            }
        }
        buttonPlus.setOnClickListener {
            if (expression.text.length != 0) {
                if (expression.text[expression.text.length - 1] in "0123456789") {
                    setExpression("+")
                } else {
                    expression.text = expression.text.substring(0, expression.text.length - 1) + "+"
                }
            }
        }
        buttonDiv.setOnClickListener {
            if (expression.text.length != 0) {
                if (expression.text[expression.text.length - 1] in "0123456789") {
                    setExpression("/")
                } else if (expression.text.length != 0) {
                    expression.text = expression.text.substring(0, expression.text.length - 1) + "/"
                }
            }
        }
        buttonMult.setOnClickListener {
            if (expression.text.length != 0) {
                if (expression.text[expression.text.length - 1] in "0123456789") {
                    setExpression("*")
                } else if (expression.text.length != 0) {
                    expression.text = expression.text.substring(0, expression.text.length - 1) + "*"
                }
            }
        }
        buttonComma.setOnClickListener {
            if (expression.text.length != 0 && expression.text[expression.text.length - 1] in "0123456789") {
                setExpression(".")
            }
        }
        buttonAC.setOnClickListener { expression.text = "" }
        buttonPL_MN.setOnClickListener {
            var cnt = 0
            var expCopy = expression.text.toString()
            if (expCopy.length > 0) {
                var i = (expCopy.length) - 1
                while (cnt < expCopy.length && expCopy[i] in "0123456789.") {
                    cnt++
                    i--
                }
                if (cnt > 0) {
                    if (cnt == expCopy.length) {
                        expCopy = "-" + expCopy
                    } else {
                        if (expCopy[expCopy.length - cnt - 1] == '-') {
                            if (cnt == expCopy.length - 1) {
                                expCopy = expCopy.substring(1, expCopy.length)
                            } else if (expCopy[expCopy.length - cnt - 2] in "*/") {
                                expCopy = expCopy.substring(0, expCopy.length - cnt - 1) +
                                        expCopy.substring(expCopy.length - cnt, expCopy.length)
                            } else {
                                val numbers =
                                    expCopy.substring(expCopy.length - cnt, expCopy.length)
                                val remainder = expCopy.substring(0, expCopy.length - cnt - 1)
                                expCopy = remainder + "+" + numbers
                            }
                        } else if (expCopy[expCopy.length - cnt - 1] == '+') {
                            val numbers =
                                expCopy.substring(expCopy.length - cnt, expCopy.length)
                            val remainder = expCopy.substring(0, expCopy.length - cnt - 1)
                            expCopy = remainder + "-" + numbers
                        } else {
                            val numbers =
                                expCopy.substring(expCopy.length - cnt, expCopy.length)
                            val remainder = expCopy.substring(0, expCopy.length - cnt)
                            expCopy = remainder + "-" + numbers

                        }
                    }
                }
                expression.text = expCopy
            }

        }
        buttonPer.setOnClickListener {
            if (expression.text[expression.text.length - 1] in "0123456789") {
                var expCopy = expression.text.toString()
                var ind = expCopy.length - 1
                while (expCopy[ind] in "0123456789." && ind != 0) {
                    ind--
                }
                if (ind == 0) {
                    val answer = expCopy.toDouble() / 100
                    expression.text = answer.toString()
                } else if (expCopy[ind] in "+-" && expCopy[ind - 1] !in "/*") {
                    val strNum = expCopy.substring(ind + 1, expCopy.length)
                    val intNum = strNum.toDouble() / 100
                    val substrExp =
                        ExpressionBuilder(expCopy.substring(0, ind)).build().evaluate() * intNum
                    expCopy = expCopy.substring(0, ind + 1) + substrExp.toString()
                    expression.text = expCopy.toString()
                } else if (expCopy[ind] in "/*" || expCopy[ind - 1] in "/*") {
                    if (expCopy[ind - 1] in "/*") {
                        ind--
                    }
                    val strNum = expCopy.substring(ind + 1, expCopy.length)
                    val intNum = strNum.toDouble() / 100
                    expCopy = expCopy.substring(0, ind + 1) + intNum.toString()
                    expression.text = expCopy.toString()

                }

            }
        }




        buttonEquals.setOnClickListener {
            try {
                val exp = ExpressionBuilder(expression.text.toString()).build()
                val result = exp.evaluate()
                val longRes = result.toLong()
                if (result == longRes.toDouble()) {
                    if (result > 999999999) {
                        Toast.makeText(
                            this@MainActivity,
                            "Результат слишком большой",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        expression.text = longRes.toString()
                    }

                } else {
                    if (result.toString().length > 11) {
                        val res = result.toString()
                        var counter = 0
                        while (res[counter] != '.') {
                            counter++
                        }
                        var answer: String
                        if (counter == 1) {
                            answer = String.format("%.8f", result)
                        } else if (counter == 2) {
                            answer = String.format("%.7f", result)
                        } else if (counter == 3) {
                            answer = String.format("%.6f", result)
                        } else if (counter == 4) {
                            answer = String.format("%.5f", result)
                        } else if (counter == 5) {
                            answer = String.format("%.4f", result)
                        } else if (counter == 6) {
                            answer = String.format("%.3f", result)
                        } else if (counter == 7) {
                            answer = String.format("%.2f", result)
                        } else if (counter == 8) {
                            answer = String.format("%.1f", result)
                        } else {
                            answer = String.format("%.0f", result)
                        }
                        answer =
                            answer.replace(oldChar = ',', newChar = '.', ignoreCase = false)

                        expression.text = answer.toString()
                    } else {
                        expression.text = result.toString()
                    }
                }

            } catch (err: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Введено некорректное математическое выражение",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }


    fun setExpression(str: String) {
        expression.append(str)

    }
}