package adarsh.myfavouritedish.view.fragments

import adarsh.myfavouritedish.R
import adarsh.myfavouritedish.application.FavDishApplication
import adarsh.myfavouritedish.databinding.FragmentAllDishesBinding
import adarsh.myfavouritedish.model.entities.FavDish
import adarsh.myfavouritedish.view.activities.AddUpdateDishActivity
import adarsh.myfavouritedish.view.activities.MainActivity
import adarsh.myfavouritedish.view.adapters.FavDishAdapter
import adarsh.myfavouritedish.viewmodel.FavDishViewModel
import adarsh.myfavouritedish.viewmodel.FavDishViewModelFactory
import adarsh.myfavouritedish.viewmodel.HomeViewModel
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

class AllDishesFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var mBinding: FragmentAllDishesBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.

    private val mFavDishViewModel : FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

       mBinding = FragmentAllDishesBinding.inflate(inflater, container, false)

        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding!!.rvDishesList.layoutManager = GridLayoutManager(requireActivity() , 2)
       val favDishAdapter = FavDishAdapter(this@AllDishesFragment)
        mBinding!!.rvDishesList.adapter = favDishAdapter

        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner){
            dishes ->
            dishes.let {

                  if (it.isNotEmpty()){
                      mBinding!!.rvDishesList.visibility = View.VISIBLE
                      mBinding!!.tvNoDishesAddedYet.visibility = View.GONE
                      favDishAdapter.dishesList(it)
                  }else{
                      mBinding!!.rvDishesList.visibility = View.VISIBLE
                      mBinding!!.tvNoDishesAddedYet.visibility = View.GONE
                  }

            }
        }
    }

    fun dishDetails(favDish:FavDish){
        findNavController().navigate(AllDishesFragmentDirections.actionAllDishesToDishDetails(
            favDish
        ))

        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity){
            (activity as MainActivity?)?.showBottomNavigationView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_dish ->{
                startActivity(Intent(requireActivity(),AddUpdateDishActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}