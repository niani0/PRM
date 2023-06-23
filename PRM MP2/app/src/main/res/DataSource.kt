package com.example.prmmp1poprawka

object DataSource {
    val animals = mutableListOf<Animal>(
        Animal(
            0,
            "Monkey",
            "Simiiformes",
            R.drawable.monke,
            "Monkeys are intelligent animals that are good at solving problems. Almost all types live together in groups. A monkey group commonly includes several related females, their young, and one or more males."
        ),
        Animal(
            1,
            "Parrot",
            "Psittaciformes",
            R.drawable.parrot,
            "Parrots are beautiful medium-sized birds that live in forests. Parrots are not only found in India but also in various warm countries. "
        ),
        Animal(
            2,
            "Penguin",
            "Spheniscidae",
            R.drawable.pingu,
            "Penguins are flightless birds with flippers instead of wings. Their bodies are adapted for swimming and diving in the water, with some species able to reach speeds up to 15 miles per hour."
        ),
        Animal(
            3,
            "Tiger",
            "Panthera tigris",
            R.drawable.tiger,
            "Easily recognized by its coat of reddish-orange with dark stripes, the tiger is the largest wild cat in the world. The big cat's tail is three feet long. On average the big cat weighs 450 pounds, about the same as eight ten-year-old kids."
        ),
        Animal(
            4,
            "Moose",
            "Alces alces",
            R.drawable.moose,
            "The moose is the largest species of deer, found in the boreal forests of North America, Europe, and Asia. They are known for their distinctive appearance, with large antlers on males and a long snout."
        ),
        Animal(
            5,
            "Sheep",
            "Ovis aries",
            R.drawable.sheep,
            "Sheep, species of domesticated ruminant (cud-chewing) mammal, raised for its meat, milk, and wool."
        )
    )
    var animal: Animal = animals[0]
    var flag: String = "edit"
}