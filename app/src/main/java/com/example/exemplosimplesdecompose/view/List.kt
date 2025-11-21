package com.example.exemplosimplesdecompose.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.exemplosimplesdecompose.R
import com.example.exemplosimplesdecompose.data.Coordinates
import com.example.exemplosimplesdecompose.data.GasStation
import org.json.JSONArray
import org.json.JSONObject
fun gasStationToJson(gasStation: GasStation): JSONObject {
    return JSONObject().apply {
        put("name", gasStation.name)
        put("precoAlcool", gasStation.precoAlcool)
        put("precoGasolina", gasStation.precoGasolina)
        put("dataCadastro", gasStation.dataCadastro)
        put("lat", gasStation.coord.lat)
        put("lgt", gasStation.coord.lgt)
    }
}

fun jsonToGasStation(json: JSONObject?): GasStation {
    val name = json?.optString("name", "") ?: ""
    val precoAlcool = json?.optDouble("precoAlcool", 0.0) ?: 0.0
    val precoGasolina = json?.optDouble("precoGasolina", 0.0) ?: 0.0
    val dataCadastro = json?.optLong("dataCadastro", System.currentTimeMillis()) ?: System.currentTimeMillis()
    val lat = json?.optDouble("lat", 41.40338) ?: 41.40338
    val lgt = json?.optDouble("lgt", 2.17403) ?: 2.17403
    return GasStation(name, precoAlcool, precoGasolina, Coordinates(lat, lgt), dataCadastro)
}

fun stringToJson_Safe(jsonString: String): JSONObject? {
    return try {
        JSONObject(jsonString)
    } catch (e: Exception) {
        null
    }
}

const val SHARED_FILE_NAME = "gasStationListJSON"
const val KEY_GAS_STATIONS = "gasStations"

fun saveGasStationListJSON(context: Context, gasStationList: List<GasStation>){
    Log.v("PDM25","Salvando lista de postos em JSON")
    val sp: SharedPreferences = context.getSharedPreferences(SHARED_FILE_NAME, Context.MODE_PRIVATE)
    val editor = sp.edit()
    val jsonArray = JSONArray()
    gasStationList.forEach { gasStation ->
        jsonArray.put(gasStationToJson(gasStation))
    }
    editor.putString(KEY_GAS_STATIONS, jsonArray.toString())
    editor.apply()
}

fun getGasStationListJSON(context: Context): List<GasStation>{
    val sp: SharedPreferences = context.getSharedPreferences(SHARED_FILE_NAME, Context.MODE_PRIVATE)
    val aux: String = sp.getString(KEY_GAS_STATIONS, "[]").toString()
    val gasStationList = mutableListOf<GasStation>()

    try {
        val jsonArray = JSONArray(aux)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            gasStationList.add(jsonToGasStation(jsonObject))
        }
    } catch (e: Exception) {
        Log.e("PDM25", "Erro ao carregar lista de postos JSON: ${e.message}")
    }
    return gasStationList
}

fun deleteGasStationJSON(context: Context, gasStationToDelete: GasStation) {
    val currentList = getGasStationListJSON(context).toMutableList()
    val stationIndex = currentList.indexOfFirst {
        it.name == gasStationToDelete.name &&
                it.precoAlcool == gasStationToDelete.precoAlcool &&
                it.precoGasolina == gasStationToDelete.precoGasolina &&
                it.dataCadastro == gasStationToDelete.dataCadastro
    }

    if (stationIndex != -1) {
        currentList.removeAt(stationIndex)
        saveGasStationListJSON(context, currentList)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListofGasStations(navController: NavHostController, posto:String) {
    val context= LocalContext.current
    var postosComp by remember { mutableStateOf(getGasStationListJSON(context)) }

    LaunchedEffect(posto) {
        if (posto.isNotEmpty() && posto != "0.0") {
            postosComp = getGasStationListJSON(context)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.list_title)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("mainalcgas/-1/Novo Posto/0.0/0.0/0.0/0.0") }
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.add_gas_station_desc))
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(postosComp) { item ->
                Card(
                    onClick = {
                        val stationJson = gasStationToJson(item).toString()
                        navController.navigate("detalhesPosto/${Uri.encode(stationJson)}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${stringResource(R.string.alcool_price_label)}: R$ ${item.precoAlcool}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${stringResource(R.string.gasolina_price_label)}: R$ ${item.precoGasolina}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}