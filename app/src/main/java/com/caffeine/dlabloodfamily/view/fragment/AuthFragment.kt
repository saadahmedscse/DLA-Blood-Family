package com.caffeine.dlabloodfamily.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.caffeine.dlabloodfamily.R
import com.caffeine.dlabloodfamily.databinding.FragmentAuthBinding
import com.caffeine.dlabloodfamily.utils.AlertDialog
import com.caffeine.dlabloodfamily.utils.Constants
import com.caffeine.dlabloodfamily.utils.DataState
import com.caffeine.dlabloodfamily.viewmodel.AuthViewModel

class AuthFragment : Fragment() {

    private lateinit var binding : FragmentAuthBinding
    private val viewModel : AuthViewModel by viewModels()
    private var isButtonEnabled = false
    private var verificationID = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater)

        makeButtonEnabled();

        binding.authBtn.setOnClickListener{
            if (isButtonEnabled){
                viewModel.authLiveData.removeObservers(viewLifecycleOwner)
                if (Constants.internetAvailable(requireContext())){
                    //send verification code
                    viewModel.sendVerificationCode(requireActivity(), binding.number.text.toString())

                    viewModel.authLiveData.observe(viewLifecycleOwner){
                        when (it) {
                            is DataState.Loading -> {
                                binding.btnTxt.visibility = View.GONE
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            is DataState.Success -> {
                                verificationID = it.data!!
                                val array = arrayOf(verificationID, "+88${binding.number.text}")
                                val action = AuthFragmentDirections.authToVerification(array)
                                Navigation.findNavController(binding.root).navigate(action)
                                binding.progressBar.visibility = View.GONE
                                binding.btnTxt.visibility = View.VISIBLE
                            }

                            is DataState.Failed -> {
                                AlertDialog.showAlertDialog(requireContext(), it.message!!, "Close")
                                binding.progressBar.visibility = View.GONE
                                binding.btnTxt.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                else {
                    AlertDialog.showAlertDialog(
                        requireContext(),
                        "No internet connection available. Please check your internet connection and try again",
                        "Close")
                }
            }
        }

        return binding.root
    }

    private fun makeButtonEnabled(){
        binding.number.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Before
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //On
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().startsWith("01")){
                    isButtonEnabled = false
                    binding.authBtn.setBackgroundResource(R.drawable.bg_disabled_button)
                }
                else if (s.toString().length == 11){
                    isButtonEnabled = true
                    binding.authBtn.setBackgroundResource(R.drawable.bg_light_red_5)
                }
                else {
                    isButtonEnabled = false
                    binding.authBtn.setBackgroundResource(R.drawable.bg_disabled_button)
                }
            }

        })
    }
}