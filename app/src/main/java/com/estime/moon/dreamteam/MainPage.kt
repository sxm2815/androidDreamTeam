package com.estime.moon.dreamteam

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.TextInputEditText
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.app_bar_main_page.*
import android.widget.Toast
import com.google.android.gms.common.internal.Constants
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.messaging_page.* //Retrieves ID's from the Messaging layout
import kotlinx.android.synthetic.main.notes_page.* //Retrieves ID's from the Notes layout
import java.util.*
import kotlin.collections.ArrayList

class MainPage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var dbReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        setSupportActionBar(toolbar)

        initFirebase()

        //SETS THE BUTTON ON THE NOTES TO ONCLICK FOR THE NOTESFOR THE NOTESFOR THE NOTESFOR THE NOTESFOR THE NOTESFOR THE NOTES
        setupFabCreateButton()

        createFireBaseNotesListener()
        nav_view.setNavigationItemSelectedListener(this)
    }

    //FIREBASE INITIALIZATION
    private fun initFirebase(){
        FirebaseApp.initializeApp(applicationContext)
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG)

        dbReference = FirebaseDatabase.getInstance().reference
    }

    //CREATES A LISTENER FOR THE NOTES SYSTEM FOR THE NOTES FOR THE NOTES FOR THE NOTES FOR THE NOTES FOR THE NOTES FOR THE NOTES
    private fun createFireBaseNotesListener(){
        val postListener = object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val notesList: ArrayList<Notes> = ArrayList()

                for(data in p0.children){
                    val notesData = data.getValue<Notes>(Notes::class.java)
                    val note = notesData?.let { it } ?: continue

                    notesList.add(note)
                }
                notesList.sortBy { note ->
                    note.timeStamp
                }
                print(notesList)
                setupNotesAdapter(notesList)
            }
            override fun onCancelled(p0: DatabaseError) {}
        }
        dbReference?.child("notes")?.addValueEventListener(postListener)
    }

    //CREATES A LISTENER FOR THE MESSAGING SYSTEM
    private fun createFireBaseMessageListener(){
        val postListener = object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val messageList: ArrayList<Message> = ArrayList()

                for(data in p0.children){
                    val messageData = data.getValue<Message>(Message::class.java)
                    val message = messageData?.let { it } ?: continue

                    messageList.add(message)
                }
                messageList.sortBy { message ->
                    message.timeStamp
                }
                setupMessageAdapter(messageList)
            }
            override fun onCancelled(p0: DatabaseError) {}
        }
        dbReference?.child("messages")?.addValueEventListener(postListener)
    }

    //Sets up the adapter for the Messages
    private fun setupMessageAdapter(data: ArrayList<Message>){
        val linearLayoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = MessageAdapter(data) {
            Toast.makeText(this, "${it.text} clicked", Toast.LENGTH_SHORT).show()
        }
        messageRecyclerView.scrollToPosition(data.size -1)
    }

    //Sets up the adapter for the Messages FOR THE NOTES
    private fun setupNotesAdapter(data: ArrayList<Notes>){
        val lineLayoutManager = LinearLayoutManager(this)
        notesRecyclerView.adapter = NotesAdapter(data) {
            Toast.makeText(this, "${it.title} clicked", Toast.LENGTH_SHORT).show()
        }
        notesRecyclerView.scrollToPosition(data.size -1)
    }

    //FAB BUTTON TO CREATE NOTES  FOR THE NOTESFOR THE NOTESFOR THE NOTES FOR THE NOTESFOR THE NOTESFOR THE NOTESFOR THE NOTES
    private fun setupFabCreateButton(){
        fab.setOnClickListener{
            addNewItemDialog()
        }
    }

    //CREATES AN ALERT DIALOG WHEN CREATING A NOTE FOR THE NOTESFOR THE NOTESFOR THE NOTESFOR THE NOTESFOR THE NOTESFOR THE NOTES
    private fun addNewItemDialog() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_notes_alert, null)
        val editTitle = dialogView.findViewById<EditText>(R.id.notesTitle)
        val editDescription = dialogView.findViewById<EditText>(R.id.notesDesc)

        dialog.setView(dialogView)
        dialog.setCancelable(false)

        dialog.setPositiveButton("Submit"){dialog, positiveButton ->
            dbReference?.
                child("notes")?.
                child(java.lang.String.valueOf(System.currentTimeMillis()))?.
                setValue(Notes(editTitle.text.toString(),editDescription.text.toString()))
            dialog.dismiss()
            Toast.makeText(this, "Notes are saved ", Toast.LENGTH_SHORT).show()
        }

        dialog.setNegativeButton("Cancel",{dialogInterface: DialogInterface, i: Int ->})
        dialog.show()
    }

    //SETTING UP SEND BUTTON FOR MESSAGING
    private fun setupMessageSendButton(){
        messageSendButton.setOnClickListener{
            if(!messageEditText.text.toString().isEmpty()){
                sendMessage()
            } else {
                Toast.makeText(this,"Please Enter a message",Toast.LENGTH_SHORT).show()
            }
        }
    }

    //SENDING THE DATA TO FIREBASE
    private fun sendMessage(){
        dbReference?.
                child("messages")?.
                child(java.lang.String.valueOf(System.currentTimeMillis()))?.
                setValue(Message("User: "+messageEditText.text.toString()))
        messageEditText.setText("")
    }

    //MENU IMPLEMENTATION
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_group1 -> {
                // Handle Group 1
            }
            R.id.nav_group2 -> {

            }
            R.id.nav_group3 -> {

            }
            R.id.nav_group4 -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
