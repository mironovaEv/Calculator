package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val data = Model()
        val ids = binding.buttonGroup.referencedIds
        if (ids != null) {
            for (id in ids) {
                val button = findViewById<Button>(id)
                button.setOnClickListener {
                    binding.expression.text =
                        data.clickNumbers(button.text[button.text.length - 1].toString())
                }
            }
        }

        binding.buttonMinus.setOnClickListener {
            binding.expression.text = data.clickMinus()
        }
        binding.buttonPlus.setOnClickListener {
            binding.expression.text = data.clickPlus()
        }
        binding.buttonDivision.setOnClickListener {
            binding.expression.text = data.clickDivision()
        }
        binding.buttonMultiplication.setOnClickListener {
            binding.expression.text = data.clickMultiplication()
        }
        binding.buttonComma.setOnClickListener {
            binding.expression.text = data.clickComma()
        }
        binding.buttonAC.setOnClickListener {
            binding.expression.text = data.clickAC()
        }
        binding.buttonPlusMinus.setOnClickListener {
            binding.expression.text = data.clickPlusMinus()
        }
        binding.buttonPercent.setOnClickListener {
            binding.expression.text = data.clickPercent()
        }

        binding.buttonEquals.setOnClickListener {
            when (val result = data.clickEquals()) {
                "bigResult" -> {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.bigResult,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                "Error" -> {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.Error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    binding.expression.text = result
                }
            }

        }


    }


}