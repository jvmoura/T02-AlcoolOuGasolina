package com.example.exemplosimplesdecompose.view

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.exemplosimplesdecompose.R
import com.example.exemplosimplesdecompose.data.Coordinates
import com.example.exemplosimplesdecompose.data.GasStation
import com.google.android.gms.location.LocationServices
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun fetchCurrentLocation(context: Context, onLocationFound: (Coordinates) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onLocationFound(Coordinates(location.latitude, location.longitude))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlcoolGasolinaPreco(
    navController: NavHostController,
    check: Boolean,
    index: Int,
    initialName: String,
    initialAlcool: Double,
    initialGasolina: Double,
    initialLat: Double,
    initialLgt: Double
) {
    val context = LocalContext.current
    var alcool by remember { mutableStateOf(if (initialAlcool > 0.0) initialAlcool.toString() else "") }
    var gasolina by remember { mutableStateOf(if (initialGasolina > 0.0) initialGasolina.toString() else "") }
    var nomeDoPosto by remember { mutableStateOf(initialName) }
    var checkedState by remember { mutableStateOf(check) }
    var resultado by remember { mutableStateOf(context.getString(R.string.default_result_text)) }
    val isDark = isSystemInDarkTheme()
    var currentCoords by remember {
        mutableStateOf(
            if (initialLat != 0.0 && initialLgt != 0.0) Coordinates(initialLat, initialLgt)
            else Coordinates(0.0, 0.0)
        )
    }

    var isEditing by remember { mutableStateOf(index != -1) }

    LaunchedEffect(key1 = Unit) {
        if (index == -1) {
            val hasFinePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            val hasCoarsePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

            if (hasFinePermission || hasCoarsePermission) {
                fetchCurrentLocation(context) { coords ->
                    currentCoords = coords
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEditing) stringResource(R.string.title_edit_station) else stringResource(R.string.title_new_station))
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = alcool,
                    onValueChange = { alcool = it.replace(",", ".") },
                    label = { Text(stringResource(R.string.alcool_price_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = if (isDark) Color.White else Color.Black,
                        unfocusedTextColor = if (isDark) Color.White else Color.Black,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = if (isDark) Color(0xFFE6E1E5) else Color(0xFF1C1B1F),
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedBorderColor = if (isDark) Color(0xFFCAC4D0) else Color(0xFF49454F),
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
                OutlinedTextField(
                    value = gasolina,
                    onValueChange = { gasolina = it.replace(",", ".") },
                    label = { Text(stringResource(R.string.gasolina_price_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = if (isDark) Color.White else Color.Black,
                        unfocusedTextColor = if (isDark) Color.White else Color.Black,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = if (isDark) Color(0xFFE6E1E5) else Color(0xFF1C1B1F),
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedBorderColor = if (isDark) Color(0xFFCAC4D0) else Color(0xFF49454F),
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
                OutlinedTextField(
                    value = nomeDoPosto,
                    onValueChange = { nomeDoPosto = it },
                    label = { Text(stringResource(R.string.gas_station_name_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = if (isDark) Color.White else Color.Black,
                        unfocusedTextColor = if (isDark) Color.White else Color.Black,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = if (isDark) Color(0xFFE6E1E5) else Color(0xFF1C1B1F),
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedBorderColor = if (isDark) Color(0xFFCAC4D0) else Color(0xFF49454F),
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = if (checkedState) "75%" else "70%",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Switch(
                        modifier = Modifier.semantics { contentDescription = context.getString(R.string.switch_content_description) },
                        checked = checkedState,
                        onCheckedChange = { checkedState = it
                            saveConfig(context,checkedState)
                        },
                        thumbContent = {
                            if (checkedState) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        }
                    )
                }

                Button(
                    onClick = {
                        val precoAlcool = alcool.toDoubleOrNull()
                        val precoGasolina = gasolina.toDoubleOrNull()

                        if (precoAlcool != null && precoGasolina != null && precoGasolina > 0) {
                            val fatorCorte = if (checkedState) 0.75 else 0.70

                            val limiteAlcool = precoGasolina * fatorCorte

                            resultado = if (precoAlcool <= limiteAlcool) {
                                context.getString(R.string.result_alcohol_is_better)
                            } else {
                                context.getString(R.string.result_gasoline_is_better)
                            }
                        } else {
                            resultado = context.getString(R.string.result_invalid_input)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.calculate_button))
                }

                Text(
                    text = resultado,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Button(
                    onClick = {
                        val precoAlcool = alcool.toDoubleOrNull()
                        val precoGasolina = gasolina.toDoubleOrNull()
                        val stationName = nomeDoPosto.ifEmpty { context.getString(R.string.gas_station_name_label) }

                        if (precoAlcool != null && precoGasolina != null) {
                            val newStation = GasStation(
                                name = stationName,
                                precoAlcool = precoAlcool,
                                precoGasolina = precoGasolina,
                                coord = currentCoords,
                                dataCadastro = System.currentTimeMillis()
                            )

                            val currentList = getGasStationListJSON(context).toMutableList()

                            if (isEditing && index in currentList.indices) {
                                currentList[index] = newStation.copy(dataCadastro = currentList[index].dataCadastro)
                            } else {
                                currentList.add(newStation)
                            }

                            saveGasStationListJSON(context, currentList)
                            navController.navigate("listaDePostos?posto=refresh") {
                                popUpTo("listaDePostos?posto={posto}") { inclusive = true }
                            }
                        } else {
                            resultado = context.getString(R.string.result_invalid_input)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.save_button))
                }
            }
        }
    }
}

fun saveConfig(context: Context, switch_state:Boolean){
    val sharedFileName="config_Alc_ou_Gas"
    var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    var editor = sp.edit()
    editor.putBoolean("is_75_checked",switch_state)
    editor.apply()
}