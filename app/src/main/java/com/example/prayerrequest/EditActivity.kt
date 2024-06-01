package com.example.prayerrequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.prayerrequest.databinding.ActivityEditBinding
import android.util.Log
import android.widget.Toast

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
        val id = intent.getLongExtra("id", -1L) // Recupera o ID do pedido
        val parts = selectedItem?.split(" - ")
        val nome = parts?.get(0)
        val pedido = parts?.get(1)

        binding.editTextNomeEdit.setText(nome)
        binding.editTextPedidoEdit.setText(pedido)


        binding.buttonAtualizar.setOnClickListener {
            val novoNome = binding.editTextNomeEdit.text.toString()
            val novoPedido = binding.editTextPedidoEdit.text.toString()

            if (novoNome.isEmpty() || novoPedido.isEmpty()) {
                // Se algum campo estiver vazio
                Toast.makeText(this, "Ei, Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()

            } else {
                if (id != -1L) {
                    dbHelper.atualizarPedido(id, novoNome, novoPedido)
                    Log.d("EditActivity", "Pedido atualizado no banco de dados")
                } else {
                    Log.e("EditActivity", "ID inválido: $id")
                }
                Toast.makeText(this, "Cadastro atualizado.", Toast.LENGTH_SHORT).show()
            }

            setResultAndFinish()
        }


        binding.buttonExcluir.setOnClickListener {
            if (id != -1L) {
                dbHelper.deletePedido(id)
                Log.d("EditActivity", "Pedido excluído do banco de dados")
                Toast.makeText(this, "Pedido excluído.", Toast.LENGTH_SHORT).show()
                setResultAndFinish()
            } else {
                Log.e("EditActivity", "ID inválido para exclusão: $id")
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