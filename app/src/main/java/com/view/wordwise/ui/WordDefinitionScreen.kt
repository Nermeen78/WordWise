package com.view.wordwise.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.view.wordwise.viewmodel.WordViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.view.wordwise.data.model.Definition
import com.view.wordwise.data.model.Meaning
import com.view.wordwise.data.model.WordDefinitionEntity
import com.view.wordwise.utils.ConnectivityStatus
import com.view.wordwise.utils.UIState
import retrofit2.HttpException

@Composable
fun WordDefinitionScreen(viewModel: WordViewModel) {
    var word by remember { mutableStateOf(TextFieldValue("")) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Search for word definition",color = Color.Black, modifier = Modifier.padding(16.dp), textDecoration = TextDecoration.Underline)
        NetworkStatusComposable(viewModel)
        OutlinedTextField(
            value = word,
            onValueChange = { word = it },
            label = { Text("Enter a word") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.fetchWordDefinition(word.text) }) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))
        val uiState by viewModel.uiState.collectAsState()

        when (uiState) {
            is UIState.Loading -> {
                CircularProgressIndicator()
            }

            is UIState.Success -> {
                val wordDefinition = (uiState as UIState.Success<WordDefinitionEntity>).data
                Column {
                    Text(text = "Word: ${wordDefinition.word}")

                    OuterList(items = wordDefinition.meanings)
                }
            }

            is UIState.Error -> {
                val exception = (uiState as UIState.Error).exception
                if (exception is HttpException) {
                    if (exception.code() == 404)
                        Text(text = "Error 404: Data not found")
                    else
                        Text(text = "Error: ${exception.message}")
                } else
                    Text(text = "Error: ${exception.message}")
            }

            UIState.Default -> Text("")
        }

    }
}

@Composable
fun InnerListItem(item: Definition) {
    Column {
        Text(text = "Defintion: ${item.definition}")
        Spacer(modifier = Modifier.height(10.dp))
        item.example?.let {
            Text(
                text = "example: $it",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

}

@Composable
fun InnerList(items: List<Definition>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            items.forEach {
                InnerListItem(item = it)
            }
        }
    }
}

@Composable
fun OuterListItem(partOfSpeech: String, items: List<Definition>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        Text(
            text = partOfSpeech,
            color = Color.Gray,
            style = TextStyle(
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.padding(8.dp)
        )
        InnerList(items = items)
    }
}

@Composable
fun OuterList(items: List<Meaning>) {
    LazyColumn(
        Modifier
            .wrapContentHeight()
            .padding(top = 8.dp)
    ) {
        items(items) { innerItems ->
            OuterListItem(innerItems.partOfSpeech, items = innerItems.definitions)
        }
    }
}

@Composable
fun NetworkStatusComposable(viewModel: WordViewModel) {
    val networkStatus by viewModel.networkStatus.collectAsState()

    if (networkStatus != ConnectivityStatus.AVAILABLE) {
        Text(text = "No network available", color = Color.Red, modifier = Modifier.padding(16.dp))
    } else {
        Text(text = "")
    }
}