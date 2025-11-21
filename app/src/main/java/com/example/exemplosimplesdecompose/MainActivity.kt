package com.example.exemplosimplesdecompose

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.exemplosimplesdecompose.ui.theme.ExemploSimplesDeComposeTheme
import com.example.exemplosimplesdecompose.view.AlcoolGasolinaPreco
import com.example.exemplosimplesdecompose.view.DetalhesDoPosto
import com.example.exemplosimplesdecompose.view.ListofGasStations
import com.example.exemplosimplesdecompose.view.Welcome

class MainActivity : ComponentActivity() {
    private val LOCATION_PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val LOCATION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var check= false
        check=loadConfig(this)

        requestLocationPermissions()

        setContent {
            ExemploSimplesDeComposeTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(navController = navController, startDestination = "listaDePostos?posto=0.0") {
                    composable("welcome") { Welcome(navController) }
                    composable("mainalcgas/{index}/{name}/{alcool}/{gasolina}/{lat}/{lgt}") { backStackEntry ->
                        val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: -1
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val alcool = backStackEntry.arguments?.getString("alcool")?.toDoubleOrNull() ?: 0.0
                        val gasolina = backStackEntry.arguments?.getString("gasolina")?.toDoubleOrNull() ?: 0.0
                        val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull() ?: 0.0
                        val lgt = backStackEntry.arguments?.getString("lgt")?.toDoubleOrNull() ?: 0.0

                        AlcoolGasolinaPreco(navController, check, index, name, alcool, gasolina, lat, lgt)
                    }
                    composable("listaDePostos?posto={posto}") { backStackEntry ->
                        val posto = backStackEntry.arguments?.getString("posto") ?: "0.0"
                        ListofGasStations(navController, posto)
                    }
                    composable("detalhesPosto/{stationJson}") { backStackEntry ->
                        val stationJson = backStackEntry.arguments?.getString("stationJson")
                        DetalhesDoPosto(navController, stationJson)
                    }
                }
            }
        }
    }

    private fun requestLocationPermissions() {
        val fineLocationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)

        if (fineLocationPermission != PackageManager.PERMISSION_GRANTED || coarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                LOCATION_PERMISSIONS,
                LOCATION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }


    fun loadConfig(context: Context):Boolean{
        val sharedFileName="config_Alc_ou_Gas"
        var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
        var is_75_checked=false
        if(sp!=null) {
            is_75_checked = sp.getBoolean("is_75_checked", false)
        }
        return is_75_checked
    }
}