package com.fphoenixcorneae.permission

interface PermissionsCallback {

    fun onGranted()

    fun onDenied(permissions: List<String>)

    fun onShowRationale(permissionRequest: PermissionRequest)

    fun onNeverAskAgain(permissions: List<String>)
}

/**
 * DSL implementation for [PermissionsCallback].
 */
class PermissionsCallbackDSL : PermissionsCallback {

    private var onGranted: () -> Unit = {}
    private var onDenied: (permissions: List<String>) -> Unit = {}
    private var onShowRationale: (request: PermissionRequest) -> Unit = {}
    private var onNeverAskAgain: (permissions: List<String>) -> Unit = {}

    fun onGranted(func: () -> Unit) {
        onGranted = func
    }

    fun onDenied(func: (permissions: List<String>) -> Unit) {
        onDenied = func
    }

    fun onShowRationale(func: (permissionRequest: PermissionRequest) -> Unit) {
        onShowRationale = func
    }

    fun onNeverAskAgain(func: (permissions: List<String>) -> Unit) {
        onNeverAskAgain = func
    }

    override fun onGranted() {
        onGranted.invoke()
    }

    override fun onDenied(permissions: List<String>) {
        onDenied.invoke(permissions)
    }

    override fun onShowRationale(permissionRequest: PermissionRequest) {
        onShowRationale.invoke(permissionRequest)
    }

    override fun onNeverAskAgain(permissions: List<String>) {
        onNeverAskAgain.invoke(permissions)
    }

}