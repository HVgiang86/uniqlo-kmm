package com.gianghv.uniqlo.data

import com.gianghv.uniqlo.domain.Evaluation
import com.gianghv.uniqlo.domain.User

object FakeData {
    val user1 = User(
        id = 9, name = "Anna", email = "test1@gmail.com", phone = "035556528", imagePath = null, gender = "male", role = "user"
    )

    val user2 = User(
        id = 10, name = "Huyen", email = "huyen@gmail.com", phone = null, imagePath = null, gender = "female", role = "user"
    )


    val evaluation1 = Evaluation(
        id = 1, star = 4.5, content = "I love this product", account = user1
    )

    val evaluation2 = Evaluation(
        id = 2, star = 3.5, content = "I hate this product", account = user2
    )

    val evaluation3 = Evaluation(
        id = 3,
        star = 5.0,
        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Culpa clita nulla dolores veniam obcaecat nibh blandit nisl et nam clita sit proident voluptate in hendrerit. Eros gubergren quod illum voluptua voluptate dolor ullamco, qui aliquyam obcaecat luptatum zzril sanctus elit molestie rebum nonummy suscipit consectetur. Fugiat excepteur congue.\n" + "\n" + "Zzril ullamcorper erat dignissim. Sint cillum invidunt. Amet ea blandit.",
        account = user1
    )

    val evaluations = mutableListOf<Evaluation>().apply {
        add(evaluation1)
        add(evaluation3)
        add(evaluation2)
        add(evaluation1.copy(id = 4))
        add(evaluation2.copy(id = 5))
        add(evaluation3.copy(id = 6))
    }
}
