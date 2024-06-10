package at.fhooe.sail.project.semesterproject1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import at.fhooe.sail.project.semesterproject1.databinding.ActivityJoinSessionBinding

class JoinSessionActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityJoinSessionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the "Join Session" button
        binding.activityJoinSessionButton.setOnClickListener(this)
    }

    // onClick method to handle click events on views
    override fun onClick(v: View?) {
        // Check the ID of the clicked view
        when (v?.id) {
            R.id.activity_join_session_button -> {
                // Create an intent to start the QRCodeScannerActivity
                startActivity(Intent(this, QRCodeScannerActivity::class.java))
            }
            // Default case if an unexpected view ID is encountered
            else -> {
                // Log an error message with the unexpected view ID
                Log.e(TAG, "JoinSessionActivity::onClick unexpected ID encountered (${v?.id})")
            }
        }
    }
}
