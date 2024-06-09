package com.example.prayerrequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prayerrequest.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private lateinit var dbHelper: PedidoDbHelper
    private var id: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = PedidoDbHelper(this)

        val selectedItem = intent.getStringExtra("selectedItem")
        id = intent.getLongExtra("id", -1L) // Recupera o ID do pedido
        val parts = selectedItem?.split(" - ")
        val nome = parts?.get(0)
        val tipoPedido = parts?.get(1)?.split(": ")?.get(0)
        val pedido = parts?.get(1)?.split(": ")?.get(1)?.split(" (Pedido: ")?.get(0)
        val detalhes = parts?.get(1)?.split(" (Pedido: ")?.get(1)?.replace(")", "")

        binding.editTextNomeEdit.setText(nome)
        binding.editTextPedidoEdit.setText(pedido)
        if (tipoPedido == "Oração") {
            binding.radioOracaoEdit.isChecked = true
        } else if (tipoPedido == "Jejum") {
            binding.radioJejumEdit.isChecked = true
        }
        binding.editTextDetalhesEdit.setText(detalhes)

        binding.buttonAtualizar.setOnClickListener {
            val novoNome = binding.editTextNomeEdit.text.toString()
            val novoPedido = binding.editTextPedidoEdit.text.toString()
            val novoTipoPedido = when {
                binding.radioOracaoEdit.isChecked -> "Oração"
                binding.radioJejumEdit.isChecked -> "Jejum"
                else -> ""
            }
            val novosDetalhes = binding.editTextDetalhesEdit.text.toString()

            if (novoNome.isEmpty() || novoPedido.isEmpty() || novoTipoPedido.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
            } else {
                if (id != -1L) {
                    dbHelper.atualizarPedido(id, novoNome, novoPedido, novoTipoPedido, novosDetalhes)
                    Toast.makeText(this, "Cadastro atualizado.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro ao atualizar pedido.", Toast.LENGTH_SHORT).show()
                }
                setResultAndFinish()
            }
        }

        binding.buttonExcluir.setOnClickListener {
            if (id != -1L) {
                dbHelper.deletePedido(id)
                Toast.makeText(this, "Pedido excluído.", Toast.LENGTH_SHORT).show()
                setResultAndFinish()
            } else {
                Toast.makeText(this, "Erro ao excluir pedido.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        setResultAndFinish()
    }

    private fun setResultAndFinish() {
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
