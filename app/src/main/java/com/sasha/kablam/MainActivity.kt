package com.sasha.kablam

import android.os.Bundle
import android.view.inputmethod.InputConnection
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview
import io.ktor.client.*
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.sasha.myapplication.ui.theme.BaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseTheme {
	        Surface(
	            modifier = Modifier.fillMaxSize(), 
	            color = MaterialTheme.colorScheme.background
	        ) {
		    Scaffold(modifier = Modifier.fillMaxSize().padding(16.dp)) { innerPadding ->
			val modifier = Modifier.padding(innerPadding)
			val name: String? = null
			Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
			    Image(
				    painter = painterResource(R.drawable.revolver),
				    contentDescription = null,
				    modifier = Modifier.fillMaxWidth()
			    )
			    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
				val innerModifier = Modifier.fillMaxWidth()
				Text(
					text = "KABLAM!",
					modifier = innerModifier,
					fontSize = 50.sp,
					textAlign = TextAlign.Center
				)
				if (name != null) {
				Credentials(name, modifier = innerModifier)
				} else {
				Registration(modifier = innerModifier)
				}
			    }
		    	}
		    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewContent() {
    BaseTheme {
		val modifier = Modifier
		Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
		    Image(
			    painter = painterResource(R.drawable.revolver),
			    contentDescription = null,
			    modifier = Modifier.fillMaxWidth()
		    )
		    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
			val innerModifier = Modifier.fillMaxWidth()
			val name: String? = null
			Text(
				text = "KABLAM!",
				modifier = innerModifier,
				fontSize = 50.sp,
				textAlign = TextAlign.Center
			)
			if (name != null) {
				Credentials(name, modifier = innerModifier)
			} else {
				Registration(modifier = innerModifier)
			}
		    }
	    }
    }
}

@Composable
fun Registration(modifier: Modifier = Modifier) {
	Column(verticalArrangement = Arrangement.Center, modifier = modifier) {
		var error: Boolean by remember { mutableStateOf(false) }
		Text(text = "log in mothertrucka", modifier = modifier)
		Field("Name")
		Button(
			onClick = {},
			content = { Text("Submit") },
			enabled = false
		)
	}
}
@Composable
fun Credentials(name: String, modifier: Modifier = Modifier) {
    	    Text(
    		text = "Welcome, $name!",
    		modifier = modifier,
    		fontSize = 30.sp,
    		textAlign = TextAlign.Center
    	    )
}
@Composable
fun Field(label: String, modifier: Modifier = Modifier) {
	var text: String by remember { mutableStateOf("") }
	var error: Boolean by remember { mutableStateOf(false) }
	TextField (
		value = text,
		onValueChange = { 
			text = it 
			if (it == "pog") {
				error = true
			} else {
				error = false
			}
		},
		label = { Text(label) },
		trailingIcon = { 
			Image(
				painter = painterResource(R.drawable.revolver),
				contentDescription = null,
				modifier = Modifier
					.size(50.dp)
			)
		},
		isError = error,
		singleLine = true,
		supportingText = { Text(text) },
		modifier = modifier
	)
}
