package com.devjsr.inventario.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devjsr.inventario.R
import com.devjsr.inventario.databinding.FragmentItemDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ItemDetailFragment : Fragment() {

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentItemDetailBinding.inflate( inflater, container, false)
        return binding.root
    }

    /* Display an alert dialog to get the user´s confirmation defore deleting the item */
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

    /* Delete the current item and navigates to the list fragment */
    private fun deleteItem() {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}