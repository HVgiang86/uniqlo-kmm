package com.gianghv.uniqlo.presentation.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpandableSearchView(
    searchDisplay: String,
    onSearchDisplayChanged: (String) -> Unit,
    onSearchDisplayClosed: () -> Unit,
    modifier: Modifier = Modifier,
    expandedInitially: Boolean = false,
    tint: Color = Color.Black
) {
    val (expanded, onExpandedChanged) = remember {
        mutableStateOf(expandedInitially)
    }

    Crossfade(targetState = expanded) { isSearchFieldVisible ->
        when (isSearchFieldVisible) {
            true -> ExpandedSearchView(
                searchDisplay = searchDisplay,
                onSearchDisplayChanged = onSearchDisplayChanged,
                onSearchDisplayClosed = onSearchDisplayClosed,
                onExpandedChanged = onExpandedChanged,
                modifier = modifier,
                tint = tint
            )

            false -> CollapsedSearchView(
                onExpandedChanged = onExpandedChanged, modifier = modifier, tint = tint
            )
        }
    }
}

@Composable
fun SearchIcon(iconTint: Color) {
    Icon(
        imageVector = Icons.Default.Search, contentDescription = "search icon", tint = iconTint
    )
}

@Composable
fun CollapsedSearchView(
    onExpandedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.Black,
) {

    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Tasks", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(start = 16.dp)
        )
        IconButton(onClick = { onExpandedChanged(true) }) {
            SearchIcon(iconTint = tint)
        }
    }
}

@Composable
fun ExpandedSearchView(
    searchDisplay: String,
    onSearchDisplayChanged: (String) -> Unit,
    onSearchDisplayClosed: () -> Unit,
    onExpandedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.Black,
) {
    val focusManager = LocalFocusManager.current

    val textFieldFocusRequester = remember { FocusRequester() }

    SideEffect {
        textFieldFocusRequester.requestFocus()
    }

    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(searchDisplay, TextRange(searchDisplay.length)))
    }

    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            onExpandedChanged(false)
            onSearchDisplayClosed()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back icon", tint = tint
            )
        }
        TextField(value = textFieldValue, onValueChange = {
            textFieldValue = it
            onSearchDisplayChanged(it.text)
        }, leadingIcon = {
            SearchIcon(iconTint = tint)
        }, modifier = Modifier.fillMaxWidth().focusRequester(textFieldFocusRequester), keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ), keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
        })
        )
    }
}


const val textFieldWidthWeight = 0.7f
const val textFieldHeightWeight = 0.05f

const val searchIconContentDescription = "Search Icon"
const val closeIconContentDescription = "Close Icon"

val textFieldCornerShape = 10.dp
val textFieldBackgroundColor = Color.LightGray
val textFieldBackgroundColor2 = Color(0xFFD5D5D5)
val textFieldPaddingStart = 15.dp

@Composable
private fun SearchTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    focusRequester: FocusRequester,
    onQueryChange: (String) -> Unit,
    onQueryDone: (KeyboardActionScope.() -> Unit)?,
    query: String
) {
    BasicTextField(value = query,
        onValueChange = onQueryChange,
        modifier = modifier.padding(start = textFieldPaddingStart).focusRequester(focusRequester = focusRequester),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = onQueryDone),
        decorationBox = { innerTextField ->
            if (query.isEmpty()) {
                Text(
                    text = placeholder, fontSize = 16.sp, fontWeight = FontWeight.Normal, color = Color.White
                )
            }
            innerTextField()
        })
}

@Composable
fun SearchTextFieldWrapper(
    modifier: Modifier = Modifier,
    title: String,
    placeholder: String,
    query: String,
    focusRequester: FocusRequester,
    onQueryChange: (String) -> Unit,
    onQueryDone: (KeyboardActionScope.() -> Unit)?,
    onClose: () -> Unit,
    textFieldColor: Color = Color.White
) {
    Row(
        modifier = modifier.fillMaxWidth(textFieldWidthWeight).fillMaxHeight(textFieldHeightWeight).border(
                border = BorderStroke(1.dp, textFieldColor), shape = RoundedCornerShape(textFieldCornerShape)
            ).clip(shape = RoundedCornerShape(textFieldCornerShape)).background(textFieldColor)
    ) {
        SearchTextField(
            modifier = modifier.weight(1f).align(Alignment.CenterVertically),
            query = query,
            placeholder = placeholder,
            focusRequester = focusRequester,
            onQueryChange = onQueryChange,
            onQueryDone = onQueryDone
        )
        IconButton(onClick = onClose) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = closeIconContentDescription,
            )
        }
    }
}
