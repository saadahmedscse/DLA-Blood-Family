package com.caffeine.dlabloodfamily.view.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.caffeine.dlabloodfamily.R
import com.caffeine.dlabloodfamily.databinding.FragmentPostBinding
import com.caffeine.dlabloodfamily.services.model.BloodModel
import com.caffeine.dlabloodfamily.utils.*
import com.caffeine.dlabloodfamily.view.activity.HomeActivity
import com.caffeine.dlabloodfamily.viewmodel.PostViewModel
import com.caffeine.dlabloodfamily.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostFragment : Fragment() {

    private lateinit var binding : FragmentPostBinding
    private val viewModel : PostViewModel by viewModels()
    private val userViewModel : UserViewModel by viewModels()

    private var name : String = ""
    private var quantity : String = ""
    private var date : String = ""
    private var time : String = ""
    private var hospital : String = ""
    private var location : String = ""
    private var desc : String = ""
    private var currentTime : String = ""
    private var bloodGroup : String = ""
    private var poster : String = ""
    private var number : String = ""

    private lateinit var format : SimpleDateFormat
    private lateinit var defaultFormat : SimpleDateFormat
    private lateinit var months : Array<Int>

    private lateinit var bgList : ArrayList<TextView>
    private lateinit var dayTime : ArrayList<TextView>
    private lateinit var bag : ArrayList<TextView>
    private var listItemCount = 0

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater)

        val adUtils = AdUtils.getInstance(requireContext())
        adUtils.showBannerAd(binding.adView)
        adUtils.loadVideoAd()

        userViewModel.getMyInfo()
        userViewModel.myLiveData.observe(viewLifecycleOwner){
            when (it){
                is DataState.Loading -> {}

                is DataState.Success -> {
                    poster = it.data!!.name
                    number = it.data.number
                }

                is DataState.Failed -> {}
            }
        }

        bgList = ArrayList()
        dayTime = ArrayList()
        bag = ArrayList()
        addTextViewToList()
        textItemClickListeners()

        format = SimpleDateFormat("dd-MMM-yyyy")
        defaultFormat = SimpleDateFormat("dd-MM-yyyy")
        months = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

        binding.datePicker.setOnClickListener{
            chooseDate(binding.date)
        }

        binding.postBtn.setOnClickListener{
            initialize()
            if (validate()){
                if (Constants.internetAvailable(requireContext())){
                    val blood = BloodModel(
                        currentTime,
                        Constants.auth.uid!!,
                        name,
                        poster,
                        number,
                        bloodGroup,
                        quantity,
                        date,
                        time,
                        hospital,
                        location,
                        desc,
                        currentTime
                    )
                    viewModel.postABloodRequest(blood)

                    viewModel.bloodLiveData.observe(viewLifecycleOwner){
                        when (it) {
                            is DataState.Loading -> {
                                binding.btnTxt.visibility = View.GONE
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            is DataState.Success -> {
                                adUtils.showVideoAd(requireActivity())
                                SuccessDialog.showAlertDialog(requireContext(), it.data!!, "Close")
                                binding.progressBar.visibility = View.GONE
                                binding.btnTxt.visibility = View.VISIBLE
                            }

                            is DataState.Failed -> {
                                Constants.showSnackBar(requireContext(), binding.root, it.message!!, Constants.SNACK_LONG)
                                binding.progressBar.visibility = View.GONE
                                binding.btnTxt.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                else {
                    Constants.showSnackBar(requireContext(), binding.root, "No internet connection", Constants.SNACK_SHORT)
                }
            }
        }

        return binding.root
    }

    private fun initialize(){
        currentTime = System.currentTimeMillis().toString()
        name = binding.name.text.toString()
        hospital = binding.hospital.text.toString()
        location = binding.location.text.toString()
        desc = binding.desc.text.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun chooseDate(tv : TextView){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireActivity(), { _, year, monthOfYear, dayOfMonth ->

            val m = months[monthOfYear]
            val d = "$dayOfMonth-$m-$year"
            val dat = defaultFormat.parse(d)
            val finalDate = format.format(dat)
            tv.text = finalDate

            date = finalDate

        }, year, month, day)

        dpd.show()
    }

    private fun validate() : Boolean{
        if (name.isEmpty()){
            AlertDialog.showAlertDialog(requireContext(), "It seems you haven't defined the patient name", "Close")
            return false
        }

        else if (bloodGroup.isEmpty()){
            AlertDialog.showAlertDialog(requireContext(), "It seems you haven't choosen the blood group", "Close")
            return false
        }

        else if (quantity.isEmpty()){
            AlertDialog.showAlertDialog(requireContext(), "It seems you haven't choosen the quantity blood bag", "Close")
            return false
        }

        else if (time.isEmpty()){
            AlertDialog.showAlertDialog(requireContext(), "It seems you haven't choosen the donation time", "Close")
            return false
        }

        else if (date.isEmpty()){
            AlertDialog.showAlertDialog(requireContext(), "It seems you haven't choosen the donation date", "Close")
            return false
        }

        else if (hospital.isEmpty()){
            AlertDialog.showAlertDialog(requireContext(), "It seems you haven't defined the hospital name", "Close")
            return false
        }

        else if (location.isEmpty()){
            AlertDialog.showAlertDialog(requireContext(), "PIt seems you haven't defined the hospital location", "Close")
            return false
        }

        else if (desc.isEmpty()){
            AlertDialog.showAlertDialog(requireContext(), "It seems you haven't defined a short description of patient", "Close")
            return false
        }

        else if (poster.isEmpty() || number.isEmpty()){
            AlertDialog.showAlertDialog(requireContext(), "Something went wrong, please try again later", "Close")
            return false
        }

        return true
    }

    /*------------------------------------------------------TEXT VIEW FUNCTIONALITIES------------------------------------------------------*/

    private fun textItemClickListeners() {
        bgList[0].setOnClickListener{
            listItemCount = 0
            bloodGroup = bgList[listItemCount].text.toString()
            changeItemBackground(bgList)
        }

        bgList[1].setOnClickListener{
            listItemCount = 1
            bloodGroup = bgList[listItemCount].text.toString()
            changeItemBackground(bgList)
        }

        bgList[2].setOnClickListener{
            listItemCount = 2
            bloodGroup = bgList[listItemCount].text.toString()
            changeItemBackground(bgList)
        }

        bgList[3].setOnClickListener{
            listItemCount = 3
            bloodGroup = bgList[listItemCount].text.toString()
            changeItemBackground(bgList)
        }

        bgList[4].setOnClickListener{
            listItemCount = 4
            bloodGroup = bgList[listItemCount].text.toString()
            changeItemBackground(bgList)
        }

        bgList[5].setOnClickListener{
            listItemCount = 5
            bloodGroup = bgList[listItemCount].text.toString()
            changeItemBackground(bgList)
        }

        bgList[6].setOnClickListener{
            listItemCount = 6
            bloodGroup = bgList[listItemCount].text.toString()
            changeItemBackground(bgList)
        }

        bgList[7].setOnClickListener{
            listItemCount = 7
            bloodGroup = bgList[listItemCount].text.toString()
            changeItemBackground(bgList)
        }

        dayTime[0].setOnClickListener{
            listItemCount = 0
            time = dayTime[listItemCount].text.toString()
            changeItemBackground(dayTime)
        }

        dayTime[1].setOnClickListener{
            listItemCount = 1
            time = dayTime[listItemCount].text.toString()
            changeItemBackground(dayTime)
        }

        dayTime[2].setOnClickListener{
            listItemCount = 2
            time = dayTime[listItemCount].text.toString()
            changeItemBackground(dayTime)
        }

        dayTime[3].setOnClickListener{
            listItemCount = 3
            time = dayTime[listItemCount].text.toString()
            changeItemBackground(dayTime)
        }

        bag[1].setOnClickListener{
            listItemCount = 1
            quantity = bag[listItemCount].text.toString()
            changeItemBackground(bag)
        }

        bag[2].setOnClickListener{
            listItemCount = 2
            quantity = bag[listItemCount].text.toString()
            changeItemBackground(bag)
        }

        bag[3].setOnClickListener{
            listItemCount = 3
            quantity = bag[listItemCount].text.toString()
            changeItemBackground(bag)
        }

        bag[4].setOnClickListener{
            listItemCount = 4
            quantity = bag[listItemCount].text.toString()
            changeItemBackground(bag)
        }

        bag[5].setOnClickListener{
            listItemCount = 5
            quantity = bag[listItemCount].text.toString()
            changeItemBackground(bag)
        }

        bag[6].setOnClickListener{
            listItemCount = 6
            quantity = bag[listItemCount].text.toString()
            changeItemBackground(bag)
        }

        bag[7].setOnClickListener{
            listItemCount = 7
            quantity = bag[listItemCount].text.toString()
            changeItemBackground(bag)
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

        bag.add(binding.bloodBags.one)
        bag.add(binding.bloodBags.two)
        bag.add(binding.bloodBags.three)
        bag.add(binding.bloodBags.four)
        bag.add(binding.bloodBags.five)
        bag.add(binding.bloodBags.six)
        bag.add(binding.bloodBags.seven)
        bag.add(binding.bloodBags.eight)

        dayTime.add(binding.donationTime.morning)
        dayTime.add(binding.donationTime.noon)
        dayTime.add(binding.donationTime.evening)
        dayTime.add(binding.donationTime.night)
    }

    private fun changeItemBackground(list : ArrayList<TextView>){
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

    /*------------------------------------------------------TEXT VIEW FUNCTIONALITIES------------------------------------------------------*/

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).setAppBarTitle("Post For Blood")
    }
}