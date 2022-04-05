package com.caffeine.dlabloodfamily.view.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.caffeine.dlabloodfamily.R
import com.caffeine.dlabloodfamily.databinding.FragmentInfoBinding
import com.caffeine.dlabloodfamily.services.model.UserDetails
import com.caffeine.dlabloodfamily.utils.AlertDialog
import com.caffeine.dlabloodfamily.utils.Constants
import com.caffeine.dlabloodfamily.utils.DataState
import com.caffeine.dlabloodfamily.view.activity.HomeActivity
import com.caffeine.dlabloodfamily.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS
import kotlin.collections.ArrayList

class InfoFragment : Fragment() {

    private lateinit var binding : FragmentInfoBinding
    private val arg: InfoFragmentArgs by navArgs()
    private val viewModel : UserViewModel by viewModels()

    private var number = ""
    private var name : String = ""
    private var location : String = ""
    private var bloodGroup : String = ""
    private var gender : String = ""
    private var dob : String = ""
    private var ldd : String = ""
    private var tempLdd : String = ""
    private lateinit var format : SimpleDateFormat
    private lateinit var defaultFormat : SimpleDateFormat
    private lateinit var months : Array<Int>

    private lateinit var bgList : ArrayList<TextView>
    private lateinit var txtGender : ArrayList<TextView>
    private lateinit var icGender : ArrayList<ImageView>
    private lateinit var genderList : ArrayList<LinearLayout>

    private var listItemCount = 0
    private var count = 0

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater)

        format = SimpleDateFormat("dd-MMM-yyyy")
        defaultFormat = SimpleDateFormat("dd-MM-yyyy")
        months = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        bgList = ArrayList()
        genderList = ArrayList()
        txtGender = ArrayList()
        icGender = ArrayList()

        addTextViewToList()
        textItemClickListeners()

        binding.dobPicker.setOnClickListener{
            chooseDob(binding.dob)
        }

        binding.lddPicker.setOnClickListener{
            chooseDob(binding.ldd)
        }

        binding.never.setOnClickListener{
            if (count == 0){
                ldd = "never"
                binding.never.setBackgroundResource(R.drawable.bg_light_red_5)
                binding.icNever.setColorFilter(resources.getColor(R.color.colorWhite))
                binding.txtNever.setTextColor(resources.getColor(R.color.colorWhite))
                binding.lddPicker.visibility = View.GONE
                count++
            }
            else if (count == 1){
                ldd = tempLdd
                binding.never.setBackgroundResource(R.drawable.bg_light_grey_5)
                binding.icNever.setColorFilter(resources.getColor(R.color.colorDarkGrey))
                binding.txtNever.setTextColor(resources.getColor(R.color.colorDarkGrey))
                binding.lddPicker.visibility = View.VISIBLE
                count--;
            }
        }

        binding.updateBtn.setOnClickListener{
            initialize()
            val user = UserDetails(
                Constants.auth.uid!!,
                name,
                location,
                number,
                bloodGroup,
                gender,
                dob,
                ldd
            )
            if (Constants.internetAvailable(requireContext())){
                if (informationIsValid()){
                    viewModel.updateData(user)
                }
            }
            else {
                AlertDialog.showAlertDialog(
                    requireContext(),
                    "No internet connection available. Please check your internet connection and try again",
                    "Close")
            }
        }

        viewModel.userLiveData.observe(viewLifecycleOwner){
            when (it) {
                is DataState.Loading -> {
                    binding.btnTxt.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }

                is DataState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnTxt.visibility = View.VISIBLE
                    Constants.intentToActivity(requireActivity(), HomeActivity::class.java)
                }

                is DataState.Failed -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnTxt.visibility = View.VISIBLE
                    AlertDialog.showAlertDialog(requireContext(), it.message!!, "Close")
                }
            }
        }

        return binding.root
    }

    private fun initialize(){
        number = arg.number
        name = binding.name.text.toString()
        location = binding.address.text.toString()
    }

    /*------------------------------------------------------TEXT VIEW FUNCTIONALITIES------------------------------------------------------*/

    private fun textItemClickListeners() {
        bgList[0].setOnClickListener{
            listItemCount = 0
            bloodGroup = bgList[listItemCount].text.toString()
            changeBloodGroupBackground(bgList)
        }

        bgList[1].setOnClickListener{
            listItemCount = 1
            bloodGroup = bgList[listItemCount].text.toString()
            changeBloodGroupBackground(bgList)
        }

        bgList[2].setOnClickListener{
            listItemCount = 2
            bloodGroup = bgList[listItemCount].text.toString()
            changeBloodGroupBackground(bgList)
        }

        bgList[3].setOnClickListener{
            listItemCount = 3
            bloodGroup = bgList[listItemCount].text.toString()
            changeBloodGroupBackground(bgList)
        }

        bgList[4].setOnClickListener{
            listItemCount = 4
            bloodGroup = bgList[listItemCount].text.toString()
            changeBloodGroupBackground(bgList)
        }

        bgList[5].setOnClickListener{
            listItemCount = 5
            bloodGroup = bgList[listItemCount].text.toString()
            changeBloodGroupBackground(bgList)
        }

        bgList[6].setOnClickListener{
            listItemCount = 6
            bloodGroup = bgList[listItemCount].text.toString()
            changeBloodGroupBackground(bgList)
        }

        bgList[7].setOnClickListener{
            listItemCount = 7
            bloodGroup = bgList[listItemCount].text.toString()
            changeBloodGroupBackground(bgList)
        }

        genderList[0].setOnClickListener{
            listItemCount = 0
            gender = "Male"
            changeGenderBackground(genderList, txtGender, icGender)
        }

        genderList[1].setOnClickListener{
            listItemCount = 1
            gender = "Female"
            changeGenderBackground(genderList, txtGender, icGender)
        }
    }

    private fun addTextViewToList(){
        bgList.add(binding.bloodGroups.ap)
        bgList.add(binding.bloodGroups.an)
        bgList.add(binding.bloodGroups.bp)
        bgList.add(binding.bloodGroups.bn)
        bgList.add(binding.bloodGroups.op)
        bgList.add(binding.bloodGroups.on)
        bgList.add(binding.bloodGroups.abp)
        bgList.add(binding.bloodGroups.abn)

        genderList.add(binding.male)
        genderList.add(binding.female)

        icGender.add(binding.icMale)
        icGender.add(binding.icFemale)

        txtGender.add(binding.txtMale)
        txtGender.add(binding.txtFemale)
    }

    private fun changeBloodGroupBackground(list : ArrayList<TextView>){
        for (i in 0 until list.size){
            if (i==listItemCount){
                list[i].setBackgroundResource(R.drawable.bg_light_red_5)
                list[i].setTextColor(resources.getColor(R.color.colorWhite))
            }
            else{
                list[i].setBackgroundResource(R.drawable.bg_light_grey_5)
                list[i].setTextColor(resources.getColor(R.color.colorDarkGrey))
            }
        }
    }

    private fun changeGenderBackground(list : ArrayList<LinearLayout>, name : ArrayList<TextView>, icon : ArrayList<ImageView>){
        for (i in 0 until list.size){
            if (i==listItemCount){
                list[i].setBackgroundResource(R.drawable.bg_light_red_5)
                name[i].setTextColor(resources.getColor(R.color.colorWhite))
                icon[i].setColorFilter(resources.getColor(R.color.colorWhite))
            }
            else{
                list[i].setBackgroundResource(R.drawable.bg_light_grey_5)
                name[i].setTextColor(resources.getColor(R.color.colorDarkGrey))
                icon[i].setColorFilter(resources.getColor(R.color.colorDarkGrey))
            }
        }
    }

    /*------------------------------------------------------TEXT VIEW FUNCTIONALITIES------------------------------------------------------*/

    private fun informationIsValid() : Boolean{
        return when {
            name.isEmpty() -> {
                AlertDialog.showAlertDialog(requireContext(), "Please enter your fullname to create account", "Close")
                false
            }
            gender.isEmpty() -> {
                AlertDialog.showAlertDialog(requireContext(), "Please choose your gender to create account", "Close")
                false
            }
            bloodGroup.isEmpty() -> {
                AlertDialog.showAlertDialog(requireContext(), "Please choose your blood group to create account", "Close")
                false
            }
            dob.isEmpty() -> {
                AlertDialog.showAlertDialog(requireContext(), "Please choose your date of birth to create account", "Close")
                false
            }
            ldd.isEmpty() -> {
                AlertDialog.showAlertDialog(requireContext(), "Please choose your last donation date to create account", "Close")
                false
            }
            location.isEmpty() -> {
                AlertDialog.showAlertDialog(requireContext(), "Please enter your present address to create account", "Close")
                false
            }
            else -> true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun chooseDob(tv : TextView){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireActivity(), { _, year, monthOfYear, dayOfMonth ->

            val m = months[monthOfYear]
            val d = "$dayOfMonth-$m-$year"
            val date = defaultFormat.parse(d)
            val finalDate = format.format(date)
            tv.text = finalDate

            if (tv.id == R.id.dob){
                dob = finalDate
            }
            if (tv.id == R.id.ldd){
                ldd = finalDate
                tempLdd = finalDate
            }

        }, year, month, day)

        dpd.show()
    }
}