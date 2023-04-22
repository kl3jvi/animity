package com.kl3jvi.animity.ui.fragments.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kl3jvi.animity.databinding.NotificationsBottomSheetBinding
import com.kl3jvi.animity.utils.collectLatest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationBottomSheetFragment : BottomSheetDialogFragment() {

    private var binding: NotificationsBottomSheetBinding? = null
    private val viewModel by viewModels<NotificationViewModel>()
    private lateinit var pagingController: NotificationsController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotificationsBottomSheetBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagingController = NotificationsController()
        binding?.notificationsRv?.setController(pagingController)
        binding?.notificationsRv?.layoutManager = LinearLayoutManager(requireContext())
        collectLatest(viewModel.notifications) { notificationData ->
            Log.e("NOTIFIACTIONs", notificationData.toString())
            pagingController.submitData(notificationData)
        }
    }
}
