package com.caffeine.dlabloodfamily.view.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.caffeine.dlabloodfamily.R
import com.caffeine.dlabloodfamily.databinding.FragmentVerificationBinding
import com.caffeine.dlabloodfamily.utils.Constants
import com.caffeine.dlabloodfamily.utils.DataState
import com.caffeine.dlabloodfamily.view.activity.HomeActivity
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

    private lateinit var slideTop : Animation
    private lateinit var slideBottom : Animation

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerificationBinding.inflate(inflater)

        verificationID = args.args[0]
        number = args.args[1]
        slideTop = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_top)
        slideBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_bottom)

        makeOtpEmpty()
        jumpToNext()

        binding.verifyBtn.setOnClickListener{
            if (isButtonEnabled){
                isButtonEnabled = false
                otpIntialize()
                viewModel.verifyOTP(verificationID, otp)
                viewModel.authLiveData.observe(viewLifecycleOwner){
                    when (it) {
                        is DataState.Loading -> {
                            binding.arrowOne.visibility = View.VISIBLE
                            binding.loadingLayout.visibility = View.VISIBLE
                            binding.arrowOne.startAnimation(slideBottom)
                            binding.loadingLayout.startAnimation(slideBottom)
                        }

                        is DataState.Success -> {
                            binding.confirmationText.text = "Mobile Number Confirmed"
                            binding.confirmationIcon.setImageResource(R.drawable.checked)
                            binding.confirmationIcon.colorFilter = null
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.arrowTwo.visibility = View.VISIBLE
                                binding.finalLayout.visibility = View.VISIBLE
                                binding.arrowTwo.startAnimation(slideBottom)
                                binding.finalLayout.startAnimation(slideBottom)
                            }, 1500)
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.arrowOne.visibility = View.GONE
                                binding.loadingLayout.visibility = View.GONE
                                binding.arrowTwo.visibility = View.GONE
                                binding.finalLayout.visibility = View.GONE
                                binding.verifyBtn.visibility = View.VISIBLE
                            }, 4000)

                            Handler(Looper.getMainLooper()).postDelayed({
                                if (it.data == "noInfo") {
                                    val action = VerificationFragmentDirections.verificationToInfo("+88$number")
                                    Navigation.findNavController(requireView()).navigate(action)
                                }
                                if (it.data == "hasInfo"){
                                    Constants.intentToActivity(requireActivity(), HomeActivity::class.java)
                                }
                            }, 3000)
                        }

                        is DataState.Failed -> {
                            binding.confirmationText.text = "Invalid verification code"
                            binding.confirmationIcon.setImageResource(R.drawable.warning)
                            binding.confirmationIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorLightRed), android.graphics.PorterDuff.Mode.MULTIPLY);
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.arrowTwo.visibility = View.VISIBLE
                                binding.finalLayout.visibility = View.VISIBLE
                                binding.arrowTwo.startAnimation(slideBottom)
                                binding.finalLayout.startAnimation(slideBottom)
                            }, 1500)

                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.arrowOne.visibility = View.GONE
                                binding.loadingLayout.visibility = View.GONE
                                binding.arrowTwo.visibility = View.GONE
                                binding.finalLayout.visibility = View.GONE
                                binding.verifyBtn.visibility = View.VISIBLE
                            }, 4000)
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

    private fun makeOtpEmpty(){
        binding.otp1.setText("")
        binding.otp2.setText("")
        binding.otp3.setText("")
        binding.otp4.setText("")
        binding.otp5.setText("")
        binding.otp6.setText("")
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