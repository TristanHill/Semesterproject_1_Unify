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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.activity_join_session_button -> {
                // Start QRCodeScannerActivity when the "Join Session" button is clicked
                startActivity(Intent(this, QRCodeScannerActivity::class.java))
            }
            else -> {
                Log.e(TAG, "JoinSessionActivity::onClick unexpected ID encountered (${v?.id})")
            }
        }
    }
}
