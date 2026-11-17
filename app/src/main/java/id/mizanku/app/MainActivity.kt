package id.mizanku.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Typeface
import android.view.Gravity
import android.widget.*
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import java.text.NumberFormat
import java.util.Locale

data class Product(val id: Long, val name: String, val price: Long)
class StoreDb(activity: Activity) : SQLiteOpenHelper(activity, "mizanku.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE products(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, price INTEGER NOT NULL)")
        db.execSQL("CREATE TABLE sales(id INTEGER PRIMARY KEY AUTOINCREMENT, total INTEGER NOT NULL, created_at INTEGER NOT NULL)")
        listOf("Kopi Susu Aren" to 18000L, "Es Teh Manis" to 5000L, "Nasi Goreng" to 25000L).forEach { (name, price) ->
            val values = ContentValues().apply { put("name", name); put("price", price) }
            db.insert("products", null, values)
        }
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun products(): List<Product> {
        val result = mutableListOf<Product>()
        readableDatabase.rawQuery("SELECT id,name,price FROM products ORDER BY name", null).use { c ->
            while (c.moveToNext()) result.add(Product(c.getLong(0), c.getString(1), c.getLong(2)))
        }
        return result
    }
    fun add(name: String, price: Long) {
        val values = ContentValues().apply { put("name", name); put("price", price) }
        writableDatabase.insert("products", null, values)
    }
    fun sale(total: Long) {
        val values = ContentValues().apply { put("total", total); put("created_at", System.currentTimeMillis()) }
        writableDatabase.insert("sales", null, values)
    }
}
class MainActivity : Activity() {
    private lateinit var db: StoreDb
    private val cart = linkedMapOf<Long, Pair<Product, Int>>()
    private lateinit var content: LinearLayout
    private val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); db = StoreDb(this); render() }
    private fun label(value: String, size: Float = 15f, bold: Boolean = false) = TextView(this).apply {
        text = value; textSize = size; setTextColor(0xff172033.toInt())
        if (bold) setTypeface(null, Typeface.BOLD)
        setPadding(8, 8, 8, 8)
    }
    private fun actionButton(value: String, action: () -> Unit) = Button(this).apply { text = value; setOnClickListener { action() } }
    private fun render() {
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(16,16,16,16); setBackgroundColor(0xfff5f7fb.toInt()) }
        root.addView(label("Mizanku",26f,true))
        root.addView(label("Kasir UMKM • Data tersimpan di perangkat",13f))
        val scroll = ScrollView(this)
        content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        scroll.addView(content); root.addView(scroll, LinearLayout.LayoutParams(-1,0,1f)); setContentView(root)
        content.addView(label("PRODUK",13f,true))
        db.products().forEach { p ->
            val row = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL; setPadding(4,4,4,4) }
            val info = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
            info.addView(label(p.name,16f,true)); info.addView(label(rupiah.format(p.price),13f))
            row.addView(info,LinearLayout.LayoutParams(0,-2,1f))
            row.addView(actionButton("Tambah") { val q = cart[p.id]?.second ?: 0; cart[p.id] = p to q+1; render() })
            content.addView(row)
        }
        content.addView(label("KERANJANG",16f,true))
        if (cart.isEmpty()) content.addView(label("Belum ada barang dipilih",13f))
        cart.values.forEach { (p,q) -> content.addView(label(p.name + " × " + q + " — " + rupiah.format(p.price*q),14f)) }
        val total = cart.values.sumOf { it.first.price * it.second }
        content.addView(label("TOTAL  " + rupiah.format(total),20f,true))
        content.addView(actionButton("Selesaikan Transaksi") {
            if (total > 0) { db.sale(total); cart.clear(); Toast.makeText(this,"Transaksi tersimpan",Toast.LENGTH_SHORT).show(); render() }
            else Toast.makeText(this,"Keranjang masih kosong",Toast.LENGTH_SHORT).show()
        })
        content.addView(actionButton("Tambah Produk") { addProductDialog() })
        content.addView(label("Prototipe awal 0.1.0 — fitur lanjutan sedang dikembangkan",11f))
    }
    private fun addProductDialog() {
        val form = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(24,8,24,8) }
        val name = EditText(this).apply { hint = "Nama produk" }
        val price = EditText(this).apply { hint = "Harga (angka)"; inputType = 2 }
        form.addView(name); form.addView(price)
        android.app.AlertDialog.Builder(this).setTitle("Tambah Produk").setView(form)
            .setNegativeButton("Batal",null).setPositiveButton("Simpan") { _,_ ->
                val n = name.text.toString().trim(); val p = price.text.toString().toLongOrNull()
                if (n.isNotEmpty() && p != null && p > 0) { db.add(n,p); render() }
                else Toast.makeText(this,"Nama dan harga harus diisi dengan benar",Toast.LENGTH_SHORT).show()
            }.show()
    }
}
