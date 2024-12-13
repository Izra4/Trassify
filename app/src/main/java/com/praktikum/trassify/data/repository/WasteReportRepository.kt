package com.praktikum.trassify.data.repository

import com.google.firebase.database.DatabaseReference
import com.praktikum.trassify.data.model.WasteReport

class WasteReportRepository(private val firebaseReference: DatabaseReference) {

    fun saveWasteReport(
        wasteReport: WasteReport,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val reportId = firebaseReference.child("waste_reports").push().key
        if (reportId != null) {
            firebaseReference.child("waste_reports").child(reportId)
                .setValue(wasteReport.copy(id = reportId))
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { exception -> onError(exception) }
        } else {
            onError(Exception("Failed to generate report ID"))
        }
    }

    fun getWasteReportsByUserId(userId: String, onSuccess: (List<WasteReport>) -> Unit, onError: (Exception) -> Unit) {
        firebaseReference.child("waste_reports")
            .orderByChild("userId")  // Menyaring data berdasarkan userId
            .equalTo(userId)         // Mencocokkan userId dengan yang diberikan
            .get()
            .addOnSuccessListener { snapshot ->
                val wasteReports = mutableListOf<WasteReport>()
                for (childSnapshot in snapshot.children) {
                    val wasteReport = childSnapshot.getValue(WasteReport::class.java)
                    if (wasteReport != null) {
                        wasteReports.add(wasteReport)
                    }
                }
                onSuccess(wasteReports)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun getWasteReportById(
        reportId: String,
        onSuccess: (WasteReport) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firebaseReference.child("waste_reports").child(reportId)
            .get()
            .addOnSuccessListener { snapshot ->
                try {
                    // Pastikan snapshot adalah objek DataSnapshot yang valid
                    val wasteReport = snapshot.getValue(WasteReport::class.java)
                    if (wasteReport != null) {
                        onSuccess(wasteReport)
                    } else {
                        onError(Exception("Laporan tidak ditemukan"))
                    }
                } catch (e: Exception) {
                    onError(e)
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

}

