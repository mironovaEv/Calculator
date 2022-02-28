package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val ids = binding.buttonGroup?.referencedIds
        if (ids != null) {
            for (id in ids) {
                val button = findViewById<Button>(id)
                button.setOnClickListener { setExpression("0") }
            }
        }


        binding.buttonMinus.setOnClickListener {
            if (binding.expression.text.isEmpty() || binding.expression.text[binding.expression.text.length - 1] in "0123456789") {
                setExpression("-")
            } else {
                binding.expression.text =
                    binding.expression.text.substring(0, binding.expression.text.length - 1) + "-"
            }
        }
        binding.buttonPlus.setOnClickListener {
            if (binding.expression.text.isNotEmpty()) {
                if (binding.expression.text[binding.expression.text.length - 1] in "0123456789") {
                    setExpression("+")
                } else {
                    binding.expression.text = binding.expression.text.substring(
                        0,
                        binding.expression.text.length - 1
                    ) + "+"
                }
            }
        }
        binding.buttonDivision.setOnClickListener {
            if (binding.expression.text.isNotEmpty()) {
                if (binding.expression.text[binding.expression.text.length - 1] in "0123456789") {
                    setExpression("/")
                } else if (binding.expression.text.isNotEmpty()) {
                    binding.expression.text = binding.expression.text.substring(
                        0,
                        binding.expression.text.length - 1
                    ) + "/"
                }
            }
        }
        binding.buttonMultiplication.setOnClickListener {
            if (binding.expression.text.isNotEmpty()) {
                if (binding.expression.text[binding.expression.text.length - 1] in "0123456789") {
                    setExpression("*")
                } else if (binding.expression.text.isNotEmpty()) {
                    binding.expression.text = binding.expression.text.substring(
                        0,
                        binding.expression.text.length - 1
                    ) + "*"
                }
            }
        }
        binding.buttonComma.setOnClickListener {
            if (binding.expression.text.isNotEmpty() && binding.expression.text[binding.expression.text.length - 1] in "0123456789") {
                setExpression(".")
            }
        }
        binding.buttonAC.setOnClickListener { binding.expression.text = "" }
        binding.buttonPlusMinus.setOnClickListener {
            var cnt = 0
            var expCopy = binding.expression.text.toString()
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
                binding.expression.text = expCopy
            }

        }
        binding.buttonPercent.setOnClickListener {
            if (binding.expression.text.isNotEmpty() && binding.expression.text[binding.expression.text.length - 1] in "0123456789") {
                var expCopy = binding.expression.text.toString()
                var ind = expCopy.length - 1
                while (expCopy[ind] in "0123456789." && ind != 0) {
                    ind--
                }
                if (ind == 0) {
                    val answer = expCopy.toDouble() / 100
                    binding.expression.text = answer.toString()
                } else if (expCopy[ind] in "+-" && expCopy[ind - 1] !in "/*") {
                    val strNum = expCopy.substring(ind + 1, expCopy.length)
                    val intNum = strNum.toDouble() / 100
                    val substringExpression =
                        ExpressionBuilder(expCopy.substring(0, ind)).build().evaluate() * intNum
                    expCopy = expCopy.substring(0, ind + 1) + substringExpression.toString()
                    binding.expression.text = expCopy
                } else if (expCopy[ind] in "/*" || expCopy[ind - 1] in "/*") {
                    if (expCopy[ind - 1] in "/*") {
                        ind--
                    }
                    val strNum = expCopy.substring(ind + 1, expCopy.length)
                    val intNum = strNum.toDouble() / 100
                    expCopy = expCopy.substring(0, ind + 1) + intNum.toString()
                    binding.expression.text = expCopy

                }

            }
        }




        binding.buttonEquals.setOnClickListener {
            try {
                val expr = ExpressionBuilder(binding.expression.text.toString()).build()
                val result = expr.evaluate()
                val longRes = result.toLong()
                if (result == longRes.toDouble()) {
                    if (result > 999999999) {

                        Toast.makeText(
                            this@MainActivity,
                            R.string.bigResult,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        binding.expression.text = longRes.toString()
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

                        binding.expression.text = answer
                    } else {
                        binding.expression.text = result.toString()
                    }
                }

            } catch (err: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.Error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }


    private fun setExpression(str: String) {
        binding.expression.append(str)

    }
}