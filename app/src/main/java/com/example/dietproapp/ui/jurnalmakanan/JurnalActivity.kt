package com.example.dietproapp.ui.jurnalmakanan

import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dietproapp.NavigasiActivity
import com.example.dietproapp.R
import com.example.dietproapp.core.data.source.model.Makanan
import com.example.dietproapp.core.data.source.remote.network.State
import com.example.dietproapp.databinding.ActivityJurnalBinding
import com.example.dietproapp.ui.base.MyActivity
import com.example.dietproapp.ui.jurnalmakanan.adapter.MenuJurnalAdapter
import com.example.dietproapp.ui.statistik.StatistikFragment
import com.example.dietproapp.util.SPrefs
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.inyongtisto.myhelper.extension.intentActivity
import com.inyongtisto.myhelper.extension.isNull
import com.inyongtisto.myhelper.extension.pushActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class JurnalActivity : MyActivity() {

    private var _binding: ActivityJurnalBinding? = null

    private val binding get() = _binding!!

    private lateinit var listJurnalMakanan: RecyclerView
    private lateinit var searchView: SearchView

    private val viewModel: JurnalMenuViewModel by viewModel()

    private val adapter = MenuJurnalAdapter()

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityJurnalBinding.inflate(layoutInflater)
        setContentView(binding.root)


        searchView = binding.searchView

//        swipeRefreshLayout = binding.swipeRefreshLayout
//        swipeRefreshLayout.setOnRefreshListener {
//            // Panggil fungsi untuk melakukan refresh data di sini
//            refreshData()
//        }

        setupSearchView()
        setupAdapter()
        getData()
        mainButton()
    }

//    private fun refreshData() {
//        getData()
//        swipeRefreshLayout.isRefreshing = false
//    }

    private fun mainButton(){
        binding.fabSimpan.setOnClickListener {
           store()
        }

        binding.imgBack.setOnClickListener{
            intentActivity(NavigasiActivity::class.java)
        }
    }

    private fun setupAdapter() {
        listJurnalMakanan = binding.rvListJurnalmakanan
        listJurnalMakanan.layoutManager = LinearLayoutManager(this)
        listJurnalMakanan.adapter = adapter
    }

    private fun getData() {
        viewModel.getmenuJurnal().observe(this) {
            when (it.state) {
                State.SUCCESS -> {
                    val data: List<Makanan> = it.data ?: emptyList()
                    adapter.addItems(data)
                    binding.tvError.isVisible = data.isNull()
                }
                State.ERROR -> {
                    binding.tvError.isVisible = true
                }
                State.LOADING -> {
                    // Tindakan yang perlu dilakukan saat loading
                }
            }
        }
    }

    private fun store() {
        val user = SPrefs.getUser() // Ambil model User dari SPrefs
        val idUser = user?.id

        val selectedFoodIds = mutableListOf<Int>()

        // Loop melalui setiap checkbox untuk memeriksa apakah dicentang
        for ((foodId, isChecked) in adapter.checkedItems) {
            if (isChecked) {
                selectedFoodIds.add(foodId)
            }
        }

        // Buat JSON Object yang berisi data yang akan dikirimkan ke server
        val requestJson = JsonObject()
        val idMakananArray = JsonArray()
        selectedFoodIds.forEach { idMakananArray.add(it) }
        requestJson.add("id_makanan", idMakananArray)


        viewModel.store(idUser,requestJson).observe(this) {
            when (it.state) {
                State.SUCCESS -> {
                    val message = "Data telah disimpan."
                    showToast(message)
//
//                    // Pindah ke StatistikFragment
//                    val fragment = StatistikFragment()
//                    val transaction = supportFragmentManager.beginTransaction()
//                    transaction.replace(R.id., fragment)
//                    transaction.commit()
                    pushActivity(NavigasiActivity::class.java)

                }
                State.ERROR -> {
                    val message = "gagal menambahkan data."
                    showToast(message)
                }
                State.LOADING -> {
                    // Tindakan yang perlu dilakukan saat loading
                }
            }
        }
    }
    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
