package com.gianghv.uniqlo.data

import com.gianghv.uniqlo.domain.Brand
import com.gianghv.uniqlo.domain.Category
import com.gianghv.uniqlo.domain.Image
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.domain.ProductVariation
import com.gianghv.uniqlo.domain.User

object FakeData {
    val products = listOf(
        Product(
            id = 1,
            name = "Uniqlo T-Shirt",
            price = 19.99,
            description = "A comfortable and stylish t-shirt.",
            specifications = "100% cotton",
            defaultImage = "https://example.com/images/tshirt.jpg",
            averageRating = 4.5,
            numberRating = 100,
            discountPercentage = 10,
            active = true,
            createdAt = "2023-01-01",
            brand = Brand(id = 1, name = "Uniqlo"),
            images = listOf(
                Image(id = 1, imagePath = "https://example.com/images/tshirt1.jpg"), Image(id = 2, imagePath = "https://example.com/images/tshirt2.jpg")
            ),
            variations = listOf(
                ProductVariation(id = 3, color = "Red", price = 19.99),
                ProductVariation(id = 4, color = "Black", price = 19.99),
                ProductVariation(id = 5, color = "White", price = 20.99)
            ),
            category = Category(id = 1, name = "Clothing")
        ), Product(
            id = 2,
            name = "Uniqlo Jeans",
            price = 49.99,
            description = "Stylish and durable jeans.",
            specifications = "98% cotton, 2% elastane",
            defaultImage = "https://example.com/images/jeans.jpg",
            averageRating = 4.7,
            numberRating = 200,
            discountPercentage = 15,
            active = true,
            createdAt = "2023-02-01",
            brand = Brand(id = 1, name = "Uniqlo"),
            images = listOf(
                Image(id = 3, imagePath = "https://example.com/images/jeans1.jpg"), Image(id = 4, imagePath = "https://example.com/images/jeans2.jpg")
            ),
            variations = listOf(
                ProductVariation(id = 3, color = "Red", price = 49.99),
                ProductVariation(id = 4, color = "Black", price = 50.99),
                ProductVariation(id = 5, color = "White", price = 49.99)
            ),
            category = Category(id = 2, name = "Pants")
        ),
        Product(
            id = 3,
            name = "Uniqlo Jeans",
            price = 49.99,
            description = "Stylish and durable jeans.",
            specifications = "98% cotton, 2% elastane",
            defaultImage = "https://example.com/images/jeans.jpg",
            averageRating = 4.7,
            numberRating = 200,
            discountPercentage = 15,
            active = true,
            createdAt = "2023-02-01",
            brand = Brand(id = 1, name = "Uniqlo"),
            images = listOf(
                Image(id = 3, imagePath = "https://example.com/images/jeans1.jpg"), Image(id = 4, imagePath = "https://example.com/images/jeans2.jpg")
            ),
            variations = listOf(
                ProductVariation(id = 3, color = "Red", price = 49.99),
                ProductVariation(id = 4, color = "Black", price = 50.99),
                ProductVariation(id = 5, color = "White", price = 49.99)
            ),
            category = Category(id = 2, name = "Pants")
        ),
        Product(
            id = 4,
            name = "Uniqlo Jeans",
            price = 49.99,
            description = "Stylish and durable jeans.",
            specifications = "98% cotton, 2% elastane",
            defaultImage = "https://example.com/images/jeans.jpg",
            averageRating = 4.7,
            numberRating = 200,
            discountPercentage = 15,
            active = true,
            createdAt = "2023-02-01",
            brand = Brand(id = 1, name = "Uniqlo"),
            images = listOf(
                Image(id = 3, imagePath = "https://example.com/images/jeans1.jpg"), Image(id = 4, imagePath = "https://example.com/images/jeans2.jpg")
            ),
            variations = listOf(
                ProductVariation(id = 3, color = "Red", price = 49.99),
                ProductVariation(id = 4, color = "Black", price = 50.99),
                ProductVariation(id = 5, color = "White", price = 49.99)
            ),
            category = Category(id = 2, name = "Pants")
        ),
        Product(
            id = 5,
            name = "Uniqlo Jeans",
            price = 49.99,
            description = "Stylish and durable jeans.",
            specifications = "98% cotton, 2% elastane",
            defaultImage = "https://example.com/images/jeans.jpg",
            averageRating = 4.7,
            numberRating = 200,
            discountPercentage = 15,
            active = true,
            createdAt = "2023-02-01",
            brand = Brand(id = 1, name = "Uniqlo"),
            images = listOf(
                Image(id = 3, imagePath = "https://example.com/images/jeans1.jpg"), Image(id = 4, imagePath = "https://example.com/images/jeans2.jpg")
            ),
            variations = listOf(
                ProductVariation(id = 3, color = "Red", price = 49.99),
                ProductVariation(id = 4, color = "Black", price = 50.99),
                ProductVariation(id = 5, color = "White", price = 49.99)
            ),
            category = Category(id = 2, name = "Pants")
        ),
        Product(
            id = 6,
            name = "Uniqlo Jeans",
            price = 49.99,
            description = "Stylish and durable jeans.",
            specifications = "98% cotton, 2% elastane",
            defaultImage = "https://example.com/images/jeans.jpg",
            averageRating = 4.7,
            numberRating = 200,
            discountPercentage = 15,
            active = true,
            createdAt = "2023-02-01",
            brand = Brand(id = 1, name = "Uniqlo"),
            images = listOf(
                Image(id = 3, imagePath = "https://example.com/images/jeans1.jpg"), Image(id = 4, imagePath = "https://example.com/images/jeans2.jpg")
            ),
            variations = listOf(
                ProductVariation(id = 3, color = "Red", price = 49.99),
                ProductVariation(id = 4, color = "Black", price = 50.99),
                ProductVariation(id = 5, color = "White", price = 49.99)
            ),
            category = Category(id = 2, name = "Pants")
        ),
        Product(
            id = 7,
            name = "Uniqlo Jeans",
            price = 49.99,
            description = "Stylish and durable jeans.",
            specifications = "98% cotton, 2% elastane",
            defaultImage = "https://example.com/images/jeans.jpg",
            averageRating = 4.7,
            numberRating = 200,
            discountPercentage = 15,
            active = true,
            createdAt = "2023-02-01",
            brand = Brand(id = 1, name = "Uniqlo"),
            images = listOf(
                Image(id = 3, imagePath = "https://example.com/images/jeans1.jpg"), Image(id = 4, imagePath = "https://example.com/images/jeans2.jpg")
            ),
            variations = listOf(
                ProductVariation(id = 3, color = "Red", price = 49.99),
                ProductVariation(id = 4, color = "Black", price = 50.99),
                ProductVariation(id = 5, color = "White", price = 49.99)
            ),
            category = Category(id = 2, name = "Pants")
        ),
        Product(
            id = 8,
            name = "Uniqlo Jeans",
            price = 49.99,
            description = "Stylish and durable jeans.",
            specifications = "98% cotton, 2% elastane",
            defaultImage = "https://example.com/images/jeans.jpg",
            averageRating = 4.7,
            numberRating = 200,
            discountPercentage = 15,
            active = true,
            createdAt = "2023-02-01",
            brand = Brand(id = 1, name = "Uniqlo"),
            images = listOf(
                Image(id = 3, imagePath = "https://example.com/images/jeans1.jpg"), Image(id = 4, imagePath = "https://example.com/images/jeans2.jpg")
            ),
            variations = listOf(
                ProductVariation(id = 3, color = "Red", price = 49.99),
                ProductVariation(id = 4, color = "Black", price = 50.99),
                ProductVariation(id = 5, color = "White", price = 49.99)
            ),
            category = Category(id = 2, name = "Pants")
        ),
        Product(
            id = 9,
            name = "Uniqlo Jeans",
            price = 49.99,
            description = "Stylish and durable jeans.",
            specifications = "98% cotton, 2% elastane",
            defaultImage = "https://example.com/images/jeans.jpg",
            averageRating = 4.7,
            numberRating = 200,
            discountPercentage = 15,
            active = true,
            createdAt = "2023-02-01",
            brand = Brand(id = 1, name = "Uniqlo"),
            images = listOf(
                Image(id = 3, imagePath = "https://example.com/images/jeans1.jpg"), Image(id = 4, imagePath = "https://example.com/images/jeans2.jpg")
            ),
            variations = listOf(
                ProductVariation(id = 3, color = "Red", price = 49.99),
                ProductVariation(id = 4, color = "Black", price = 50.99),
                ProductVariation(id = 5, color = "White", price = 49.99)
            ),
            category = Category(id = 2, name = "Pants")
        ),
        Product(
            id = 10,
            name = "Uniqlo Jeans",
            price = 49.99,
            description = "Stylish and durable jeans.",
            specifications = "98% cotton, 2% elastane",
            defaultImage = "https://example.com/images/jeans.jpg",
            averageRating = 4.7,
            numberRating = 200,
            discountPercentage = 15,
            active = true,
            createdAt = "2023-02-01",
            brand = Brand(id = 1, name = "Uniqlo"),
            images = listOf(
                Image(id = 3, imagePath = "https://example.com/images/jeans1.jpg"), Image(id = 4, imagePath = "https://example.com/images/jeans2.jpg")
            ),
            variations = listOf(
                ProductVariation(id = 3, color = "Red", price = 49.99),
                ProductVariation(id = 4, color = "Black", price = 50.99),
                ProductVariation(id = 5, color = "White", price = 49.99)
            ),
            category = Category(id = 2, name = "Pants")
        )
    )

    val user = User(
        id = 3,
        name = "Hoang Van Giang",
        email = "HVGiang86@gmail.com",
        phone = null,
        imagePath = null,
        gender = "other",
        role = "user",
        active = true,
        passwordReset = null,
        birthday = "08/06/2002",
        wishList = listOf(1,3)
    )
}
