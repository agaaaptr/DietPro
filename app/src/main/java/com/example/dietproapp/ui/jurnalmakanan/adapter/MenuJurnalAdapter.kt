package com.example.dietproapp.ui.jurnalmakanan.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.dietproapp.core.data.source.model.Makanan
import com.example.dietproapp.databinding.ListJurnalBinding
import java.util.Locale

@SuppressLint("NotifyDataSetChanged")
class MenuJurnalAdapter : RecyclerView.Adapter<MenuJurnalAdapter.ViewHolder>(), Filterable {

    var data = ArrayList<Makanan>()
    private var filteredData = ArrayList<Makanan>()
    val checkedItems = mutableMapOf<Int, Boolean>()
    private var originalData = ArrayList<Makanan>()
    val jumlahMakananMap = mutableMapOf<Int, String>()


    inner class ViewHolder(val itemBinding: ListJurnalBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        var jumlahMakanan: String = ""
        fun bind(item: Makanan, position: Int) {
            itemBinding.apply {
                item.id
                namaBahan.text = item.Nama_Bahan
                EnKkal.text = item.Energi_kkal
                protein.text = item.Protein_g
                lemak.text = item.Lemak_g
                Kh.text = item.KH_g
                serat.text = item.Serat_Total_g
                natrium.text = item.Natrium_mg
                kalium.text = item.Kalium_mg
            }
        }
    }

    fun addItems(items: List<Makanan>) {
        data.clear()
        data.addAll(items)
        originalData.addAll(items) // Isi originalData dengan data asli

        // Inisialisasi checkedItems untuk semua item dalam originalData sebagai unchecked
        for (item in originalData) {
            checkedItems[item.id] = false
        }

        notifyDataSetChanged()
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString().toLowerCase(Locale.getDefault())
                filteredData.clear()

                if (charSearch.isEmpty()) {
                    filteredData.addAll(originalData) // Gunakan originalData saat tidak ada kata kunci
                } else {
                    for (item in originalData) {
                        if (item.Nama_Bahan?.toLowerCase(Locale.getDefault())?.contains(charSearch) == true) {
                            filteredData.add(item)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredData
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredData = results?.values as ArrayList<Makanan>
                notifyDataSetChanged()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            ListJurnalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        // Tambahkan listener untuk checkbox di sini
        viewHolder.itemBinding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            val position = viewHolder.adapterPosition
            checkedItems[position] = isChecked
        }

        return viewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val makanan = filteredData[position] // Gunakan filteredData
        holder.bind(makanan, position)

        val energiKkal = makanan.Energi_kkal
        val foodId = makanan.id

        holder.itemBinding.checkbox.isChecked = checkedItems[foodId] ?: false

        if (energiKkal != null) {
            holder.itemBinding.EnKkal.text = energiKkal
        } else {
            holder.itemBinding.EnKkal.text = "N/A"
        }

        holder.itemBinding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            checkedItems[foodId] = isChecked
        }

       val edtGram = holder.itemBinding.edtGram.text.toString()

    if (edtGram.isEmpty()) {
        // Atur nilai default ke 200 jika edtGram kosong
        holder.itemBinding.edtGram.text = Editable.Factory.getInstance().newEditable("200")
        jumlahMakananMap[foodId] = "200"
    } else {
        jumlahMakananMap[foodId] = edtGram
    }

    holder.itemBinding.edtGram.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val jumlahMakanan = s.toString()
            jumlahMakananMap[foodId] = jumlahMakanan
        }

        override fun afterTextChanged(s: Editable?) {}
    })
    }



    override fun getItemCount(): Int {
        return filteredData.size
    }
}
