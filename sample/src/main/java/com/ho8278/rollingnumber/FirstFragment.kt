package com.ho8278.rollingnumber

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ho8278.rollingnumber.databinding.FragmentFirstBinding
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            val aa = Random.nextInt(0, 1000000000)
            binding.te.text = getPriceString(aa)
        }
    }

    private fun getPriceString(price: Int): String {
        var priceText = price.toString()
        var result = ""

        if (priceText.length <= 3) return price.toString()

        while (priceText.isNotEmpty()) {
            if (priceText.length > 3) {
                result = ",${priceText.takeLast(3)}$result"
                priceText = priceText.dropLast(3)
            } else {
                result = "$priceText$result"
                priceText = ""
            }
        }
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}