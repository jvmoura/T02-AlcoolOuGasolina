package com.example.exemplosimplesdecompose.view

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesDoPosto(navController: NavHostController, stationJson: String?) {
    val context = LocalContext.current
    val gasStation = remember {
        stationJson?.let {
            jsonToGasStation(stringToJson_Safe(Uri.decode(it)))
        }
    }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (gasStation == null || gasStation.name.isEmpty()) {
        Text("Posto não encontrado.", modifier = Modifier.padding(16.dp))
        return
    }

    val currentList = getGasStationListJSON(context)
    val stationIndex = currentList.indexOfFirst {
        it.name == gasStation.name &&
                it.precoAlcool == gasStation.precoAlcool &&
                it.precoGasolina == gasStation.precoGasolina &&
                it.dataCadastro == gasStation.dataCadastro
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.details_title)) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = gasStation.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${stringResource(R.string.detail_alcohol_price)}: R$ ${gasStation.precoAlcool}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${stringResource(R.string.detail_gasoline_price)}: R$ ${gasStation.precoGasolina}",
                style = MaterialTheme.typography.bodyLarge
            )
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            Text(
                text = "${stringResource(R.string.detail_registration_date)}: ${formatter.format(Date(gasStation.dataCadastro))}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${stringResource(R.string.detail_location)}: Lat ${gasStation.coord.lat}, Lgt ${gasStation.coord.lgt}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = {
                    val label = Uri.encode(gasStation.name)
                    val uriString = "geo:${gasStation.coord.lat},${gasStation.coord.lgt}?q=${gasStation.coord.lat},${gasStation.coord.lgt}($label)"
                    val gmmIntentUri = Uri.parse(uriString)

                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

                    mapIntent.setPackage("com.google.android.apps.maps")

                    try {
                        context.startActivity(mapIntent)
                    } catch (e: Exception) {
                        mapIntent.setPackage(null)
                        try {
                            context.startActivity(mapIntent)
                        } catch (e2: Exception) {
                            Toast.makeText(context, "Não foi possível abrir o mapa", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.detail_location))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        navController.navigate("mainalcgas/$stationIndex/${gasStation.name}/${gasStation.precoAlcool}/${gasStation.precoGasolina}/${gasStation.coord.lat}/${gasStation.coord.lgt}")
                    },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text(stringResource(R.string.edit_button))
                }

                Button(
                    onClick = { showDeleteConfirmation = true },
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text(stringResource(R.string.delete_button))
                }
            }
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(stringResource(R.string.confirm_delete_title)) },
            text = { Text(stringResource(R.string.confirm_delete_message)) },
            confirmButton = {
                Button(
                    onClick = {
                        deleteGasStationJSON(context, gasStation)
                        showDeleteConfirmation = false
                        navController.navigate("listaDePostos?posto=refresh") {
                            popUpTo("listaDePostos?posto={posto}") { inclusive = true }
                        }
                    }
                ) {
                    Text(stringResource(R.string.confirm_yes))
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteConfirmation = false }
                ) {
                    Text(stringResource(R.string.confirm_no))
                }
            }
        )
    }
}