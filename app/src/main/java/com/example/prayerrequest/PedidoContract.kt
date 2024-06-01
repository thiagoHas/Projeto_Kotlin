package com.example.prayerrequest

import android.provider.BaseColumns

object PedidoContract {
    // Definição do esquema da tabela
    object PedidoEntry : BaseColumns {
        const val TABLE_NAME = "pedidos"
        const val COLUMN_NOME = "nome"
        const val COLUMN_PEDIDO = "pedido"
        const val COLUMN_CATEGORIA = "categoria"
    }
}