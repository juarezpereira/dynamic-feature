package br.com.mobapps.dynamicfeature

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var manager: SplitInstallManager
    private lateinit var billsAdapter: BillsAdapter
    private lateinit var bills: MutableList<Bill>

    private val moduleExportPDF by lazy { getString(R.string.title_export_report) }

    private var dialogProgress: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = SplitInstallManagerFactory.create(this)

        bills = MutableList(20) {
            val category = when (it) {
                0, 1, 2, 14 -> Category.Food
                3, 4, 5, 12, 13 -> Category.Health
                6, 7, 8, 9, 10, 11-> Category.Clothing
                else -> Category.Recreation
            }

            Bill(description = "Description $it", value = it * 10.0, category = category)
        }

        billsAdapter = BillsAdapter(bills)

        rvBills.layoutManager = LinearLayoutManager(this)
        rvBills.adapter = billsAdapter
        rvBills.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bills, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_export -> onMenuItemExportPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        manager.registerListener(installListener)
        super.onResume()
    }

    override fun onPause() {
        manager.unregisterListener(installListener)
        super.onPause()
    }

    private fun onMenuItemExportPressed() {
        loadAndLaunchModule(moduleExportPDF)
    }

    /**
     * Load a feature by module name.
     * @param name The name of the feature module to load.
     */
    private fun loadAndLaunchModule(name: String) {
        updateProgressMessage("Loading module $name")

        if (manager.installedModules.contains(name)) {
            updateProgressMessage("Already installed")
            dismissProgressMessage()
            onSuccessfulLoad(name, launch = true)
            return
        }

        val request = SplitInstallRequest.newBuilder()
                .addModule(name)
                .build()

        manager.startInstall(request)

        updateProgressMessage("Starting install for $name")
    }

    private fun updateProgressMessage(message: String) {
        val builder = AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)

        dialogProgress?.setMessage(message)
        dialogProgress = dialogProgress ?: builder.create()

        if (dialogProgress?.isShowing == false) {
            dialogProgress?.show()
        }
    }

    private fun dismissProgressMessage() {
        if (dialogProgress?.isShowing == true) {
            dialogProgress?.dismiss()
        }
    }

    private fun onSuccessfulLoad(name: String, launch: Boolean) {
        when (name) {
            Constant.EXPORT_PDF -> if (launch) launchActivity(Constant.EXPORT_PDF)
        }
    }

    private val installListener = SplitInstallStateUpdatedListener {
        state ->
        state.moduleNames()
             .forEach { name ->
                 when (state.status()) {
                     SplitInstallSessionStatus.DOWNLOADING -> {}
                     SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> { }
                     SplitInstallSessionStatus.INSTALLED -> onSuccessfulLoad(name, launch = state.moduleNames().size == 1)
                     SplitInstallSessionStatus.INSTALLING -> { }
                     SplitInstallSessionStatus.FAILED -> {}
                 }
             }
    }

    private fun launchActivity(className: String) {
        Intent().apply { setClassName(Constant.PACKAGE_ONDEMAND_NAME, className) }
                .run { startActivity(this) }
    }

}