package adarsh.myfavouritedish.view.fragments

import adarsh.myfavouritedish.application.FavDishApplication
import adarsh.myfavouritedish.databinding.FragmentFavoriteDishesBinding
import adarsh.myfavouritedish.model.entities.FavDish
import adarsh.myfavouritedish.view.activities.MainActivity
import adarsh.myfavouritedish.view.adapters.FavDishAdapter
import adarsh.myfavouritedish.viewmodel.FavDishViewModel
import adarsh.myfavouritedish.viewmodel.FavDishViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

class FavoriteDishesFragment : Fragment() {

    //   private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentFavoriteDishesBinding? = null
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)
        val root: View = mBinding.root
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set the LayoutManager that this RecyclerView will use.
        mBinding!!.rvFavoriteDishesList.layoutManager =
            GridLayoutManager(requireActivity(), 2)
        // Adapter class is initialized and list is passed in the param.
        val adapter = FavDishAdapter(this@FavoriteDishesFragment)
        // adapter instance is set to the recyclerview to inflate the items.
        mBinding!!.rvFavoriteDishesList.adapter = adapter

        mFavDishViewModel.favoriteDishes.observe(viewLifecycleOwner) { dishes ->

            dishes.let {
                if (it.isNotEmpty()) {
                    mBinding!!.rvFavoriteDishesList.visibility = View.VISIBLE
                    mBinding!!.tvNoFavoriteDishesAvailable.visibility = View.GONE
                    adapter.dishesList(it)
                } else {
                    mBinding!!.rvFavoriteDishesList.visibility = View.GONE
                    mBinding!!.tvNoFavoriteDishesAvailable.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun dishDetails(favDish: FavDish) {
        findNavController().navigate(
            FavoriteDishesFragmentDirections.actionFavDishesToDishDetails(
                favDish
            )
        )

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }
}