package com.gianghv.uniqlo.presentation.screen.productdetail

import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.domain.Image
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.domain.ProductVariation
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.theme.Orange
import com.gianghv.uniqlo.theme.Silver
import com.gianghv.uniqlo.util.ValidateHelper
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.ext.round
import com.gianghv.uniqlo.util.ext.toCurrencyText
import com.gianghv.uniqlo.util.getScreenHeightInDp
import com.gianghv.uniqlo.util.logging.AppLogger
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.rememberAsyncImageState
import com.github.panpf.sketch.request.ComposableImageOptions
import com.github.panpf.sketch.request.placeholder
import kotlinx.coroutines.launch
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_dark_uniqlo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(viewModel: ProductDetailViewModel, productId: Long?, onBack: () -> Unit) {
    val state by viewModel.state.asState()

    LaunchedEffect(Unit) {
        if (productId == null) onBack()
        else viewModel.sendEvent(ProductDetailUiEvent.LoadProduct(productId))
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        AppErrorDialog(state.error?.throwable, onDismissRequest = {})
    }

    val product = state.product
    if (product != null) {
        AppLogger.d("ProductDetailScreen: $product")
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent), title = {}, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            }, actions = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
                }
            })
        }) {
            Box(modifier = Modifier.fillMaxSize().padding(bottom = it.calculateBottomPadding())) {
                val screenHeight = getScreenHeightInDp().dp

                val scrollState = rememberScrollState()
                Column(modifier = Modifier.fillMaxSize().background(Color.White).verticalScroll(scrollState)) {
                    ProductImagePanel(
                        modifier = Modifier.fillMaxWidth().height(screenHeight * 0.6f), images = product.images
                    )
                    ProductInfoPanel(modifier = Modifier.fillMaxWidth().wrapContentHeight(), product = product)
                }

//                AddToCartPanel(modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth().fillMaxHeight(0.1f).align(Alignment.BottomCenter))
            }
        }
    }
}

@Composable
fun ProductImagePanel(modifier: Modifier = Modifier, images: List<Image>? = emptyList()) {
    val scrollState = rememberScrollState()
    LaunchedEffect(true) {
        scrollState.animateScrollTo(scrollState.maxValue, tween(1500))
    }

    val filteredImages = images?.filter {
        val error = ValidateHelper.ValidateIsUrl(it.imagePath)
        error == null
    }

    Box(modifier = modifier) {
        val pagerState = rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f, pageCount = { filteredImages?.size ?: 0 })

        VerticalPager(state = pagerState, modifier = modifier.fillMaxSize()) { pageIndex ->
            val image = filteredImages?.get(pageIndex)
            ProductImagePager(
                imageUrl = image?.imagePath, modifier = Modifier.fillMaxSize()
            )
        }
        val coroutineScope = rememberCoroutineScope()

        if ((filteredImages?.size ?: 0) > 1) {
            ProductImagePagerIndicator(modifier = Modifier.padding(end = 16.dp).align(Alignment.CenterEnd),
                pagerState = pagerState,
                onClickIndicator = { index ->
                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                })
        }
    }
}

@Composable
fun ProductImagePager(modifier: Modifier = Modifier, imageUrl: String?) {
    val urlError = ValidateHelper.ValidateIsUrl(imageUrl ?: "")

    if (urlError != null) {
        return
    }

    Box(modifier = modifier) {
        val isImageLoadedSuccessfully = rememberAsyncImageState(ComposableImageOptions {
            placeholder(Res.drawable.ic_dark_uniqlo)
            crossfade()
        })

        AsyncImage(
            uri = imageUrl ?: "",
            modifier = modifier.fillMaxSize(),
            state = isImageLoadedSuccessfully,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}

@Composable
fun ProductImagePagerIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onClickIndicator: (Int) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            if (pagerState.currentPage == iteration) Box(
                modifier = Modifier.padding(2.dp).size(8.dp).clip(CircleShape).background(color = MaterialTheme.colorScheme.secondary, shape = CircleShape)
            )
            else Box(modifier = Modifier.padding(2.dp).size(8.dp).clip(CircleShape).background(color = Color.Gray, shape = CircleShape)
                .clickable { onClickIndicator(iteration) })
        }
    }
}

@Composable
fun ProductInfoPanel(modifier: Modifier = Modifier, product: Product) {
    Column(
        modifier = modifier.padding(16.dp).fillMaxWidth().wrapContentHeight()
    ) {
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            Text(
                text = "Long Sleeve T-Shirt. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vulputate suscipit deserunt sea voluptua duis, praesent delenit praesent tempor enim, eum aliquid adipiscing qui accusam tempor possim ea molestie proident culpa, soluta hendrerit tincidunt erat lorem doming quis soluta sea duo iure ea. Tincidunt eleifend tation, odio cum ullamco consequat elitr pariatur enim sanctus erat non tincidunt elitr laoreet minim eos culpa nihil, placerat culpa enim luptatum id soluta excepteur no consetetur eirmod lobortis tempor eiusmod amet adipiscing dignissim lorem aliquam. Mazim sea at hendrerit aliquid. Non accusam esse.\n",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                maxLines = 3,
                minLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 48.dp).fillMaxWidth().wrapContentHeight()
            )

            Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.Gray)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Men",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            minLines = 1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProductPrice(product = product)

        ProductRating(product = product)

        ProductVariationPreview()

        product.variations?.let { ProductVariationPanel(variations = it) }
    }
}


@Composable
fun AddToCartPanel(modifier: Modifier) {
    Surface(modifier = modifier, color = Color.Green) {

    }
}

@Composable
fun ProductPrice(modifier: Modifier = Modifier, product: Product) {
    val originalPrice = (product.price ?: 0.0) * 1000
    val discountedPrice: Double = if (product.discountPercentage != null && product.price != null) {
        originalPrice - (originalPrice * (product.discountPercentage * 1.0 / 100))
    } else originalPrice

    val originalPriceStr = originalPrice.toCurrencyText()
    val discountedPriceStr = discountedPrice.toCurrencyText()

    if (originalPrice != discountedPrice) {
        Column(modifier = modifier.fillMaxWidth().wrapContentHeight()) {
            Text(
                text = discountedPriceStr,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red,
                maxLines = 1,
                minLines = 1,
                overflow = TextOverflow.Clip
            )

            Text(
                originalPriceStr,
                style = MaterialTheme.typography.titleMedium,
                textDecoration = TextDecoration.LineThrough,
                color = Color.Black,
                maxLines = 1,
                minLines = 1,
                overflow = TextOverflow.Clip
            )
        }
    } else {
        Column(modifier = modifier.fillMaxWidth().wrapContentHeight()) {
            Text(
                "", style = MaterialTheme.typography.titleMedium, color = Color.Red, maxLines = 1, minLines = 1, overflow = TextOverflow.Clip
            )

            Text(
                originalPriceStr, style = MaterialTheme.typography.titleMedium, color = Color.Black, maxLines = 1, minLines = 1, overflow = TextOverflow.Clip
            )
        }
    }
}

@Composable
fun ProductRating(modifier: Modifier = Modifier, product: Product) {
    val starts = (product.averageRating ?: 0.0).round(2)

    var ratingRowModifier = Modifier.fillMaxWidth().wrapContentHeight()

    if (starts <= 0) {
        ratingRowModifier = ratingRowModifier.alpha(0f)
    }

    Row(modifier = ratingRowModifier) {
        Icon(
            imageVector = Icons.Default.Star, contentDescription = null, modifier = Modifier.size(24.dp).align(Alignment.CenterVertically), tint = Color.Yellow
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = "${product.averageRating}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "(${product.numberRating ?: 0} Reviews)",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterVertically),
            maxLines = 1,
            minLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductVariationPreview() {
    Text(text = "Select Size")

    val sizeSelectState = remember { mutableStateOf<VariationSize?>(null) }

    FlowRow {
        val sizeList = listOf(VariationSize.XS, VariationSize.S, VariationSize.M, VariationSize.L, VariationSize.XL, VariationSize.XXL)

        sizeList.forEach { size ->
            VariationSizeButton(size = size, isSelected = size == sizeSelectState.value, onSelected = {
                sizeSelectState.value = it
            })
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductVariationPanel(variations: List<ProductVariation>) {
    Text(text = "Select Color")

    val colorSelectState = remember { mutableStateOf<ProductVariation?>(null) }

    FlowRow {
        variations.forEach { variation ->
            VariationColorButton(variation = variation, isSelected = variation == colorSelectState.value, onSelected = {
                colorSelectState.value = it
            })
        }
    }
}

@Composable
fun VariationColorButton(variation: ProductVariation, isSelected: Boolean, onSelected: (ProductVariation) -> Unit) {
    if (variation.color == null) return
    val color = variation.color.toVariationColor()

    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF800080), Color(0xFFDA70D6)) // Purple to Orchid gradient
    )
    val unselectedColor = Color.Gray

    val outlineBrush = if (isSelected) gradientBrush else Brush.linearGradient(listOf(unselectedColor, unselectedColor))
    val textColor = color.getTextColor()

    val selectedIndicatorColor = if (color == VariationColor.RED) Color.White else Color.Red
    val selectedIndicatorIconColor = if (color == VariationColor.RED) Color.Red else Color.White

    Box(modifier = Modifier.wrapContentSize().padding(4.dp)) {
        OutlinedButton(
            onClick = {
                onSelected(variation)
            },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, outlineBrush),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor, containerColor = color.color)
        ) {
            Text(text = variation.color.uppercase(), color = textColor)
        }

        if (isSelected) {
            Box(
                modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 2.dp).background(selectedIndicatorColor)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = selectedIndicatorIconColor,
                    modifier = Modifier.size(16.dp).align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
fun VariationSizeButton(size: VariationSize, isSelected: Boolean, onSelected: (VariationSize) -> Unit) {
    val selectedColor = MaterialTheme.colorScheme.primary
    val unselectedColor = Color.Gray

    val outlineColor = if (isSelected) selectedColor else unselectedColor
    val textColor = if (isSelected) Color.White else Color.Black
    val backgroundColor = if (isSelected) selectedColor else Color.Transparent

    OutlinedButton(
        onClick = {
            onSelected(size)
        },
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, outlineColor),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor, containerColor = backgroundColor)
    ) {
        Text(text = size.name.uppercase(), color = textColor)
    }
}

enum class VariationColor(val color: Color) {
    BLUE(Color.Blue), GREY(Color.Gray), BLACK(Color.Black), YELLOW(Color.Yellow), RED(Color.Red), WHITE(Color.White), PINK(Color.Magenta), SILVER(Silver), GREEN(
        Color.Green
    ),
    ORANGE(Orange), PURPLE(Color(0xFF800080)), GRAY(Color.Gray), OTHER(Color.White)
}

fun VariationColor.getTextColor(): Color {
    return when (this) {
        VariationColor.BLACK, VariationColor.BLUE, VariationColor.GREY, VariationColor.RED, VariationColor.PURPLE -> Color.White
        else -> Color.Black
    }
}

fun String.toVariationColor(): VariationColor {
    return when (this.lowercase()) {
        "blue" -> VariationColor.BLUE
        "grey" -> VariationColor.GREY
        "black" -> VariationColor.BLACK
        "yellow" -> VariationColor.YELLOW
        "red" -> VariationColor.RED
        "white" -> VariationColor.WHITE
        "ping" -> VariationColor.PINK
        "silver" -> VariationColor.SILVER
        "green" -> VariationColor.GREEN
        "orange" -> VariationColor.ORANGE
        "purple" -> VariationColor.PURPLE
        "gray" -> VariationColor.GRAY
        else -> VariationColor.OTHER
    }
}

enum class VariationSize(val size: String) {
    XS("XS"), S("S"), M("M"), L("L"), XL("XL"), XXL("XXL"),
}
