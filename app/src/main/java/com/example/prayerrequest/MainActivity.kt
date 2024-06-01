package com.example.prayerrequest

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prayerrequest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbHelper: PedidoDbHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = PedidoDbHelper(this)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dbHelper.lerPedidos())
        binding.listViewPedidos.adapter = adapter

        binding.buttonSalvar.setOnClickListener {
            val nome = binding.editTextNome.text.toString()
            val pedido = binding.editTextPedido.text.toString()

            if (nome.isEmpty() || pedido.isEmpty()) {

                // Se algum campo estiver vazio
                Toast.makeText(this, "Ei, Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()

            }else{
                dbHelper.inserirPedido(nome, pedido)
                adapter.clear()
                adapter.addAll(dbHelper.lerPedidos())
                binding.editTextNome.text.clear()
                binding.editTextPedido.text.clear()
                Toast.makeText(this, "Pedido cadastrado com sucesso!", Toast.LENGTH_SHORT).show()

            }

        }

        binding.listViewPedidos.setOnItemClickListener { _, _, position, _ ->
            val id = dbHelper.getPedidoIdFromPosition(position)
            val selectedItem = adapter.getItem(position)
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("selectedItem", selectedItem)
            intent.putExtra("id", id) // Passa o ID do pedido para a EditActivity
            startActivityForResult(intent, 1)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.clear()
        adapter.addAll(dbHelper.lerPedidos())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            adapter.clear()
            adapter.addAll(dbHelper.lerPedidos())
        }
    }
}
