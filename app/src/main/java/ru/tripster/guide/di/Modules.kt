package ru.tripster.guide.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.tripster.guide.ui.MainFragmentViewModel
import ru.tripster.guide.ui.MainViewModel
import ru.tripster.guide.ui.bottomNavigation.calendar.CalendarContainerViewModel
import ru.tripster.guide.ui.bottomNavigation.orders.OrdersContainerViewModel
import ru.tripster.guide.ui.bottomNavigation.profile.ProfileContainerViewModel
import ru.tripster.guide.ui.calendar.CalendarViewModel
import ru.tripster.guide.ui.fragments.authorization.login.LoginViewModel
import ru.tripster.guide.ui.fragments.calendar.CalendarFiltrationBottomSheetViewModel
import ru.tripster.guide.ui.fragments.calendar.CalendarOrderViewModel
import ru.tripster.guide.ui.fragments.calendar.chooseDate.CalendarChooseDateViewModel
import ru.tripster.guide.ui.fragments.calendar.closeTime.CalendarCloseTimeViewModel
import ru.tripster.guide.ui.fragments.calendar.orders.CalendarDateOrderViewModel
import ru.tripster.guide.ui.fragments.cancel.OrderCancelViewModel
import ru.tripster.guide.ui.fragments.chat.ChatViewModel
import ru.tripster.guide.ui.fragments.chat.RulesOfViewContactBottomSheetViewModel
import ru.tripster.guide.ui.fragments.confirmOrder.ChooseTimeBottomSheetViewModel
import ru.tripster.guide.ui.fragments.confirmOrder.ConfirmOrEditOrderViewModel
import ru.tripster.guide.ui.fragments.confirmOrder.EditPriceBottomSheetViewModel
import ru.tripster.guide.ui.fragments.deleteAcount.DeleteAccountViewModel
import ru.tripster.guide.ui.fragments.home.HomeViewModel
import ru.tripster.guide.ui.fragments.menudebug.MenuDebugViewModel
import ru.tripster.guide.ui.fragments.notifications.NotificationsViewModel
import ru.tripster.guide.ui.fragments.order.OrderViewModel
import ru.tripster.guide.ui.fragments.orderDetails.groupTour.OrderDetailsGroupTourViewModel
import ru.tripster.guide.ui.fragments.orderDetails.groupTour.userDetails.UserOrderGroupTourDetailsViewModel
import ru.tripster.guide.ui.fragments.orderDetails.individual.OrderDetailsIndividualViewModel
import ru.tripster.guide.ui.fragments.profile.ProfileViewModel
import ru.tripster.guide.ui.fragments.splashScreen.SplashScreenViewModel

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { MainFragmentViewModel() }
    viewModel { OrdersContainerViewModel() }
    viewModel { CalendarContainerViewModel() }
    viewModel { ProfileContainerViewModel() }
    viewModel { HomeViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { SplashScreenViewModel(get(), get(), get(), get()) }
    viewModel { OrderViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { OrderDetailsIndividualViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel {
        ConfirmOrEditOrderViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel { EditPriceBottomSheetViewModel() }
    viewModel { UserOrderGroupTourDetailsViewModel(get(), get(), get(), get(), get()) }
    viewModel { ChooseTimeBottomSheetViewModel(get()) }
    viewModel { CalendarViewModel(get()) }
    viewModel { ChatViewModel(get(), get(), get(), get()) }
    viewModel { RulesOfViewContactBottomSheetViewModel(get()) }
    viewModel { OrderCancelViewModel(get(), get(), get(), get(), get()) }
    viewModel { OrderDetailsGroupTourViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { CalendarDateOrderViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { CalendarOrderViewModel(get(), get(), get(), get(), get()) }
    viewModel { CalendarChooseDateViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { CalendarFiltrationBottomSheetViewModel(get()) }
    viewModel { CalendarCloseTimeViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { MenuDebugViewModel(get(), get(), get()) }
    viewModel { DeleteAccountViewModel(get()) }
    viewModel { NotificationsViewModel(get(), get()) }
}