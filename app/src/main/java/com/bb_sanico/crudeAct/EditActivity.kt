package com.bb_sanico.crudeAct

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EditActivity : AppCompatActivity() {
    private var FName: EditText? = null
    private var MName: EditText? = null
    private var LName: EditText? = null
    private var BtnUpdate: Button? = null
    private var BtnDelete: Button? = null
    private var BtnBack: Button? = null
    private var Conn: SQLiteDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        Conn = SQLiteDB(this)

        // retrieve the extra values
        val extras = intent.extras
        val id = extras!!.getInt(SQLiteDB.PROF_ID)
        val fName = extras.getString(SQLiteDB.PROF_FNAME)
        val mName = extras.getString(SQLiteDB.PROF_MNAME)
        val lName = extras.getString(SQLiteDB.PROF_LNAME)

        // set the values to the fields
        val fNameField = findViewById<EditText>(R.id.fName)
        val mNameField = findViewById<EditText>(R.id.mName)
        val lNameField = findViewById<EditText>(R.id.lName)
        fNameField.setText(fName)
        mNameField.setText(mName)
        lNameField.setText(lName)

        // assign edittext fields
        FName = findViewById(R.id.fName)
        MName = findViewById(R.id.mName)
        LName = findViewById(R.id.lName)
        val nameFields = listOf(
            FName, MName, LName
        )

        // assign button views
        BtnDelete = findViewById(R.id.btnDelete)
        BtnUpdate = findViewById(R.id.btnUpdate)
        BtnBack = findViewById(R.id.btnBack)

        BtnBack!!.setOnClickListener {
            Log.i("BtnBack", "BtnBack clicked!")
            finish()
        }

        BtnDelete!!.setOnClickListener {
            // delete the entry in db, check if it's successful
            if (Conn!!.DeleteRecord(id)) {
                Log.i("BtnEdit", "Record deleted!")
                Toast.makeText(this, "Record deleted!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Log.i("BtnEdit", "Record deletion unsuccessful!")
                Toast.makeText(this, "Record deletion unsuccessful!", Toast.LENGTH_SHORT).show()
            }
        }

        BtnUpdate!!.setOnClickListener {
            Log.i("JobAlleyEditRecord", "BtnEdit clicked!")

            // Get the values from the edit text fields
            val newFirstName = FName!!.text.toString()
            val newMiddleName = MName!!.text.toString()
            val newLastName = LName!!.text.toString()

            // Check if each field has values
            if (newFirstName.isEmpty() || newLastName.isEmpty()) {
                Toast.makeText(this, "Please don't leave a blank" +
                        "!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if the new values conflict with existing records (excluding the current record being edited)
            val existingRecordId = intent.extras!!.getInt(SQLiteDB.PROF_ID)
            if (Conn!!.RecordExists(newFirstName, newMiddleName, newLastName) && !Conn!!.RecordExists(newFirstName, newMiddleName, newLastName, existingRecordId)) {
                Log.i("BtnUpdate", " An existing record is found!")
                Toast.makeText(this, "Error : An existing record is found!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Update the entry in the database, check if it's successful
            val success = Conn!!.UpdateRecord(
                existingRecordId,
                newFirstName,
                newMiddleName,
                newLastName
            )
            if (success) {
                Log.i("BtnEdit", "Record updated!")
                Toast.makeText(this, "Record updated!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Log.i("BtnEdit", "Error saving changes")
                Toast.makeText(this, "Error saving changes", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
