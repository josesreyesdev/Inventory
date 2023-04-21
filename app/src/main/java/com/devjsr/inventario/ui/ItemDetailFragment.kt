package com.devjsr.inventario.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devjsr.inventario.R
import com.devjsr.inventario.data.Item
import com.devjsr.inventario.data.getFormattedPrice
import com.devjsr.inventario.databinding.FragmentItemDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ItemDetailFragment : Fragment() {

    private val viewModel: InventoryViewModel by activityViewModels{
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    lateinit var item: Item

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentItemDetailBinding.inflate( inflater, container, false)
        return binding.root
    }

    /* Display an alert dialog to get the user´s confirmation before deleting the item */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _,_ ->}
            .setPositiveButton(getString(R.string.yes)) { _,_ ->
                deleteItem()
            }
            .show()
    }

    //vincular los txt
    private fun bind( item: Item) {
        binding.apply {
            itemName.text = item.itemName
            itemPrice.text = item.getFormattedPrice()
            itemCount.text = item.quantityInStock.toString()

            sellItem.isEnabled = viewModel.isStockAvailable(item)
            sellItem.setOnClickListener { viewModel.sellItem(item) }

            deleteItem.setOnClickListener { showConfirmationDialog() }

            editItem.setOnClickListener { editItem() }
        }
    }

    /* Delete the current item and navigates to the list fragment */
    private fun deleteItem() {
        viewModel.deleteItem(item)
        findNavController().navigateUp()
    }

    private fun editItem() {
        val action = ItemDetailFragmentDirections.actionItemDetailFragmentToAddItemFragment(
            getString(R.string.edit_fragment_title),
            item.id
        )
        this.findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //recuperando el id desde itemListFragment
        val id = navigationArgs.itemId

        //recuperando Item por ID
        viewModel.retrievedItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
            item = selectedItem
            bind(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}