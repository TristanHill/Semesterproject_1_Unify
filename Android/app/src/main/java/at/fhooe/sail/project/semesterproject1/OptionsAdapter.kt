package at.fhooe.sail.project.semesterproject1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class OptionsAdapter(
    private val mData: List<Option>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<OptionsAdapter.OptionHolder>() {

    private var selectedPosition: Int? = null

    inner class OptionHolder(val root: View) : RecyclerView.ViewHolder(root), View.OnClickListener {
        val mContent: TextView = root.findViewById(R.id.fragment_survey_option_tv)

        init {
            root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
                setSelectedPosition(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_survey_option, parent, false)
        return OptionHolder(view)
    }

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        holder.mContent.text = mData[position].content

        val context = holder.itemView.context
        val colorRes = if (selectedPosition == position) {
            ContextCompat.getColor(context, R.color.selected_item)
        } else {
            ContextCompat.getColor(context, R.color.unselected_item)
        }
        holder.itemView.setBackgroundColor(colorRes)
    }

    private fun setSelectedPosition(newPos: Int) {
        val old = selectedPosition
        selectedPosition = newPos
        old?.let { notifyItemChanged(it) }
        notifyItemChanged(newPos)
    }

    override fun getItemCount(): Int = mData.size
}
