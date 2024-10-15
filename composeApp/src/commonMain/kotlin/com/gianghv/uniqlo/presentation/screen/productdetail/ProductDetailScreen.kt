package com.gianghv.uniqlo.presentation.screen.productdetail

import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import com.gianghv.uniqlo.domain.Image
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.BaseOutlinedButton
import com.gianghv.uniqlo.presentation.component.ExpandableText
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.component.MyAlertDialog
import com.gianghv.uniqlo.presentation.component.RedFilledTextButton
import com.gianghv.uniqlo.presentation.screen.home.RecommendProductList
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.presentation.screen.productdetail.components.AddCartBottomSheet
import com.gianghv.uniqlo.presentation.screen.productdetail.components.BrandBar
import com.gianghv.uniqlo.presentation.screen.productdetail.components.VariantPickerBottomSheet
import com.gianghv.uniqlo.presentation.screen.productdetail.components.VariationColorButton
import com.gianghv.uniqlo.presentation.screen.productdetail.components.VariationSizeButton
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
fun ProductDetailScreen(viewModel: ProductDetailViewModel, productId: Long?, onBack: () -> Unit, navigateTo: (MainScreenDestination) -> Unit) {
    val state by viewModel.state.asState()
    val scope = rememberCoroutineScope()

    val addCartBottomSheetState = rememberModalBottomSheetState(
        initialDetent = Hidden, detents = listOf(Hidden, FullyExpanded)
    )

    LaunchedEffect(Unit) {
        if (productId == null) onBack()
        else {
            viewModel.sendEvent(ProductDetailUiEvent.LoadProduct(productId))
            viewModel.sendEvent(ProductDetailUiEvent.LoadRecommendedProduct(productId))
        }
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        scope.launch {
            addCartBottomSheetState.animateTo(Hidden)
        }
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
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
                }
            })
        }) {
            Box(modifier = Modifier.fillMaxSize().padding(bottom = it.calculateBottomPadding())) {
                val screenHeight = getScreenHeightInDp().dp

                val scrollState = rememberScrollState()
                val productImages = product.images.let { it?.toMutableList() }.also { it?.add(0, Image(-1, product.defaultImage ?: "")) }?.distinctBy {
                    it.imagePath
                }
                Column(modifier = Modifier.fillMaxSize().background(Color.White).verticalScroll(scrollState)) {
                    ProductImagePanel(
                        modifier = Modifier.fillMaxWidth().height(screenHeight * 0.6f), images = productImages
                    )
                    ProductInfoPanel(modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        product = product,
                        state = state,
                        viewModel = viewModel,
                        onFavoriteClick = { product, isFavorite ->
                            viewModel.sendEvent(ProductDetailUiEvent.SetFavorite(product, isFavorite))
                        },
                        onProductClick = {
                            viewModel.sendEvent(ProductDetailUiEvent.LoadProduct(it.id))
                            viewModel.sendEvent(ProductDetailUiEvent.LoadRecommendedProduct(it.id))
                        })
                }

                AddToCartPanel(modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth().fillMaxHeight(0.1f).align(Alignment.BottomCenter),
                    enableAddToCart = true,
                    enableOrderNow = true,
                    onAddToCart = {
                        scope.launch {
                            addCartBottomSheetState.animateTo(FullyExpanded)
                        }
                    },
                    onOrderNow = {})

                AddCartBottomSheet(variation = state.selectedVariation,
                    size = state.selectedSize,
                    state = addCartBottomSheetState,
                    product = product,
                    onSelectColor = {
                        viewModel.sendEvent(ProductDetailUiEvent.SelectVariation(variation = it))
                    },
                    onSelectSize = {
                        viewModel.sendEvent(ProductDetailUiEvent.SelectVariation(size = it))
                    },
                    addToCart = { variation, size, quantity ->
                        viewModel.sendEvent(ProductDetailUiEvent.AddToCart(quantity, size, variation))
                    })

                if (state.addCartSuccess) {
                    scope.launch {
                        addCartBottomSheetState.animateTo(Hidden)
                    }
                    MyAlertDialog(title = "Thông báo",
                        content = "Sản phẩm đã được thêm vào giỏ hàng",
                        leftBtnTitle = "Tiếp tục",
                        rightBtnTitle = "Xem giỏ hàng",
                        leftBtn = {
                            onBack()
                        },
                        rightBtn = {
                            navigateTo(MainScreenDestination.Cart)
                        })
                }
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
            ProductImagePagerIndicator(
                modifier = Modifier.padding(end = 16.dp).align(Alignment.CenterEnd),
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
fun ProductInfoPanel(
    modifier: Modifier = Modifier,
    product: Product,
    state: ProductDetailUiState,
    viewModel: ProductDetailViewModel,
    onFavoriteClick: (Product, isFavorite: Boolean) -> Unit,
    onProductClick: (Product) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(
        initialDetent = Hidden, detents = listOf(Hidden, FullyExpanded)
    )

    val scope = rememberCoroutineScope()
    var boxWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Column(modifier = modifier.padding(16.dp).fillMaxWidth().wrapContentHeight().onGloballyPositioned { layoutCoordinates ->
        val widthInPx = layoutCoordinates.size.width
        boxWidth = with(density) { widthInPx.toDp() }
    }) {

        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            Text(
                text = product.name.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                maxLines = 3,
                minLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxSize().padding(end = 48.dp),
                textAlign = TextAlign.Justify
            )

            val isFavorite = product.isFavorite
            IconButton(onClick = {
                onFavoriteClick(product, !isFavorite)
            }, modifier = Modifier.size(24.dp).align(Alignment.TopEnd)) {
                if (isFavorite) Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.Red)
                else Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val categoryName = product.category?.name
        if (categoryName != null) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                minLines = 1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProductPrice(product = product)

        ProductRating(product = product)

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.wrapContentHeight().fillMaxWidth().clickable {
            scope.launch {
                bottomSheetState.animateTo(FullyExpanded)
            }
        }) {
            Column {
                Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                    Text("Variants", style = MaterialTheme.typography.titleSmall, color = Color.Black, modifier = Modifier.align(Alignment.CenterStart))
                    Text(
                        "Click to pick variants",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
                    val variation = state.selectedVariation
                    val size = state.selectedSize
                    if (variation != null) VariationColorButton(variation, isSelected = false, onSelected = {})
                    if (size != null) VariationSizeButton(size = size, isSelected = false, onSelected = {})
                }
            }
        }

        VariantPickerBottomSheet(state = bottomSheetState, product = product, variation = state.selectedVariation, size = state.selectedSize, onSelectColor = {
            viewModel.sendEvent(ProductDetailUiEvent.SelectVariation(variation = it))
        }, onSelectSize = {
            viewModel.sendEvent(ProductDetailUiEvent.SelectVariation(size = it))
        })

        Spacer(modifier = Modifier.height(16.dp))

        val brand = product.brand

        if (brand != null) {
            BrandBar(modifier = Modifier.fillMaxWidth(), brand = brand, onClick = {

            })
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp))

        val description = product.description
        if (!description.isNullOrEmpty()) {
            Text("Description", style = MaterialTheme.typography.titleSmall, color = Color.Black)

            ExpandableText(
                style = MaterialTheme.typography.bodySmall,
                fontSize = 13.sp,
                text = description,
                modifier = Modifier.padding(end = 48.dp).fillMaxWidth().wrapContentHeight(),
                textAlign = TextAlign.Justify
            )
        }

        val recommendedProducts = state.recommendedProducts
        if (recommendedProducts.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("May you like!", style = MaterialTheme.typography.titleSmall, color = Color.Black)
            RecommendProductList(boxWidth = boxWidth, modifier = Modifier.fillMaxWidth(), productList = recommendedProducts, onClick = {
                onProductClick(it)
            }, onFavoriteClick = { selectedProduct, isFavorite ->
                onFavoriteClick(selectedProduct, isFavorite)
            })
        }

        Spacer(modifier = Modifier.height(120.dp))
    }
}


@Composable
fun AddToCartPanel(modifier: Modifier, enableAddToCart: Boolean = false, enableOrderNow: Boolean = false, onAddToCart: () -> Unit, onOrderNow: () -> Unit) {
    Row(modifier = modifier.background(color = Color.Transparent)) {
        AddToCartButton(modifier = Modifier.weight(1f).padding(vertical = 8.dp, horizontal = 16.dp), enable = enableAddToCart, onClick = onAddToCart)
        OrderNowButton(modifier = Modifier.weight(1f).padding(vertical = 8.dp, horizontal = 16.dp), enable = enableOrderNow, onClick = onOrderNow)
    }
}

@Composable
fun AddToCartButton(modifier: Modifier = Modifier, enable: Boolean = false, onClick: () -> Unit) {
    if (enable) {
        BaseOutlinedButton(
            onClick = onClick,
            text = {
                Text(text = "Add to Cart", color = MaterialTheme.colorScheme.primary)
            },
            modifier = modifier.height(52.dp),
            enable = true,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary, containerColor = Color.White),
            borderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun OrderNowButton(modifier: Modifier = Modifier, enable: Boolean = false, onClick: () -> Unit) {
    if (enable) {
        RedFilledTextButton(modifier = modifier.height(52.dp), onClick = onClick) {
            Text(text = "Checkout", color = Color.White)
        }
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





