package com.caffeine.dlabloodfamily.view.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.caffeine.dlabloodfamily.R
import com.caffeine.dlabloodfamily.databinding.FragmentVerificationBinding
import com.caffeine.dlabloodfamily.utils.Constants
import com.caffeine.dlabloodfamily.utils.DataState
import com.caffeine.dlabloodfamily.viewmodel.AuthViewModel

class VerificationFragment : Fragment() {

    private lateinit var binding : FragmentVerificationBinding
    private val args: VerificationFragmentArgs by navArgs()
    private val viewModel : AuthViewModel by viewModels()

    private var otp = ""
    private var cnt = 0
    private var isButtonEnabled = false

    private var verificationID = ""
    private var number = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerificationBinding.inflate(inflater)

        verificationID = args.args[0]
        number = args.args[1]

        jumpToNext()

        binding.verifyBtn.setOnClickListener{
            if (isButtonEnabled){
                otpIntialize()
                viewModel.verifyOTP(verificationID, otp)
                viewModel.authLiveData.observe(viewLifecycleOwner){
                    when (it) {
                        is DataState.Loading -> {
                            binding.arrowOne.visibility = View.VISIBLE
                            binding.loadingLayout.visibility = View.VISIBLE
                        }

                        is DataState.Success -> {
                            binding.arrowTwo.visibility = View.VISIBLE
                            binding.finalLayout.visibility = View.VISIBLE
                        }

                        is DataState.Failed -> {
                            binding.confirmationText.text = it.message
                            binding.confirmationIcon.setImageResource(R.drawable.warning)
                            binding.confirmationIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorLightRed), android.graphics.PorterDuff.Mode.MULTIPLY);
                            binding.arrowTwo.visibility = View.VISIBLE
                            binding.finalLayout.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun otpIntialize(){
        otp = binding.otp1.text.toString() + binding.otp2.text.toString() + binding.otp3.text.toString() + binding.otp4.text.toString() + binding.otp5.text.toString() + binding.otp6.text.toString()
    }

    private fun jumpToNext(){
        binding.otp1.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.otp1.text.toString().length == 1){
                    binding.otp2.requestFocus()
                    cnt++
                }
                if (binding.otp1.text.toString().isEmpty()){
                    cnt--
                }
                makeButtonEnabled()
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.otp2.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.otp2.text.toString().length == 1){
                    binding.otp3.requestFocus()
                    cnt++
                }
                if (binding.otp2.text.toString().isEmpty()){
                    binding.otp1.requestFocus()
                    cnt--
                }
                makeButtonEnabled()
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.otp3.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.otp3.text.toString().length == 1){
                    binding.otp4.requestFocus()
                    cnt++
                }
                if (binding.otp3.text.toString().isEmpty()){
                    binding.otp2.requestFocus()
                    cnt--
                }
                makeButtonEnabled()
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.otp4.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.otp4.text.toString().length == 1){
                    binding.otp5.requestFocus()
                    cnt++
                }
                if (binding.otp4.text.toString().isEmpty()){
                    binding.otp3.requestFocus()
                    cnt--
                }
                makeButtonEnabled()
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.otp5.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.otp5.text.toString().length == 1){
                    binding.otp6.requestFocus()
                    cnt++
                }
                if (binding.otp5.text.toString().isEmpty()){
                    binding.otp4.requestFocus()
                    cnt--
                }
                makeButtonEnabled()
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.otp6.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.otp6.text.toString().length == 1){
                    cnt++
                }
                if (binding.otp6.text.toString().isEmpty()){
                    binding.otp5.requestFocus()
                    cnt--
                }
                makeButtonEnabled()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun makeButtonEnabled(){
        if (cnt == 6){
            isButtonEnabled = true
            binding.verifyBtn.setBackgroundResource(R.drawable.bg_light_red_5)
        }
        else {
            isButtonEnabled = false
            binding.verifyBtn.setBackgroundResource(R.drawable.bg_disabled_button)
        }
    }
}