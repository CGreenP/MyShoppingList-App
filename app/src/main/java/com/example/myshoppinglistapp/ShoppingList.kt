package com.example.myshoppinglistapp

// Import necessary Compose libraries
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Data class representing a shopping item
data class ShoppingItem(
    val id: Int, // Unique identifier for the item
    var name: String, // Name of the item
    var quantity: Int, // Quantity of the item
    var isEditing: Boolean = false // Flag indicating if the item is being edited
)

/**
 * Composable function for the main shopping list app UI.
 *
 * @param paddingValues Padding values for the app content.
 */
@Composable
fun ShoppingListApp(paddingValues: PaddingValues) {
    // State variables for managing the shopping list and UI elements
    val sItems = remember { mutableStateListOf<ShoppingItem>() } // List of shopping items
    var showDialog by remember { mutableStateOf(false) } // Flag to control the add item dialog visibility
    var itemNames by remember { mutableStateOf("") } // Input for item name
    var itemQuantity by remember { mutableStateOf("") } // Input for item quantity
    val listState = rememberLazyListState() // State of the LazyColumn
    var showButton by remember { mutableStateOf(false) } // Flag to control the scroll to top button visibility

    val coroutineScope = rememberCoroutineScope()

    // Main layout container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Column to arrange the content vertically
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            // Button to trigger the add item dialog
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add Item")
            }
            // Message displayed when the list is empty
            if (sItems.isEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No items to show",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            // LazyColumn to display the shopping list items
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), state = listState
            ) {
                items(sItems, key = { it.id }) { item ->
                    // Display either the item editor or the item view based on the isEditing flag
                    if (item.isEditing) {
                        ShoppingItemEditor(item = item, onEditComplete = { updatedItem ->
                            val index = sItems.indexOf(item)
                            sItems[index] = updatedItem
                        })
                    } else {
                        ShoppingListItem(item = item, onEditClick = {
                            val index = sItems.indexOf(item)
                            sItems[index] = sItems[index].copy(isEditing = true)
                        }, onDeleteClick = {
                            sItems.remove(item)
                        })
                    }
                }
            }
        }

        // Scroll to top button
        if (showButton && sItems.isNotEmpty()) {
            FilledIconButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }, modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to Top"
                )
            }
        }

    }

    // Show/hide the scroll to top button based on scroll state
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex > 0 }.collect {
            showButton = it
        }
    }

    // Add item dialog
    if (showDialog) {
        var inputErrorI by remember { mutableStateOf(false) } // Flag for item name input error
        var inputErrorQ by remember { mutableStateOf(false) } // Flag for item quantity input error
        var inputErrorQNum by remember { mutableStateOf(false) } // Flag for item quantity input error
        val focusRequester = remember { FocusRequester() }

        AlertDialog(onDismissRequest = {
            showDialog = false
            itemNames = ""
            itemQuantity = ""
        }, confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    if (itemNames.isNotBlank() && itemQuantity.isNotBlank()) {
                        inputErrorQ = false
                        if (itemQuantity.toIntOrNull() == null) {
                            inputErrorQNum = true
                        } else {
                            inputErrorQNum = false
                            sItems.add(
                                ShoppingItem(
                                    id = sItems.lastOrNull()?.id?.plus(1) ?: 1,
                                    name = itemNames,
                                    quantity = itemQuantity.toInt()
                                )
                            )
                            showDialog = false
                            itemNames = ""
                            itemQuantity = ""
                        }
                    } else {
                        inputErrorI = itemNames.isBlank()
                        inputErrorQ = itemQuantity.isBlank()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.Green
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Add")
                }
                Button(onClick = { showDialog = false }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Cancel")
                }
            }
        }, title = {
            Text(
                text = "Add Shopping Item",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 1,
                softWrap = false,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                minLines = 1
            )
        }, text = {
            Column {
                // Input field for item name
                OutlinedTextField(value = itemNames,
                    onValueChange = { itemNames = it },
                    label = { Text(text = "Item Name") },
                    isError = inputErrorI,
                    supportingText = {
                        if (inputErrorI) {
                            Text(text = "Item Name cannot be empty")
                        }
                    },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next // Move focus to the next field when "Next" is pressed
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusRequester.requestFocus() // Request focus for the quantity field
                    }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                // Input field for item quantity
                OutlinedTextField(value = itemQuantity,
                    onValueChange = { itemQuantity = it },
                    label = { Text(text = "Item Quantity") },
                    isError = inputErrorQ || inputErrorQNum,
                    supportingText = {
                        if (inputErrorQ) {
                            Text(text = "Item Quantity cannot be empty")
                        } else if (inputErrorQNum) {
                            if (itemQuantity.isBlank()) {
                                Text(text = "Item Quantity cannot be empty")
                            } else if (itemQuantity.toIntOrNull() == null) {
                                Text(text = "Item Quantity must be a number")
                            } else {
                                inputErrorQNum = false
                            }
                        }
                    },
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .focusRequester(focusRequester), // Assign focus requester
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Show numeric keyboard
                )
            }
        })
    }
}

/**
 * Composable function for the shopping item editor UI.
 *
 * @param item The shopping item to edit.
 * @param onEditComplete Callback invoked when the edit is completed.
 */
@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (ShoppingItem) -> Unit) {
    var editedName by remember { mutableStateOf(item.name) } // State for the edited item name
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) } // State for the edited item quantity
    var inputErrorI by remember { mutableStateOf(false) } // Flag for item name input error
    var inputErrorQ by remember { mutableStateOf(false) } // Flag for item quantity input error

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(20)
            ), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            // Input field for editing the item name
            OutlinedTextField(value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.60f)
                    .padding(8.dp),
                isError = inputErrorI,
                supportingText = {
                    if (editedName.isBlank()) {
                        inputErrorI = true
                        Text(text = "Item Name cannot be empty")
                    } else {
                        inputErrorI = false
                    }
                },
                label = { Text(text = "Item Name") })
            // Input field for editing the item quantity
            OutlinedTextField(value = editedQuantity,
                onValueChange = { editedQuantity = it },
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.60f)
                    .padding(8.dp),
                isError = inputErrorQ,
                supportingText = {
                    if (editedQuantity.isBlank()) {
                        inputErrorQ = true
                        Text(text = "Item Quantity cannot be empty")
                    } else if (editedQuantity.toIntOrNull() == null) {
                        inputErrorQ = true
                        Text(text = "Item Quantity must be a number")
                    } else {
                        inputErrorQ = false
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "Item Quantity") })
        }
        // Button to save the edited item
        Button(
            onClick = {
                if (editedName.isNotBlank() && editedQuantity.isNotBlank()) {
                    if (editedQuantity.toIntOrNull() == null || editedQuantity.toIntOrNull() == 0) {
                        inputErrorQ = true
                    } else {
                        onEditComplete(
                            item.copy(
                                name = editedName,
                                quantity = editedQuantity.toIntOrNull() ?: 1,
                                isEditing = false
                            )
                        )
                    }
                } else {
                    inputErrorI = editedName.isBlank()
                    inputErrorQ = editedQuantity.isBlank()
                }
            }, modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Save", maxLines = 1, softWrap = false
            )
        }
    }
}

/**
 * Composable function for a single shopping list item UI.
 *
 * @param item The shopping item to display.
 * @param onEditClick Callback invoked when the edit button is clicked.
 * @param onDeleteClick Callback invoked when the delete button is clicked.
 * @param modifier Modifier for customizing the item's appearance.
 */
@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(20)
            ), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.name,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically)
                .offset(x = 8.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1
        )
        Text(
            text = "Qty: ${item.quantity}",
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1
        )
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

/**
 * Preview function for the ShoppingListApp composable.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShoppingListAppPreview() {
    ShoppingListApp(paddingValues = PaddingValues(8.dp))
}