package com.veterinaria.myapplication.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.veterinaria.myapplication.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeOut: () -> Unit) {
	LaunchedEffect(Unit) {
		delay(2000)
		onTimeOut()
	}
	
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colorScheme.background
	) {
		Column(
			modifier = Modifier.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Image(
				painter = painterResource(id = R.drawable.img_splash),
				contentDescription = "Logo de la veterinaria",
				modifier = Modifier.fillMaxWidth(0.5f)
			)
			Spacer(modifier = Modifier.height(24.dp))
			CircularProgressIndicator()
		}
	}
}