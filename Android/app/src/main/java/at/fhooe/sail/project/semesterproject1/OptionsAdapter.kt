package at.fhooe.sail.android.lists.a

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.sail.project.semesterproject1.Option
import at.fhooe.sail.project.semesterproject1.R
import at.fhooe.sail.project.semesterproject1.OnItemClickListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * Adapter class for the RecyclerView to display survey options.
 */
class OptionsAdapter(val mData: List<Option>, val listener: OnItemClickListener) : RecyclerView.Adapter<OptionsAdapter.OptionHolder>() {

    /**
     * ViewHolder class for the options in the survey.
     * Handles the click events and binds the data to the views.
     */
    inner class OptionHolder(val root: View) : RecyclerView.ViewHolder(root), View.OnClickListener {
        var mContent: TextView
        val db: FirebaseFirestore = Firebase.firestore
        private lateinit var sharedPref: SharedPreferences
        private lateinit var sessionID: String
        private lateinit var userID: String

        /**
         * Initialize the ViewHolder and set up the click listener for the root view.
         */
        init {
            // Initialize the TextView from the layout
            mContent = root.findViewById(R.id.fragment_survey_option_tv) as TextView
            // Set up click listener for the root view
            root.setOnClickListener(this)
        }

        /**
         * Handle click events for the view.
         * Notifies the listener of the item click position.
         */
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.fragment_survey_option, null)
        return OptionHolder(view)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * Binds the data to the ViewHolder.
     */
    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        holder.mContent.text = mData[position].content
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    override fun getItemCount(): Int = mData.size
}
