package com.example.lifecycledemo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etNamaPemilik: EditText
    private lateinit var etEmail: EditText
    private lateinit var etTahunMotor: EditText
    private lateinit var etOdometer: EditText
    private lateinit var etOdoOliTerakhir: EditText

    private lateinit var btnTypeMatic: TextView
    private lateinit var btnTypeManual: TextView
    private lateinit var btnDiagnosa: Button
    private lateinit var btnReset: Button
    private lateinit var btnDemo: Button

    private var isMaticSelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        etNamaPemilik = findViewById(R.id.etNamaPemilik)
        etEmail = findViewById(R.id.etEmail)
        etTahunMotor = findViewById(R.id.etTahunMotor)
        etOdometer = findViewById(R.id.etOdometer)
        etOdoOliTerakhir = findViewById(R.id.etOdoOliTerakhir)

        btnTypeMatic = findViewById(R.id.btnTypeMatic)
        btnTypeManual = findViewById(R.id.btnTypeManual)
        btnDiagnosa = findViewById(R.id.btnDiagnosa)
        btnReset = findViewById(R.id.btnReset)
        btnDemo = findViewById(R.id.btnDemo)
    }

    private fun setupListeners() {
        btnTypeMatic.setOnClickListener {
            isMaticSelected = true
            updateTypeSelectorUI()
        }

        btnTypeManual.setOnClickListener {
            isMaticSelected = false
            updateTypeSelectorUI()
        }

        btnDiagnosa.setOnClickListener {
            prosesDiagnostik()
        }

        btnReset.setOnClickListener {
            resetForm()
        }

        btnDemo.setOnClickListener {
            autofillDemoData()
        }
    }

    private fun updateTypeSelectorUI() {
        if (isMaticSelected) {
            btnTypeMatic.setBackgroundResource(R.drawable.bg_chip_selected)
            btnTypeMatic.setTextColor(Color.parseColor("#00F5D4"))

            btnTypeManual.setBackgroundResource(R.drawable.bg_chip_unselected)
            btnTypeManual.setTextColor(Color.parseColor("#8D99AE"))
        } else {
            btnTypeManual.setBackgroundResource(R.drawable.bg_chip_selected)
            btnTypeManual.setTextColor(Color.parseColor("#00F5D4"))

            btnTypeMatic.setBackgroundResource(R.drawable.bg_chip_unselected)
            btnTypeMatic.setTextColor(Color.parseColor("#8D99AE"))
        }
    }

    private fun autofillDemoData() {
        clearErrors()
        etNamaPemilik.setText("Budi Santoso")
        etEmail.setText("budi@gmail.com")
        etTahunMotor.setText("2021")
        etOdometer.setText("18500")
        etOdoOliTerakhir.setText("16800")
        Toast.makeText(this, "⚡ Data Demo Berhasil Diisi!", Toast.LENGTH_SHORT).show()
    }

    private fun clearErrors() {
        etNamaPemilik.error = null
        etEmail.error = null
        etTahunMotor.error = null
        etOdometer.error = null
        etOdoOliTerakhir.error = null
    }

    private fun resetForm() {
        clearErrors()
        etNamaPemilik.text.clear()
        etEmail.text.clear()
        etTahunMotor.text.clear()
        etOdometer.text.clear()
        etOdoOliTerakhir.text.clear()
        isMaticSelected = true
        updateTypeSelectorUI()
        etNamaPemilik.requestFocus()
        Toast.makeText(this, "Form telah direset", Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: String): Boolean {
        if (email.isEmpty()) return false
        val emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
        return email.matches(emailPattern.toRegex())
    }

    private fun prosesDiagnostik() {
        clearErrors()

        val nama = etNamaPemilik.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val tahunStr = etTahunMotor.text.toString().trim()
        val odoStr = etOdometer.text.toString().trim()
        val odoOliStr = etOdoOliTerakhir.text.toString().trim()

        // 1. Cek Kolom Kosong (Early Exit Pattern)
        if (nama.isEmpty()) {
            etNamaPemilik.error = getString(R.string.err_nama_empty)
            etNamaPemilik.requestFocus()
            return
        }

        if (email.isEmpty()) {
            etEmail.error = getString(R.string.err_email_empty)
            etEmail.requestFocus()
            return
        }

        if (tahunStr.isEmpty()) {
            etTahunMotor.error = getString(R.string.err_tahun_empty)
            etTahunMotor.requestFocus()
            return
        }

        if (odoStr.isEmpty()) {
            etOdometer.error = getString(R.string.err_odo_empty)
            etOdometer.requestFocus()
            return
        }

        if (odoOliStr.isEmpty()) {
            etOdoOliTerakhir.error = getString(R.string.err_odo_oli_empty)
            etOdoOliTerakhir.requestFocus()
            return
        }

        // 2. Validasi Format Email Presisi (Cegah budi@, budi, budi@gmail., budi@gm, dll.)
        if (!isValidEmail(email)) {
            etEmail.error = getString(R.string.err_email_invalid)
            etEmail.requestFocus()
            return
        }

        // 3. Validasi Tahun Motor (Rentang 2000 s/d Tahun Berjalan)
        val tahunSekarang = Calendar.getInstance()[Calendar.YEAR]
        val tahunMotor = tahunStr.toIntOrNull() ?: 0
        if (tahunMotor !in 2000..tahunSekarang) {
            etTahunMotor.error = getString(R.string.err_tahun_range, tahunSekarang)
            etTahunMotor.requestFocus()
            return
        }

        // 4. Validasi Odometer Logis (100 s/d 500.000 KM)
        val odo = odoStr.toLongOrNull() ?: 0L
        if (odo !in 100L..500000L) {
            etOdometer.error = getString(R.string.err_odo_range)
            etOdometer.requestFocus()
            return
        }

        // 5. Validasi Odometer Oli (Odo Oli Terakhir TIDAK BOLEH > Odometer saat ini)
        val odoOli = odoOliStr.toLongOrNull() ?: 0L
        if (odoOli > odo) {
            etOdoOliTerakhir.error = getString(R.string.err_odo_oli_exceed)
            etOdoOliTerakhir.requestFocus()
            return
        }

        // ==========================================
        // CALCULATION ENGINE & KOTLIN LOGIC
        // ==========================================
        val umurMotor = (tahunSekarang - tahunMotor).coerceAtLeast(0)

        // Persentase Oli Mesin Synthetic (Interval 2.500 KM)
        val pemakaianOli = odo - odoOli
        val sisaOliKm = (2500 - pemakaianOli).coerceAtLeast(0)
        val persenOli = ((sisaOliKm.toDouble() / 2500.0) * 100).toInt().coerceIn(0, 100)

        // Persentase Transmisi Dinamis (Matic 12.000 KM | Manual 15.000 KM)
        val intervalTransmisi = if (isMaticSelected) 12000L else 15000L
        val pemakaianTransmisi = odo % intervalTransmisi
        val sisaTransmisiKm = (intervalTransmisi - pemakaianTransmisi).coerceAtLeast(0)
        val persenTransmisi = ((sisaTransmisiKm.toDouble() / intervalTransmisi.toDouble()) * 100).toInt().coerceIn(0, 100)

        // Persentase Aki Dry Cell (Berdasarkan Umur Motor)
        val persenAki = when {
            umurMotor <= 2 -> 95
            umurMotor <= 4 -> 70
            umurMotor <= 6 -> 45
            else -> 20
        }

        // Persentase Filter Udara & Busi (Interval 10.000 KM)
        val pemakaianFilter = odo % 10000L
        val sisaFilterKm = (10000L - pemakaianFilter).coerceAtLeast(0)
        val persenFilter = ((sisaFilterKm.toDouble() / 10000.0) * 100).toInt().coerceIn(0, 100)

        // Overall Health Index Score (Weighted Average: 35% Oli, 35% Transmisi, 15% Aki, 15% Filter)
        val totalHealthScore = ((persenOli * 0.35) + (persenTransmisi * 0.35) + (persenAki * 0.15) + (persenFilter * 0.15)).toInt().coerceIn(0, 100)

        // Status Motor
        val statusMotor = when (totalHealthScore) {
            in 85..100 -> "PERFORMA PRIMA (Ready Touring 🚀)"
            in 65..84  -> "BUTUH SERVIS ANJURAN (Perlu Check-up 🔧)"
            in 40..64  -> "WASPADA (Performa Menurun & Boros BBM ⚠️)"
            else       -> "BAHAYA / KRITIS (Wajib Kunjungi Bengkel! 🚨)"
        }

        // Itemization Cost Estimation (Rincian Biaya Transparan)
        var totalBiaya = 50000 // Jasa Bengkel Dasar
        val rincianItems = mutableListOf<String>()
        val localeId = Locale.forLanguageTag("id-ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeId)

        rincianItems.add("• Jasa Bengkel Dasar: ${formatRupiah.format(50000).replace(",00", "")}")

        if (persenOli < 30) {
            totalBiaya += 65000
            rincianItems.add("• Ganti Oli Mesin Synthetic (SAE 10W-30/40): ${formatRupiah.format(65000).replace(",00", "")}")
        }
        if (persenTransmisi < 30) {
            val biayaDrive = 150000
            val labelDrive = if (isMaticSelected) "Ganti Set Roller CVT & V-Belt" else "Ganti Rantai & Gear Set Kit"
            totalBiaya += biayaDrive
            rincianItems.add("• $labelDrive: ${formatRupiah.format(biayaDrive).replace(",00", "")}")
        }
        if (persenAki < 30) {
            totalBiaya += 220000
            rincianItems.add("• Ganti Aki Dry Cell (12V): ${formatRupiah.format(220000).replace(",00", "")}")
        }
        if (persenFilter < 30) {
            totalBiaya += 45000
            rincianItems.add("• Ganti Busi & Clean Filter Udara: ${formatRupiah.format(45000).replace(",00", "")}")
        }

        if (rincianItems.size == 1) {
            rincianItems.add("• Semua komponen dalam kondisi prima (Tidak ada komponen aus < 30%)")
        }

        val rincianBiayaText = rincianItems.joinToString("\n")
        val transmisiLabel = if (isMaticSelected) "Matic (CVT)" else "Manual / Sport (Gear & Rantai)"

        // Navigasi Intent ke SecondActivity
        val intent = Intent(this, SecondActivity::class.java).apply {
            putExtra(SecondActivity.EXTRA_NAMA, nama)
            putExtra(SecondActivity.EXTRA_EMAIL, email)
            putExtra(SecondActivity.EXTRA_TRANSMISI, transmisiLabel)
            putExtra(SecondActivity.EXTRA_UMUR_MOTOR, umurMotor)
            putExtra(SecondActivity.EXTRA_ODOMETER, odo)
            putExtra(SecondActivity.EXTRA_HEALTH_SCORE, totalHealthScore)
            putExtra(SecondActivity.EXTRA_STATUS_MOTOR, statusMotor)
            putExtra(SecondActivity.EXTRA_PERSEN_OLI, persenOli)
            putExtra(SecondActivity.EXTRA_SISA_OLI_KM, sisaOliKm)
            putExtra(SecondActivity.EXTRA_PERSEN_TRANSMISI, persenTransmisi)
            putExtra(SecondActivity.EXTRA_SISA_TRANSMISI_KM, sisaTransmisiKm)
            putExtra(SecondActivity.EXTRA_PERSEN_AKI, persenAki)
            putExtra(SecondActivity.EXTRA_PERSEN_FILTER, persenFilter)
            putExtra(SecondActivity.EXTRA_TOTAL_BIAYA, totalBiaya)
            putExtra(SecondActivity.EXTRA_RINCIAN_BIAYA, rincianBiayaText)
        }

        startActivity(intent)
    }
}
