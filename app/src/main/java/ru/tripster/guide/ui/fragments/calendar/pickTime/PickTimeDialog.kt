package ru.tripster.guide.ui.fragments.calendar.pickTime

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import ru.tripster.guide.databinding.DialogPickCustomTimeBinding


class PickTimeDialog : TimePickerDialog.OnTimeSetListener {

    private lateinit var binding : DialogPickCustomTimeBinding

//    override fun onCreateView(
//        inflater: LayoutInflater,
//         container: ViewGroup?,
//         savedInstanceState: Bundle?
//    ): View {
//        super.onCreateView(inflater, container, savedInstanceState)
//        binding = DialogPickCustomTimeBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.timePicker.setIs24HourView(true)
//
//    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        TODO("Not yet implemented")
    }
}