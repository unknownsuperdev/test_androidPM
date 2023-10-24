package com.fiction.me.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.telephony.TelephonyManager
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.get
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.fiction.domain.analytic.AnalyticFacebook
import com.fiction.domain.analytic.ExtInfo
import com.fiction.domain.model.notification.NotificationType
import com.fiction.domain.model.notification.NotificationType.Companion.toNotificationType
import com.fiction.domain.model.notification.PushNotificationData
import com.fiction.me.BuildConfig
import com.fiction.me.R
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.ActivityMainBinding
import com.fiction.me.extensions.serializable
import com.fiction.me.ui.fragments.onboarding.SplashFragmentDirections
import com.fiction.me.ui.fragments.onboarding.SplashViewModel
import com.fiction.me.extensions.startHeaderTextAnimation
import com.fiction.me.utils.AnalyticAppsflyer
import com.fiction.me.utils.Constants
import com.fiction.me.utils.Constants.Companion.DEFAULT_NOTIFICATION_ID
import com.fiction.me.utils.Constants.Companion.EMAIL_VERIFICATION
import com.fiction.me.utils.Constants.Companion.EMPTY
import com.fiction.me.utils.Constants.Companion.FACEBOOK
import com.fiction.me.utils.Constants.Companion.NOTIFICATION_CLICKED_ACTION
import com.fiction.me.utils.Events.Companion.APP_BECOME_ACTIVE
import com.fiction.me.utils.Events.Companion.APP_BECOME_INACTIVE
import com.fiction.me.utils.Events.Companion.DID_APP_OPENED_AFTER_PUSH
import com.fiction.me.utils.Events.Companion.PUSH_CLICK
import com.fiction.me.utils.Events.Companion.PUSH_ID
import com.fiction.me.utils.Events.Companion.PUSH_TYPE
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.TIMESTAMP
import com.fiction.me.utils.InternalDeepLink
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class MainActivity : AppCompatActivity() {

    private val viewBinding: ActivityMainBinding by viewBinding()
    private val viewModel: MainViewModel by viewModel()
    private val splashViewModel: SplashViewModel by viewModel()
    lateinit var navController: NavController
    private var pushData: PushNotificationData? = null
    private var attribution = ""
    private var waitSplash = true
    private var openWithNotification = false

    private fun navigationStart(needNextScreen: Boolean) {
        lifecycleScope.launch {
            splashViewModel.screens.collect { screens ->
                if (screens == null) return@collect
                isMakeCall(true)
                screens.let {
                    if (needNextScreen) {
                        if (screens.size <= 1) {
                            navigate(SplashFragmentDirections.actionSplashFragmentToExploreFragment())
                        } else {
                            val nextScreens =
                                splashViewModel.getNextOpeningScreenList(1)?.toTypedArray()
                                    ?: arrayOf()
                            navigate(
                                SplashFragmentDirections.actionSplashFragmentToWelcomeFragment(
                                    nextScreens
                                )
                            )
                        }
                    }
                    waitSplash = false
                }
            }
        }
    }

    private fun navigate(directions: NavDirections?) {
        val isPushNotClicked = isPushDataNull()
        if (isPushNotClicked) {
            directions?.let {
                findNavController(R.id.navHostFragment).navigate(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen().also {
                it.setKeepOnScreenCondition {
                    waitSplash
                }
            }
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        navigationStart(savedInstanceState == null)
        supportActionBar?.hide()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        viewBinding.bottomNav.itemIconTintList = null
        viewBinding.bottomNav.setOnApplyWindowInsetsListener(null)
        viewBinding.bottomNav.setPadding(0)
        navController = navHostFragment.navController
        viewBinding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginAndRegistrationFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.signUpWithEmailFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.signInOrSignUpFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.exploreFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.bookSummaryFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.reportFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.readerFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.profileFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.splashFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.welcomeFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.selectGenderFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.pickReadingTimeFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.chooseFavoriteThemeFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.analyzeNovelFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.selectionBooksFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.specialOfferGetCoinFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.coinShopFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.writeToSupportFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.welcomeGiftFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.mainLibraryFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.addedToLibraryFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.finishedFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.browsingHistoryFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.librarySearchFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.browsingHistorySearchFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.addedToLibraryBooksSearchFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.finishedBooksSearchFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.allCurrentReadBooksFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.seeAllFinishedBooksFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.suggestBookSeeAllFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.booksByGenresFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.booksByTagFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.filteredBooksFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.searchMainFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.searchDefaultPageFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.searchFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.youMayAlsoLikeAllBooksFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.youMayAlsoLikeBooksWithPagingFragment -> viewBinding.bottomNav.visibility = View.VISIBLE
                R.id.forgotPasswordFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.changePasswordFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.recoveryEmailFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.verifyEmailAddressFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.registerAndEarnCoinsFragment -> viewBinding.bottomNav.visibility = View.GONE
                R.id.signInWithMailFragment -> viewBinding.bottomNav.visibility = View.GONE
            }
        }

        if (intent?.data?.path == EMAIL_VERIFICATION) {
            getIntentDataForEmailVerify()
        }

        pushData =
            intent?.serializable(Constants.PUSH_NOTIFICATION_EXTRA) as? PushNotificationData
        if (intent?.action == NOTIFICATION_CLICKED_ACTION) {
            openWithNotification = true
            val bookId = intent.extras?.getString("book_id")?.toLong()
            val type = intent.extras?.getString("type")
            val sentAt = intent.extras?.getString("sent_at")
            val pushId = intent.extras?.getString("push_id")?.toInt()
            pushId?.let { viewModel.storePushIdInLocale(it) }
            val pushData = PushNotificationData(
                null,
                null,
                bookId = bookId,
                type = type?.toNotificationType(),
                sentAt = sentAt,
                pushId = pushId ?: DEFAULT_NOTIFICATION_ID,
                timeInMillis = System.currentTimeMillis()
            )
            navigateWithPushData(pushData)
        }
        val deepLinkSub1 = intent.data?.getQueryParameter("deep_link_sub1")
        setAnalyticData(deepLinkSub1)
        printHashKey()
    }

    fun isPushDataNull(): Boolean {
        pushData?.let { navigateWithPushData(it) }
        return pushData == null
    }

    fun isMakeCall(isStart: Boolean) {
        if (isStart) {
            viewModel.run {
                setAndroidData()
                getFirebaseToken()
                setAppsFlyerData()
            }
            if (viewModel.isMakeFbCall) {
                viewModel.setFbData(getFacebookAnalyticData())
            } else viewModel.isMakeFbCall = true
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.data?.path == EMAIL_VERIFICATION) {
            getIntentDataForEmailVerify()
        }
        val push =
            intent?.serializable(Constants.PUSH_NOTIFICATION_EXTRA) as? PushNotificationData
        push?.let { navigateWithPushData(it) }
        if (intent?.action == NOTIFICATION_CLICKED_ACTION) {
            openWithNotification = true
            val bookId = intent.extras?.getString("book_id")?.toLong()
            val type = intent.extras?.getString("type")
            val sentAt = intent.extras?.getString("sent_at")

            val pushData = PushNotificationData(
                null,
                null,
                bookId = bookId,
                type = type?.toNotificationType(),
                sentAt = sentAt,
                pushId = DEFAULT_NOTIFICATION_ID,
                timeInMillis = System.currentTimeMillis()
            )
            navigateWithPushData(pushData)
        }
        setAnalyticData(AnalyticAppsflyer.webInfo)
    }

    private fun setAnalyticData(deepLinkData: String?) {
        AnalyticAppsflyer.webInfo = null
        if (deepLinkData != null) {
            viewModel.isMakeFbCall = false
            val deepLinkSub1Json = JSONObject(deepLinkData)
            val analytic = deepLinkSub1Json.getString("analytic")
            deepLinkSub1Json.remove("analytic")
            if (analytic == FACEBOOK) {
                val fcbAnalyticData = getFacebookAnalyticData()
                viewModel.makeAnalyticCall(analytic, deepLinkSub1Json, fcbAnalyticData)
            } else viewModel.makeAnalyticCall(analytic, deepLinkSub1Json)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onStart() {
        super.onStart()
        checkEventOpenApp()
    }

    private fun checkEventOpenApp() {
        if (!openWithNotification){
            viewModel.trackEvents(APP_BECOME_ACTIVE)
        }else{
            openWithNotification = false
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.trackEvents(APP_BECOME_INACTIVE)
    }

    fun customSelectNavBarItem() {
        navController.backQueue.forEach {
            val destinationId = it.destination.id
            if (destinationId == viewModel.currentDestinationId) return@forEach
            when (destinationId) {
                R.id.exploreFragment -> {
                    changeSelection(isHomeSelected = true)
                    viewModel.currentDestinationId = R.id.exploreFragment
                }

                R.id.mainLibraryFragment -> {
                    if (viewModel.isClearBackStack) {
                        viewModel.isClearBackStack = false
                        navController.popBackStack(R.id.mainLibraryFragment, false)
                    }
                    changeSelection(isLibSelected = true)
                    viewModel.currentDestinationId = R.id.mainLibraryFragment
                }

                R.id.profileFragment -> {
                    changeSelection()
                    viewModel.currentDestinationId = R.id.profileFragment
                }
            }
        }
    }

    fun setIsClearBackStackValue(isClear: Boolean) {
        Log.i("isClearBackStack", ": $isClear ")
        viewModel.isClearBackStack = isClear
    }
    private fun changeSelection(
        isHomeSelected: Boolean = false,
        isLibSelected: Boolean = false
    ) {
        with(viewBinding) {
            val darkGrey: Int = ContextCompat.getColor(this@MainActivity, R.color.dark_grey)
            val white = Color.WHITE
            when {
                isHomeSelected -> {
                    setMenuIcon(
                        R.drawable.ic_explore_checked,
                        R.drawable.ic_library,
                        R.drawable.ic_profile
                    )
                    setMenuTxtColor(white, darkGrey, darkGrey)
                }

                isLibSelected -> {
                    setMenuIcon(
                        R.drawable.ic_explore,
                        R.drawable.ic_library_checked,
                        R.drawable.ic_profile
                    )
                    setMenuTxtColor(darkGrey, white, darkGrey)
                }

                else -> {
                    setMenuIcon(
                        R.drawable.ic_explore,
                        R.drawable.ic_library,
                        R.drawable.ic_profile_checked
                    )
                    setMenuTxtColor(darkGrey, darkGrey, white)
                }
            }
        }
    }

    private fun setMenuIcon(
        exploreIcon: Int,
        libIcon: Int,
        profileIcon: Int,
    ) {
        viewBinding.bottomNav.menu.run {
            get(0).setIcon(exploreIcon)
            get(1).setIcon(libIcon)
            get(2).setIcon(profileIcon)
        }
    }

    private fun setMenuTxtColor(
        homeTxtColor: Int,
        libTxtColor: Int,
        profileTxtColor: Int
    ) {
        val exploreSp = SpannableString(resources.getString(R.string.explore))
        exploreSp.setSpan(ForegroundColorSpan(homeTxtColor), 0, exploreSp.length, 0)
        viewBinding.bottomNav.menu[0].title = exploreSp

        val libSp = SpannableString(resources.getString(R.string.library))
        libSp.setSpan(ForegroundColorSpan(libTxtColor), 0, libSp.length, 0)
        viewBinding.bottomNav.menu[1].title = libSp

        val profileSp = SpannableString(resources.getString(R.string.profile))
        profileSp.setSpan(ForegroundColorSpan(profileTxtColor), 0, profileSp.length, 0)
        viewBinding.bottomNav.menu[2].title = profileSp
    }

    private fun navigateWithPushData(push: PushNotificationData) {
        Log.i("ClickHandle", "navigateWithPushData: $push")
        push.pushId.let {
            viewModel.trackEvents(APP_BECOME_ACTIVE, hashMapOf(REFERRER to it))
        } ?: viewModel.trackEvents(APP_BECOME_ACTIVE)
        push.let {
            sendPushEvent(it, PUSH_CLICK)
            //if (it.timeInMillis + ONE_MINUTE_IN_MILLIS <= System.currentTimeMillis())
            sendPushEvent(it, DID_APP_OPENED_AFTER_PUSH)
        }
        when (push.type) {
            NotificationType.BOOK -> {
                val deepLink = InternalDeepLink.makeCustomDeepLinkToBookSummary(
                    push.bookId ?: -1L,
                    ""
                ).toUri()
                navController.navigate(R.id.home)
                navController.navigate(deepLink)
            }

            NotificationType.USER_PROFILE -> navController.navigate(R.id.profile)
            NotificationType.COINSHOP -> {
                val deeplinkToCoinShop = InternalDeepLink.makeCustomDeepLinkForStore(false).toUri()
                navController.navigate(deeplinkToCoinShop)
            }

            NotificationType.READER -> {
                val deeplink =
                    InternalDeepLink.makeCustomDeepLinkForReader(bookId = push.bookId ?: -1L)
                        .toUri()
                navController.navigate(R.id.home)
                val bookSummaryDeepLink =
                    InternalDeepLink.makeCustomDeepLinkToBookSummary(
                        //isFromPush = true,
                        bookId = push.bookId ?: -1L,
                        bookInfoJson = "",
                        isOpeningFromOnBoarding = false
                    )
                        .toUri()
                navController.navigate(bookSummaryDeepLink)
                navController.navigate(deeplink)
            }

            else -> {
                Log.i("notificationClicked", "onCreate: main")
            }
        }
    }

    private fun sendPushEvent(push: PushNotificationData, eventName: String) {
        val pushType = push.type?.type ?: NotificationType.MAIN
        val sentAt = push.sentAt ?: ""
        viewModel.trackEvents(
            eventName,
            hashMapOf(
                PUSH_TYPE to pushType,
                PUSH_ID to push.pushId,
                TIMESTAMP to sentAt
            )
        )
    }

    private fun getFacebookAnalyticData(): AnalyticFacebook {
        val devModelName = Build.MODEL
        val runtime = Runtime.getRuntime()
        val cpuCores = runtime.availableProcessors()
        val path = Environment.getExternalStorageDirectory().path
        val stat = StatFs(path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        val avlStorageSize = (availableBlocks * blockSize) / (1024 * 1024 * 1024) // available storage size in GB
        val telephonyManager = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var carrierName = telephonyManager.networkOperatorName
        val locale = Locale.getDefault()
        val packageManager = this.packageManager
        val packageName = this.packageName
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val displayMetrics = this.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val pkgInfoVerName = packageInfo.versionName

        val externalStoragePath = Environment.getExternalStorageDirectory().path
        val totalSize = stat.totalBytes

        val extStorageSize = totalSize / (1024 * 1024 * 1024) // external storage size in GB
        val osVer = Build.VERSION.SDK_INT
        val screenWidth = displayMetrics.widthPixels
        val timeZone = TimeZone.getDefault()
        val devTimezone = timeZone.id
        val pkgVerCode = BuildConfig.VERSION_CODE
        val screenDensity = displayMetrics.density
        val extInfoVer = packageInfo.applicationInfo.metaData?.getString("af_ext_info") ?: "empty"
        val devTimezoneAbv = timeZone.getDisplayName(false, TimeZone.SHORT)
        val bundleShortVersion = packageInfo.versionName
        val bundleVersion = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }
        if (carrierName.isEmpty()) carrierName = EMPTY
        val extInfo = ExtInfo(
            packageName,
            avlStorageSize.toString(),
            carrierName,
            cpuCores.toString(),
            devModelName,
            devTimezone,
            devTimezoneAbv,
            extInfoVer,
            extStorageSize.toString(),
            locale.toString(),
            osVer.toString(),
            pkgInfoVerName,
            pkgVerCode.toString(),
            screenDensity.toString(),
            screenHeight.toString(),
            screenWidth.toString()
        )
        return AnalyticFacebook(
            null,
            null,
            "",
            attribution,
            bundleShortVersion,
            bundleVersion.toString(),
            extInfo
        )
    }

    private fun getIntentDataForEmailVerify() {
        viewModel.updateBalance()
        viewBinding.customSnackBarContainer.startHeaderTextAnimation(
            R.string.email_verified,
            viewBinding.customSnackBar,
            viewBinding.close,
            true,
        )
    }

    private fun printHashKey() {
        try {
            val info = this.packageManager.getPackageInfo(
                this.packageName,
                PackageManager.GET_SIGNATURES
            )
            val signatures = info.signatures
            for (signature in signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.i("HashKey", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("HashKey", "printHashKey()", e)
        } catch (e: NoSuchAlgorithmException) {
            Log.e("HashKey", "printHashKey()", e)
        }
    }
}