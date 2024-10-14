package com.gianghv.uniqlo.presentation.screen.productdetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.Sheet
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.domain.ProductVariation
import com.gianghv.uniqlo.presentation.screen.productdetail.AddToCartButton
import com.gianghv.uniqlo.theme.Card_Border_Gray
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.rememberAsyncImageState
import com.github.panpf.sketch.request.ComposableImageOptions
import com.github.panpf.sketch.request.placeholder
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_dark_uniqlo

@Composable
fun VariantPickerBottomSheet(
    variation: ProductVariation? = null,
    size: VariationSize? = null,
    state: ModalBottomSheetState? = null,
    product: Product,
    onSelectColor: (ProductVariation) -> Unit,
    onSelectSize: (VariationSize) -> Unit
) {
    val show = state ?: rememberModalBottomSheetState(
        initialDetent = FullyExpanded, detents = listOf(Hidden, FullyExpanded)
    )

    ModalBottomSheet(state = show) {
        Sheet(
            modifier = Modifier.padding(top = 12.dp).shadow(8.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)).background(Color.White).widthIn(max = 640.dp).fillMaxWidth().imePadding(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp),
            ) {
                DragIndication(
                    modifier = Modifier.padding(top = 22.dp).align(Alignment.CenterHorizontally).background(Color.Black.copy(0.4f), RoundedCornerShape(100))
                        .width(32.dp).height(4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Product Variation", style = MaterialTheme.typography.titleSmall, color = Color.Black)

                Text(
                    "Please select the product variant currently available",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ProductVariationSelectSection(product = product, variation = variation, size = size, onSelectColor = onSelectColor, onSelectSize = onSelectSize)

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun AddCartBottomSheet(
    variation: ProductVariation? = null,
    size: VariationSize? = null,
    state: ModalBottomSheetState? = null,
    product: Product,
    onSelectColor: (ProductVariation) -> Unit,
    onSelectSize: (VariationSize) -> Unit,
    addToCart: (ProductVariation, VariationSize, Int) -> Unit
) {
    val show = state ?: rememberModalBottomSheetState(
        initialDetent = FullyExpanded, detents = listOf(Hidden, FullyExpanded)
    )

    var quantityState by remember {
        mutableStateOf(1)
    }

    var selectedVariation = variation

    ModalBottomSheet(state = show) {
        Sheet(
            modifier = Modifier.padding(top = 12.dp).shadow(8.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)).background(Color.White).widthIn(max = 640.dp).fillMaxWidth().imePadding(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
            ) {
                DragIndication(
                    modifier = Modifier.padding(top = 22.dp).align(Alignment.CenterHorizontally).background(Color.Black.copy(0.4f), RoundedCornerShape(100))
                        .width(32.dp).height(4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Product Variation", style = MaterialTheme.typography.titleSmall, color = Color.Black)

                Text(
                    "Please select the product variant currently available",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                selectedVariation?.let {
                    VariationCard(product, it, quantity = quantityState, onQuantityChanged = {
                        quantityState = it
                    })
                }

                Spacer(modifier = Modifier.height(32.dp))

                ProductVariationSelectSection(product = product, variation = variation, size = size, onSelectColor = {
                    onSelectColor(it)
                    selectedVariation = it
                }, onSelectSize = onSelectSize)

                AddToCartButton(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), enable = true, onClick = {
                    if (variation != null && size != null) addToCart(variation, size, quantityState)
                })

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun VariationCard(product: Product, variation: ProductVariation, modifier: Modifier = Modifier, quantity: Int = 1, onQuantityChanged: (Int) -> Unit) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(width = 1.dp, color = Card_Border_Gray),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp, pressedElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp)) {
            Box(modifier = Modifier.align(Alignment.Top).width(180.dp).aspectRatio(16.0f / 9)) {
                val isImageLoadedSuccessfully = rememberAsyncImageState(ComposableImageOptions {
                    placeholder(Res.drawable.ic_dark_uniqlo)
                    crossfade()
                })

                AsyncImage(
                    uri = variation.image.toString(),
                    modifier = modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
                    state = isImageLoadedSuccessfully,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }

            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().align(Alignment.Top).padding(start = 8.dp)) {
                Text(product.name ?: "", style = MaterialTheme.typography.bodyMedium, color = Color.Black)

                val color1 = variation.color?.toVariationColor()
                Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                    Box(modifier = Modifier.size(8.dp).padding(end = 8.dp).clip(CircleShape).background(color = color1?.color ?: Color.White)) {}
                    Text(variation.color ?: "", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                }

                Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                    IconButton(onClick = {
                        if (quantity > 1) onQuantityChanged(quantity - 1)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }

                    Text(
                        quantity.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 8.dp).align(Alignment.CenterVertically)
                    )

                    IconButton(onClick = {
                        if (variation.stock != null && quantity < variation.stock) onQuantityChanged(quantity + 1)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductVariationSelectSection(
    variation: ProductVariation? = null,
    size: VariationSize? = null,
    product: Product,
    onSelectColor: (ProductVariation) -> Unit,
    onSelectSize: (VariationSize) -> Unit
) {
    var variationSelectState by remember {
        mutableStateOf(variation)
    }

    var sizeSelectState by remember {
        mutableStateOf(size)
    }

    Text("Size", style = MaterialTheme.typography.titleSmall, color = Color.Black)

    Spacer(modifier = Modifier.height(4.dp))

    ProductSizeSelectSection(selected = sizeSelectState, onSelect = {
        sizeSelectState = it
        onSelectSize(it)
    })

    Spacer(modifier = Modifier.height(16.dp))

    Text("Select color", style = MaterialTheme.typography.titleSmall, color = Color.Black)

    Spacer(modifier = Modifier.height(4.dp))

    val variations = product.variations?.distinctBy { it1 -> it1.color?.lowercase() }
    variations?.let {
        ProductColorSelectSection(variations = it, selected = variationSelectState, onSelect = { selectedVariation ->
            variationSelectState = selectedVariation
            onSelectColor(selectedVariation)
        })
    }
}
