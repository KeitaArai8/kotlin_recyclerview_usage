package com.example.recyclerviewusage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recycler_view_holder.view.*

class MainActivity : AppCompatActivity() {

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                val adapter = recyclerView.adapter as MainRecyclerViewAdapter
                val from = viewHolder.layoutPosition
                val to = target.layoutPosition
                adapter.moveItem(from, to)
                adapter.notifyItemMoved(from, to)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)

                if (actionState == ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.5f
                }
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)

                viewHolder.itemView.alpha = 1.0f
            }
        }

        ItemTouchHelper(simpleItemTouchCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MainRecyclerViewAdapter(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}

class MainRecyclerViewAdapter(private val activity: MainActivity):
    RecyclerView.Adapter<MainRecyclerViewAdapter.MainRecyclerViewHolder>() {
    private var lists = listOf(
        "adapter0",
        "adapter1",
        "adapter2",
        "adapter3",
        "adapter4",
        "adapter5",
        "adapter6",
        "adapter7",
        "adapter8",
        "adapter9",
        "adapter10",
        "adapter11",
        "adapter12",
        "adapter13"
    ).toMutableList()

    fun moveItem(from: Int, to: Int) {
        val fromEmoji = lists[from]
        lists.removeAt(from)
        if (to < from) {
            lists.add(to, fromEmoji)
        } else {
            lists.add(to - 1, fromEmoji)
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        val emoji = lists[position]
        holder.setText(emoji)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_holder, parent, false)
        val viewHolder = MainRecyclerViewHolder(itemView)

        viewHolder.itemView.handleView.setOnTouchListener { view, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                activity.startDragging(viewHolder)
            }
            return@setOnTouchListener true
        }

        return viewHolder
    }

    class MainRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun setText(text: String) {
            itemView.textView.text = text
        }
    }
}