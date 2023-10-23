package ru.tripster.di

import org.koin.dsl.module
import ru.tripster.core.dispatcher.BaseCoroutineDispatcherProvider
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.*
import ru.tripster.domain.interactors.chat.*
import ru.tripster.domain.interactors.confirmOrEdit.*
import ru.tripster.domain.interactors.events.EventCountersUseCase
import ru.tripster.domain.interactors.events.GetCurrentGteEventUseCase
import ru.tripster.domain.interactors.events.GetOrderEventUseCase
import ru.tripster.domain.interactors.events.SetCurrentAvailablePlacesUseCase
import ru.tripster.domain.interactors.menuDebug.GetCurrentStageUseCase
import ru.tripster.domain.interactors.menuDebug.SaveCurrentStageUseCase
import ru.tripster.domain.interactors.notifications.GetNotificationStateUseCase
import ru.tripster.domain.interactors.notifications.GetRemindLaterTimeUseCase
import ru.tripster.domain.interactors.notifications.SetNotificationStateUseCase
import ru.tripster.domain.interactors.notifications.SetRemindLaterTimeUseCase
import ru.tripster.domain.interactors.order.*
import ru.tripster.domain.interactors.profile.DeleteAccountUseCase
import ru.tripster.domain.interactors.profile.GetUserProfileInfoUseCase
import ru.tripster.domain.interactors.profile.LogOutUseCase
import ru.tripster.domain.interactors.statistics.StatisticsUseCase
import ru.tripster.domain.interactors.сalendar.*
import ru.tripster.domain.usecases.*
import ru.tripster.domain.usecases.calendar.*
import ru.tripster.domain.usecases.chat.*
import ru.tripster.domain.usecases.confirm.*
import ru.tripster.domain.usecases.events.*
import ru.tripster.domain.usecases.menuDebug.GetCurrentStageUseCaseImpl
import ru.tripster.domain.usecases.menuDebug.SaveCurrentStageUseCaseImpl
import ru.tripster.domain.usecases.notifications.GetNotificationStateUseCaseImpl
import ru.tripster.domain.usecases.notifications.GetRemindLaterTimeUseCaseImpl
import ru.tripster.domain.usecases.notifications.SetNotificationStateUseCaseImpl
import ru.tripster.domain.usecases.notifications.SetRemindLaterTimeUseCaseImpl
import ru.tripster.domain.usecases.order.*
import ru.tripster.domain.usecases.profile.DeleteAccountUseCaseImpl
import ru.tripster.domain.usecases.profile.GetUserProfileInfoUseCaseImpl
import ru.tripster.domain.usecases.profile.LogOutUseCaseImpl
import ru.tripster.domain.usecases.statistics.StatisticsUseCaseImpl

val useCaseModule = module {
    single<CoroutineDispatcherProvider> { BaseCoroutineDispatcherProvider() }
    factory<TestUseCase> { TestUseCaseImpl(get(), get()) }
    factory<LoginUserCase> { LoginUseCaseImpl(get(), get(), get()) }
    factory<SplashUseCase> { SplashUseCaseImpl(get(), get()) }
    factory<EventsUseCase> { EventsUseCaseImpl(get(), get(), get()) }
    factory<ExperienceUseCase> { ExperienceUseCaseImpl(get(), get()) }
    factory<ExperienceFullDataUseCase> { ExperienceFullDataUseCaseImpl(get()) }
    factory<GetOrderEventUseCase> { GetOrderEventUseCaseImpl(get(), get()) }
    factory<OrderCommentsInsertUseCase> { OrderCommentsInsertUseCaseImpl(get(), get()) }
    factory<OrderCommentsChatOpenedUseCase> { OrderCommentsChatOpenedUseCaseImpl(get(), get()) }
    factory<OrderCommentsUseCase> { OrderCommentsUseCaseImpl(get(), get()) }
    factory<OrderDetailsUseCase> { OrderDetailsUseCaseImpl(get(), get()) }
    factory<EventCountersUseCase> { EventCountersUseCaseImpl(get(), get()) }
    factory<ChangeItemInAdapterListUseCase> { ChangeItemInAdapterListUseCaseImpl() }
    factory<TicketsListIdCountUseCase> { TicketsListIdCountUseCaseImpl() }
    factory<GetAdapterListModelUseCase> { GetAdapterListModelUseCaseImpl() }
    factory<CountTotalPriceUseCase> { CountTotalPriceUseCaseImpl() }
    factory<ChooseTourHourUseCase> { ChooseTourHourUseCaseImpl() }
    factory<OrderConfirmOrEditUseCase> { OrderConfirmUseCaseImpl(get(), get()) }
    factory<OrderPostCommentUseCase> { OrderPostCommentUseCaseImpl(get(), get()) }
    factory<GetGuidesScheduleUseCase> { GetGuidesScheduleUseCaseImpl(get(), get(), get()) }
    factory<CancelOrderUseCase> { CancelOrderUseCaseImpl(get(), get()) }
    factory<GetCurrentStageUseCase> { GetCurrentStageUseCaseImpl(get(), get()) }
    factory<GetNotificationStateUseCase> { GetNotificationStateUseCaseImpl(get(), get()) }
    factory<SetNotificationStateUseCase> { SetNotificationStateUseCaseImpl(get(), get()) }
    factory<SaveCurrentStageUseCase> { SaveCurrentStageUseCaseImpl(get(), get()) }
    factory<GetGuideEmailUseCase> { GetGuideEmailUseCaseImpl(get()) }
    factory<GetRemindLaterTimeUseCase> { GetRemindLaterTimeUseCaseImpl(get(), get()) }
    factory<SetRemindLaterTimeUseCase> { SetRemindLaterTimeUseCaseImpl(get(), get()) }
    factory<GetGuideIdUseCase> { GetGuideIdUseCaseImpl(get()) }
    factory<GetMenuItemUseCase> { GetMenuItemUseCaseImpl(get()) }
    factory<GetRejectInfoUseCase> { GetRejectInfoUseCaseImpl(get(), get()) }
    factory<GetCurrentGteEventUseCase> { GetCurrentGteEventUseCaseImpl(get(), get()) }
    factory<GetUserProfileInfoUseCase> { GetUserProfileInfoUseCaseImpl(get(), get(), get()) }
    factory<DeleteAccountUseCase> { DeleteAccountUseCaseImpl(get(), get()) }
    factory<StatisticsUseCase> { StatisticsUseCaseImpl(get(), get()) }
    factory<LogOutUseCase> { LogOutUseCaseImpl(get(), get()) }
    factory<OrderCounterUseCase> { OrderCounterUseCaseImpl(get(), get()) }
    factory<GetGuideDateScheduleUseCase> { GetGuideDateScheduleUseCaseImpl(get(), get(), get()) }
    factory<EditTravelerContactsFieldUseCase> { EditTravelerContactsFieldUseCaseImpl(get(), get()) }
    factory<SetCurrentAvailablePlacesUseCase> { SetCurrentAvailablePlacesUseCaseImpl(get(), get()) }
    factory<BusyIntervalUseCase> { BusyIntervalUseCaseImpl(get(), get()) }
    factory<SetExperienceIdUseCase> { ExperienceIdUseCaseImpl(get()) }
    factory<GetExperienceTitleUseCase> { GetExperienceTitleUseCaseImpl(get()) }
    factory<GetExperienceIdUseCase> { GetExperienceIdUseCaseImpl(get()) }
    factory<CloseTimeUseCase> { CloseTimeUseCaseImpl(get(), get(), get()) }
    factory<ChangeExperienceUseCase> { ChangeExperienceUseCaseImpl(get(), get()) }
}