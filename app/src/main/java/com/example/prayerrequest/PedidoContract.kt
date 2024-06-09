package com.example.prayerrequest

import android.provider.BaseColumns

object PedidoContract {
    object PedidoEntry : BaseColumns {
        const val TABLE_NAME = "pedido"
        const val COLUMN_NOME = "nome"
        const val COLUMN_PEDIDO = "pedido"
        const val COLUMN_TIPO_PEDIDO = "tipoPedido"
        const val COLUMN_DETALHES = "detalhes"
    }
}