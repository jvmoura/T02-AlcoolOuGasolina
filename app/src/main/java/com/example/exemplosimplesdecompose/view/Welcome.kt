package com.example.exemplosimplesdecompose.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.exemplosimplesdecompose.R
import kotlinx.coroutines.delay

@Composable
fun Welcome(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000)

        navController.navigate("listaDePostos?posto=0.0") {
            popUpTo("welcome") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.welcome_title))
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = stringResource(R.string.welcome_image_desc),
            modifier = Modifier.size(128.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}