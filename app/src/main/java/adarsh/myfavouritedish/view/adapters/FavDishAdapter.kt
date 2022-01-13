package adarsh.myfavouritedish.view.adapters

import adarsh.myfavouritedish.databinding.ItemDishLayoutBinding
import adarsh.myfavouritedish.model.entities.FavDish
import adarsh.myfavouritedish.view.fragments.AllDishesFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavDishAdapter(private val fragment:Fragment):RecyclerView.Adapter<FavDishAdapter.ViewHolder>(){

    private var dishes: List<FavDish> = listOf()

    class ViewHolder(view:ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root){
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemDishLayoutBinding = ItemDishLayoutBinding.inflate(
            LayoutInflater.from(fragment.context) , parent , false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)
        holder.tvTitle.text = dish.title
        holder.itemView.setOnClickListener {
            if(fragment is AllDishesFragment){
                fragment.dishDetails(dish)
            }
        }

    }

    override fun getItemCount(): Int {
       return dishes.size
    }

    fun dishesList(list:List<FavDish>){
        dishes = list
        notifyDataSetChanged()
    }
}