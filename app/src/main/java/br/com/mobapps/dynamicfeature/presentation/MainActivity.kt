package br.com.mobapps.dynamicfeature.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mobapps.dynamicfeature.*
import br.com.mobapps.dynamicfeature.core.Constant
import br.com.mobapps.dynamicfeature.date.Bill
import br.com.mobapps.dynamicfeature.date.Category
import br.com.mobapps.dynamicfeature.presentation.bill.adapter.BillsAdapter
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var manager: SplitInstallManager

    private val moduleExportPDF by lazy { getString(R.string.title_exportpdf) }

    private var dialogProgress: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = SplitInstallManagerFactory.create(this)
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