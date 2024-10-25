package com.gianghv.uniqlo.presentation.screen.productdetail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.domain.Evaluation
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.rememberAsyncImageState
import com.github.panpf.sketch.request.ComposableImageOptions
import com.github.panpf.sketch.request.placeholder
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_default_avatar

@Composable
fun EvaluationCard(modifier: Modifier = Modifier, evaluation: Evaluation) {
    Column(modifier = modifier.fillMaxWidth().wrapContentHeight()) {
        EvaluationHeader(evaluation = evaluation)
        Spacer(modifier = Modifier.height(8.dp))
        EvaluationRating(evaluation = evaluation)
        Spacer(modifier = Modifier.height(8.dp))
        EvaluationContent(evaluation = evaluation)
        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), color = Color.LightGray, thickness = 1.dp)
    }
}

@Composable
fun EvaluationHeader(modifier: Modifier = Modifier, evaluation: Evaluation) {
    Row(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.align(Alignment.Top).width(24.dp).aspectRatio(1.0f).clip(CircleShape)) {
            val isImageLoadedSuccessfully = rememberAsyncImageState(ComposableImageOptions {
                placeholder(Res.drawable.ic_default_avatar)
                crossfade()
            })

            AsyncImage(
                uri = evaluation.account.imagePath ?: "",
                modifier = Modifier.fillMaxSize().clip(CircleShape),
                state = isImageLoadedSuccessfully,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = evaluation.account.name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun EvaluationRating(modifier: Modifier = Modifier, evaluation: Evaluation) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = evaluation.star.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            imageVector = Icons.Default.Star, contentDescription = null, modifier = Modifier.size(24.dp).align(Alignment.CenterVertically), tint = Color.Yellow
        )
    }
}

@Composable
fun EvaluationContent(modifier: Modifier = Modifier, evaluation: Evaluation) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = evaluation.content,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        minLines = 1,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis
    )
}
