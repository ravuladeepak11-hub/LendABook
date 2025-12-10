package uk.ac.tees.mad.lendabook.presentation.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import uk.ac.tees.mad.lendabook.data.model.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val viewModel: ChatViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initPublicChat()  // CHANGED: Now public, no params
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Public Book Chat") }) },  // CHANGED: Descriptive title
        bottomBar = {
            Row(Modifier.padding(8.dp)) {
                OutlinedTextField(
                    value = uiState.currentMessage,
                    onValueChange = { viewModel.onEvent(ChatUiEvent.MessageChange(it)) },
                    placeholder = { Text("Type a message") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = { viewModel.onEvent(ChatUiEvent.OnSendChat) }) {
                    Text("Send")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(uiState.messages) { msg ->
                val isMine = msg.senderId == uiState.currentUserId  // CHANGED: Dynamic UID check
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = if (isMine) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Surface(
                        color = if (isMine)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Text(
                            text = "${msg.senderId}: ${msg.content}",  // NEW: Show UID + content
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "LendABook – Public Chat Screen")
@Composable
fun ChatScreenPreview() {
    // Mock state for preview
    val mockUiState = ChatUiState(
        messages = listOf(
            Message(senderId = "user123", content = "Hey! Is 'Clean Code' available?", timestamp = 1L),
            Message(senderId = "user456", content = "Yes! Great condition.", timestamp = 2L),
            Message(senderId = "user123", content = "Can I pick up Saturday?", timestamp = 3L),
            Message(senderId = "user456", content = "2pm at town center.", timestamp = 4L),
            Message(senderId = "user123", content = "See you then!", timestamp = 5L)
        ),
        currentUserId = "user123"  // Mock current UID
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Public Book Chat") },  // CHANGED: Public title
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Type a message") },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                )
                Spacer(Modifier.width(12.dp))
                Button(onClick = {}, shape = MaterialTheme.shapes.large) {
                    Text("Send")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mockUiState.messages) { msg ->
                val isMine = msg.senderId == mockUiState.currentUserId
                Box(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        color = if (isMine)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .align(if (isMine) Alignment.CenterEnd else Alignment.CenterStart)
                            .widthIn(max = 300.dp)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "${msg.senderId}: ${msg.content}",  // NEW: Show UID + content
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isMine)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// Simple data class for preview only
private data class ChatMessage(val senderId: String, val content: String)