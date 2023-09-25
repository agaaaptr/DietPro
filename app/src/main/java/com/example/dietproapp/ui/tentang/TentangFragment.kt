package com.example.dietproapp.ui.tentang

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dietproapp.databinding.FragmentTentangBinding


class TentangFragment : Fragment() {

    private lateinit var tentangViewModel: TentangViewModel
    private var _binding: FragmentTentangBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val statusBarColor = ContextCompat.getColor(requireContext(), com.example.dietproapp.R.color.background)
        activity?.window?.statusBarColor = statusBarColor

        val lightStatusIcons = true // Set this to true for light icons, false for dark icons

        val orderViewModel =
            ViewModelProvider(this).get(TentangViewModel::class.java)

        _binding = FragmentTentangBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Menambahkan event listener untuk tombol WhatsApp
        mainButton()
        return root
    }

    private fun mainButton() {
        binding.btnUrl.setOnClickListener {
            openWhatsAppWithAdmin()
        }
    }

    private fun openWhatsAppWithAdmin() {
        try {
            // Nomor WhatsApp admin yang akan dihubungi
            val adminPhoneNumber = "+6288293213968" // Ganti dengan nomor WhatsApp admin yang sesuai
            val contactNumber = "Admin"
            val message = "Halo ${contactNumber}!, saya membutuhkan bantuan."

            // Membuat URI untuk memulai WhatsApp
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=${adminPhoneNumber}&text=${message}")

            // Membuat Intent untuk membuka WhatsApp
            val intent = Intent(Intent.ACTION_VIEW, uri)

            // Memulai aplikasi WhatsApp
            startActivity(intent)
        } catch (e: Exception) {
            // Handle jika terjadi kesalahan
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
