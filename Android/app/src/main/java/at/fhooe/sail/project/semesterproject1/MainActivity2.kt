package at.fhooe.sail.project.semesterproject1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import at.fhooe.sail.project.semesterproject1.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
        private  lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Fragment_question())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.status_item -> replaceFragment(Fragment_status())
                R.id.question_item -> replaceFragment(Fragment_question())
                R.id.survey_item -> replaceFragment(Fragment_survey())
                else -> {}
            }

            true
        }
    }

   private fun replaceFragment(fragment: Fragment){
       val fragmentManager = supportFragmentManager
       val fragmentTransaction = fragmentManager.beginTransaction()
       fragmentTransaction.replace(R.id.activity_main_frameLayout, fragment)
       fragmentTransaction.commit()
   }
}