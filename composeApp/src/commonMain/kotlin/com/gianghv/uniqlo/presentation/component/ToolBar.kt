package com.gianghv.uniqlo.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gianghv.uniqlo.domain.Product
import org.jetbrains.compose.resources.vectorResource
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_cart

@Composable
fun HomeToolBar(
    onSearchChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    cartCount: Int = 0,
    onMenuClick: () -> Unit,
    onCartClick: () -> Unit,
    searchSuggestion: List<Product> = emptyList()
) {
    var text by rememberSaveable { mutableStateOf("") }
    var searchRequired by remember {
        mutableStateOf(false)
    }

    AnimatedContent(targetState = searchRequired) {
        if (it) {
            SearchAppBarTitle(text = text, searchSuggestion = searchSuggestion, onSearchTap = { textSearch ->
                text = textSearch
                onSearch(textSearch)
                searchRequired = false
            }, onCancelTap = {
                searchRequired = false
            }, onValueChange = { changed ->
                text = changed
                onSearchChange(changed)
            })
        } else {
            MyAppBar(cartCount = cartCount, text = text, onSearch = {}, onValueChange = { changed ->
                text = changed
            }, onSearchTap = {
                searchRequired = true
            }, onMenuClick = onMenuClick, onCartClick = onCartClick)
        }
    }
}

@Composable
fun SearchToolBar(
    initText: String = "",
    onSearchChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onMenuClick: () -> Unit,
    onCartClick: () -> Unit,
    onNavigationClick: () -> Unit,
    searchSuggestion: List<Product> = emptyList()
) {
    var text by rememberSaveable { mutableStateOf(initText) }
    var searchRequired by remember {
        mutableStateOf(false)
    }

    AnimatedContent(targetState = searchRequired) {
        if (it) {
            SearchAppBarTitle(text = text, searchSuggestion = searchSuggestion, onSearchTap = { textSearch ->
                text = textSearch
                onSearch(textSearch)
                searchRequired = false
            }, onCancelTap = {
                searchRequired = false
            }, onValueChange = { changed ->
                text = changed
                onSearchChange(changed)
            })
        } else {
            MyAppBar(text = text, onSearch = {}, onValueChange = { changed ->
                text = changed
            }, onSearchTap = {
                searchRequired = true
            }, onMenuClick = onMenuClick, onCartClick = onCartClick, navigationIcon = {
                IconButton(onClick = onNavigationClick) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishListToolBar(
    cartCount: Int = 0,
    onMenuClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
) {
    TopAppBar(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), title = {
        Text(text = "Wishlist", style = MaterialTheme.typography.titleMedium, color = Color.Black)
    }, actions = {
        Box(modifier = Modifier.wrapContentSize()) {
            IconButton(onClick = onCartClick) {
                Icon(imageVector = vectorResource(Res.drawable.ic_cart), contentDescription = null)
            }

            if (cartCount > 0) {

                Box(modifier = Modifier.align(Alignment.TopEnd).padding(end = 4.dp, top = 4.dp).wrapContentSize().clip(CircleShape).background(Color.Red)) {
                    Text(
                        text = "$cartCount",
                        modifier = Modifier.padding(horizontal = 2.dp),
                        fontSize = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp).fontSize,
                        color = Color.White,
                    )
                }
            }
        }

        IconButton(onClick = onMenuClick) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppBar(
    text: String = "",
    cartCount: Int = 0,
    onSearch: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onSearchTap: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {}
) {
    TopAppBar(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), title = {
        AppOutlinedTextField(modifier = Modifier.padding(end = 8.dp).height(52.dp).fillMaxWidth(),
            placeholder = "Search",
            initialValue = text,
            onMessageSent = onSearch,
            onValueChange = onValueChange,
            imeAction = ImeAction.Search,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            maxLines = 1,
            textStyle = MaterialTheme.typography.bodyMedium,
            onClick = {
                onSearchTap()
            })
    }, actions = {
        Box(modifier = Modifier.wrapContentSize()) {
            IconButton(onClick = onCartClick) {
                Icon(imageVector = vectorResource(Res.drawable.ic_cart), contentDescription = null)
            }

            if (cartCount > 0) {

                Box(modifier = Modifier.align(Alignment.TopEnd).padding(end = 4.dp, top = 4.dp).wrapContentSize().clip(CircleShape).background(Color.Red)) {
                    Text(
                        text = "$cartCount",
                        modifier = Modifier.padding(horizontal = 2.dp),
                        fontSize = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp).fontSize,
                        color = Color.White,
                    )
                }
            }
        }

        IconButton(onClick = onMenuClick) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
        }
    }, navigationIcon = {
        navigationIcon()
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBarTitle(
    text: String, onSearchTap: (String) -> Unit, onValueChange: (String) -> Unit, onCancelTap: () -> Unit = {}, searchSuggestion: List<Product> = emptyList()
) {
    val focusManager = LocalFocusManager.current
    Box(Modifier.fillMaxWidth().wrapContentHeight().padding(top = 32.dp).semantics { isTraversalGroup = true }.pointerInput(Unit) {
        detectTapGestures(onTap = {
            focusManager.clearFocus()
        })
    }) {
        DockedSearchBar(modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp).semantics { traversalIndex = 0f },
            inputField = {
                AppOutlinedTextField(modifier = Modifier.height(52.dp).wrapContentHeight().fillMaxWidth(),
                    placeholder = "Search",
                    initialValue = text,
                    onMessageSent = {
                        onSearchTap(it)
                    },
                    onValueChange = onValueChange,
                    imeAction = ImeAction.Search,
                    leadingIcon = {
                        IconButton(
                            onClick = onCancelTap
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            onValueChange("")
                            onCancelTap()
                        }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                        }
                    },
                    maxLines = 1,
                    shape = RoundedCornerShape(8.dp),
                    textStyle = MaterialTheme.typography.bodySmall
                )
            },
            expanded = true,
            onExpandedChange = { },
            colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            shape = RoundedCornerShape(8.dp)
        ) {
            //this to show Search suggestions
            Column(Modifier.wrapContentHeight().verticalScroll(rememberScrollState())) {
                repeat(searchSuggestion.size) { index ->
                    val product = searchSuggestion[index]
                    ListItem(headlineContent = { Text(product.name.toString()) },
                        supportingContent = { Text(product.category?.name.toString()) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier.clickable {
                            onSearchTap(product.name.toString())
                        }.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
