package com.sasha.kablam

import android.content.res.Configuration
import android.os.Bundle
//import android.view.inputmethod.InputConnection
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
//import androidx.compose.ui.text.input.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview
import io.ktor.client.*
/*import io.ktor.client.request.*
//import io.ktor.client.response.*
import io.ktor.client.statement.*
//import io.ktor.client.statement.bodyAsText
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition*/
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
//import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import com.sasha.myapplication.ui.theme.BaseTheme
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
//import io.ktor.client.statement.HttpResponse
import io.ktor.client.engine.cio.*
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.*
//import io.ktor.http.path
import kotlinx.coroutines.launch

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
			Content()
                }
            }
        }
    }
}
@Composable
fun Content(modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    val config = LocalConfiguration.current
    Scaffold(
    	snackbarHost = {
		SnackbarHost(hostState = snackbarHostState)
	},
    	modifier = Modifier.fillMaxSize().padding(16.dp)
    ) { innerPadding ->
	val modifier = Modifier.padding(innerPadding)
	val scope = rememberCoroutineScope()
	var name = rememberSaveable { mutableStateOf<String?>(null) }
	val masterModifier = Modifier//.align(Alignment.CenterHorizontally)
	/*OrientationMatch(config = config) {
		Title(masterModifier)
		Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
			if (name.value != null) {
				ServerInfo(name, snackbarHostState, modifier = masterModifier)
			} else {
				Registration(name, modifier = masterModifier)
			}
		}
	}*/
	/*val spinningTransition = rememberInfiniteTransition(label = "spinningTransition")
	val degree by spinningTransition.animateFloat(
		initialValue = 0f,
		targetValue = 360f,
		animationSpec = infiniteRepeatable(TweenSpec(500, easing = EaseInOut)),
		label = "spinningTransition"
	)*/
	//var nameText = rememberSaveable { mutableStateOf("") }
	//var addressText = rememberSaveable { mutableStateOf("") }
	val portrait = config.orientation == Configuration.ORIENTATION_PORTRAIT
	if (portrait) {
		Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
		    Title(masterModifier.fillMaxWidth().align(Alignment.CenterHorizontally))
		    /*Column(modifier = modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center) {
			if (name.value != null) {
				ServerInfo(name, snackbarHostState, modifier = masterModifier)
			} else {
				Registration(name, modifier = masterModifier)
			}
		    }*/
		    Logins(name, snackbarHostState, masterModifier.fillMaxWidth())
		}
	} else {
		Row(modifier = modifier.fillMaxHeight(), horizontalArrangement = Arrangement.Center) {
		    Title(masterModifier.fillMaxHeight())
		    /*Column(modifier = modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center) {
			if (name.value != null) {
				ServerInfo(name, snackbarHostState, modifier = masterModifier)
			} else {
				Registration(name, modifier = masterModifier)
			}
		    }*/
		    Logins(name, snackbarHostState, masterModifier.fillMaxHeight())
		}
	}
    }
}
@Composable
fun Title(modifier: Modifier = Modifier) {
	Column(modifier = modifier) {
	    Image(
		    painter = painterResource(R.drawable.revolver),
		    contentDescription = null,
		    modifier = Modifier.align(Alignment.CenterHorizontally)
	    )
	    Text(
		text = "KABLAM!",
		modifier = Modifier.paddingFromBaseline(bottom = 25.sp).align(Alignment.CenterHorizontally),
		fontSize = 50.sp,
		textAlign = TextAlign.Center
	    )
	}
}
@Composable
fun Logins(name: MutableState<String?> = mutableStateOf(null), snackbarHostState: SnackbarHostState, modifier: Modifier = Modifier) {
	Column(modifier = modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center) {
		if (name.value != null) {
			ServerInfo(name, snackbarHostState, modifier = Modifier)
		} else {
			Registration(name, modifier = Modifier)
		}
	    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    BaseTheme {
    	Content()
    }
}
class FieldState(var defaultMessage: String = "", var defaultText: String = "") {
	var error: Boolean by mutableStateOf(false)
	var text: String by mutableStateOf(defaultText)
	var message: String by mutableStateOf(defaultMessage)
}


@Composable
fun Registration(name: MutableState<String?>, modifier: Modifier = Modifier) {
	Column(verticalArrangement = Arrangement.Center, modifier = modifier.fillMaxWidth()) {
		var state = remember { FieldState() }
		Text(text = "Please enter your name", modifier = modifier.align(Alignment.CenterHorizontally))
		Field("Name", state = state, modifier = modifier.align(Alignment.CenterHorizontally))
		Button(
			onClick = { 
				name.value = state.text
			},
			content = {
				val content = when (state.text) {
					"iPhone 7 Plus" -> "Men"
					"Sans" -> "human.. i remember you're GENOCIDES"
					else -> "Proceed"
				}
				Text(content)
			},
			enabled = !state.error && (state.text != ""),
			modifier = modifier.align(Alignment.CenterHorizontally)
		)
	}
}
@Composable
fun ServerInfo(nameInput: MutableState<String?>, snackbarHostState: SnackbarHostState, modifier: Modifier = Modifier) {
	val name = nameInput.value
	Column(verticalArrangement = Arrangement.Center, modifier = modifier.fillMaxWidth()) {
		var state: FieldState = remember { FieldState(defaultText = "192.168.1.170:6969") }
		state.text = "192.168.1.170:6969"
		val scope = rememberCoroutineScope()
		var dropdownProtocol by remember { mutableStateOf(URLProtocol.HTTP) }
		Text(
			text = "Welcome, $name!",
			modifier = modifier.paddingFromBaseline(bottom = 10.sp).align(Alignment.CenterHorizontally),
			fontSize = 25.sp,
			textAlign = TextAlign.Center
		)
		Button(
			onClick = { nameInput.value = null },
			content = { Text("Change Name") },
			modifier = Modifier.align(Alignment.CenterHorizontally).paddingFromBaseline(bottom = 100.sp)
		)
		Row(horizontalArrangement = Arrangement.Center, modifier = modifier.fillMaxWidth()) {
			var expanded by remember { mutableStateOf(false) }
			var dropdownText by remember { mutableStateOf("HTTP") }
			/*var scrollState = rememberScrollState()
			ExposedDropdownMenuBox(
				expanded = false,
				onExpandedChange = {expanded = !expanded},
				content = ExposedDropdownMenu(
					expanded = expanded,
					onDismissRequest = {
						expanded = false	
					},
					modifier = Modifier,
					scrollState = scrollState,
					matchAnchorWidth = false,
					shape = ,
					containerColor = ,
					tonalElevation = 0.dp,
					shadowElevation = 0.dp,
					border = null,
				) {
					DropdownMenuItem(
						text = { Text("HTTP") },
						onClick = {dropdownContent = "HTTP"; expanded = false},
					)
					DropdownMenuItem(
						text = { Text("HTTPS") },
						onClick = {dropdownContent = "HTTPS"; expanded = false},
					)
				}
			) {
				TextField (
					value = dropdownContent,
					label = { Text("Protocol") },
					modifier = Modifier
						.clickable(onClick = {expanded = !expanded})
						.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
						.width(100.dp)
						.padding(end = 10.dp),
					/*trailingIcon = {
						ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
					},*/
					readOnly = true,
					onValueChange = {},
				)
				/*DropdownMenu (
					expanded = expanded,
					onDismissRequest = {
						expanded = false	
					}
				) {
					DropdownMenuItem(
						text = { Text("HTTP") },
						onClick = {dropdownContent = "HTTP"; expanded = false},
					)
					DropdownMenuItem(
						text = { Text("HTTPS") },
						onClick = {dropdownContent = "HTTPS"; expanded = false},
					)
				}*/
			}*/
			Box {
				var icon: ImageVector = if (expanded) {
					Icons.Rounded.KeyboardArrowUp
				} else {
					Icons.Rounded.KeyboardArrowDown
				}
				OutlinedTextField (
					value = dropdownText, 
					onValueChange = {},
					trailingIcon = {
						Icon(
							icon, 
							"contentDescription",
							Modifier.clickable(onClick = {expanded = !expanded})
						)
					},
					readOnly = true,
					modifier = Modifier
						.width(130.dp)
						.padding(end = 10.dp, bottom = 5.dp, top = 8.dp),
					supportingText = { Text("NOTE: The server currently supports only HTTP") }
				)
				DropdownMenu (
					expanded = expanded,
					onDismissRequest = { expanded = false }
				) {
					DropdownMenuItem(
						text = { Text("HTTP") },
						onClick = {
							dropdownText = "HTTP" 
							dropdownProtocol = URLProtocol.HTTP 
							expanded = false
						},
					)
					DropdownMenuItem(
						text = { Text("HTTPS") },
						onClick = {
							dropdownText = "HTTPS" 
							dropdownProtocol = URLProtocol.HTTPS 
							expanded = false
						},
					)
					
				}
			}
			Field(
				label = "URL", 
				state = state, 
				modifier = modifier,
				validation = { content, state -> 
					var valid = true
					val octet1: Int? = content.split(".")[0].toIntOrNull()
					if (octet1 != null) {
						valid = sanitizeIP(content, state)
					} else {
						state.error = false
						state.message = state.defaultMessage
					}
				},
			)
		}
		Button(
			onClick = { 
				scope.launch {
					try {
						val body: String = Connection().connect(state.text, dropdownProtocol, nameInput.value!!, snackbarHostState)
					} catch (e: Exception) {
						snackbarHostState.showSnackbar(message = "$e")
					}
				}
			},
			content = { Text("Connect") },
			modifier = Modifier.align(Alignment.CenterHorizontally),
			enabled = !state.error && (state.text != "")
		)
	}
}
@Composable
fun Field (
	label: String, 
	state: FieldState = remember { FieldState() }, 
	validation: (String, FieldState) -> Unit = { content, state ->
			if (content == null) {
				state.error = true
				state.message = "An error happened"
			} else {
				state.error = false
				state.message = ""
			}
	},
	modifier: Modifier = Modifier
) {
	var textFieldContent = rememberSaveable { mutableStateOf<String>(state.defaultText) }
	validation(textFieldContent.value, state)
	state.text = textFieldContent.value
	OutlinedTextField (
		value = textFieldContent.value,
		onValueChange = { value -> 
			textFieldContent.value = value
			state.text = value 
			validation(value, state) 
		},
		label = { Text(label) },
		/*trailingIcon = { 
			Image(
				painter = painterResource(R.drawable.revolver),
				contentDescription = null,
				modifier = Modifier
					.size(50.dp)
			)
		},*/
		isError = state.error,
		singleLine = true,
		supportingText = {
			Text(state.message)
		},
		modifier = modifier.padding(top = 0.dp)
	)
}
class Connection() {
	private val client = HttpClient(CIO) {
		install(WebSockets) {
				contentConverter = KotlinxWebsocketSerializationConverter(Json)	
				pingIntervalMillis = 1000
		}
		engine {
			/*connectTimeout = 5000
        		socketTimeout = 5000*/
		}
	}
	suspend fun connect(addressInput: String, urlProtocol: URLProtocol, name: String, snackbarHostState: SnackbarHostState): String {
		val address = addressInput.split(":")
		val host = address[0]
		val port = address[1].toInt()
		var result = "Unknown"
		val response = client.webSocket(method = HttpMethod.Get, host = host, port = port, path = "/android") {
			send(Frame.Text("hello from client"))
			println("test")
			incoming.consumeEach { frame ->
				result = "Unknown"
				when (frame) {
					is Frame.Text -> {
						val frameText = (frame as Frame.Text).readText()
						//snackbarHostState.showSnackbar(message = frameText)
						result = frameText
					}
					is Frame.Close -> {
						//snackbarHostState.showSnackbar(message = "Websocket closed")
						result = "Closed"
						println("closed")
						//return@loop
					}
					else -> snackbarHostState.showSnackbar(message = "Unknown message")
				}
				println("frame")
				snackbarHostState.showSnackbar(message = result)
			}
		}
		/*client.post{
			url {
				protocol = urlProtocol
				host = "$address"
				path("android")
				setBody(name)
			}
		}
		//println(response.status)*/
		return result
	}
}

fun sanitizeIP(input: String, state: FieldState): Boolean {
	var valid = true
	val octets: List<String> = input.split(".")
	var iteration = 1
	for (octet in octets) {
		var int: Int? = octet.toIntOrNull()
		if (iteration == 4) {
			val split = octet.split(":")
			int = split[0].toIntOrNull()
			if (split.size == 2) {
				val port: Int? = split[1].toIntOrNull()	
				if (port == null || port < 0 || port > 65535) {
					valid = false
				}
			}
			if (split.size > 2) {
				valid = false
				break
			}
		}
		if (int == null || int > 255 || int < 0) {
			valid = false
			break
		}
		iteration++
	}
	if (octets.size != 4 || !valid) {
		state.error = true
		state.message = "Not a valid IP address"
	} else {
		state.error = false
		state.message = state.defaultMessage
	}	
	return valid
}
@Composable
inline fun OrientationMatch(config: Configuration, modifier: Modifier = Modifier, composables: @Composable () -> Unit): Unit {
	when (config.orientation) {
		Configuration.ORIENTATION_PORTRAIT -> { 
			Column(
				verticalArrangement = Arrangement.Center,
				modifier = modifier
			) {
				//composables
				Text("portrait", modifier = Modifier.padding(top = 200.dp))
			}
		}
		else -> { 
			Row(
				horizontalArrangement = Arrangement.Center,
				modifier = modifier
			) {
				//composables
				Text("landscape", modifier = Modifier.padding(top = 200.dp))
			}
		}
	}
}

