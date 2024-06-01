package com.example.prayerrequest

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class PedidoDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${PedidoContract.PedidoEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${PedidoContract.PedidoEntry.COLUMN_NOME} TEXT," +
                    "${PedidoContract.PedidoEntry.COLUMN_PEDIDO} TEXT)"


        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${PedidoContract.PedidoEntry.TABLE_NAME}")
        onCreate(db)
    }

    fun inserirPedido(nome: String, pedido: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(PedidoContract.PedidoEntry.COLUMN_NOME, nome)
            put(PedidoContract.PedidoEntry.COLUMN_PEDIDO, pedido)
        }
        db.insert(PedidoContract.PedidoEntry.TABLE_NAME, null, values)
    }

    fun lerPedidos(): List<String> {
        val pedidos = mutableListOf<String>()
        val db = readableDatabase
        val projection = arrayOf(BaseColumns._ID, PedidoContract.PedidoEntry.COLUMN_NOME, PedidoContract.PedidoEntry.COLUMN_PEDIDO)

        val cursor = db.query(
            PedidoContract.PedidoEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val nome = getString(getColumnIndexOrThrow(PedidoContract.PedidoEntry.COLUMN_NOME))
                val pedido = getString(getColumnIndexOrThrow(PedidoContract.PedidoEntry.COLUMN_PEDIDO))
                pedidos.add("$nome - $pedido")
            }
        }

        return pedidos
    }

    fun atualizarPedido(id: Long, novoNome: String, novoPedido: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(PedidoContract.PedidoEntry.COLUMN_NOME, novoNome)
            put(PedidoContract.PedidoEntry.COLUMN_PEDIDO, novoPedido)
        }

        db.update(
            PedidoContract.PedidoEntry.TABLE_NAME,
            values,
            "${BaseColumns._ID}=?",
            arrayOf(id.toString())
        )
    }


    fun getPedidoIdFromPosition(position: Int): Long {
        val db = readableDatabase
        val cursor = db.query(
            PedidoContract.PedidoEntry.TABLE_NAME,
            arrayOf(BaseColumns._ID),
            null,
            null,
            null,
            null,
            null
        )

        cursor.moveToPosition(position)
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)) // Use BaseColumns._ID para o campo de ID padrão

        cursor.close()
        return id
    }

    fun deletePedido(id: Long) {
        val db = writableDatabase
        db.delete(PedidoContract.PedidoEntry.TABLE_NAME, "${BaseColumns._ID} = ?", arrayOf(id.toString()))
    }


    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Pedidos.db"
    }
}