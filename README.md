# FlatJ

A Kotlin tool to help "flatten" JSON documents to a tabular format.

## Author and license

Vasileios Nikakis

This tool is [MIT-licenced](LICENSE.txt).

## Synopsis and motivation

Data transformation of JSON documents to a tabular format (e.g. CSV, relational DB table, etc.) is a well known problem.

Depending on  how the data is structured inside a collection of JSON documents,
an equivalent representation to a tabular format is not always possible (e.g. half of the JSON documents may have
completely different fields than the other half).

FlatJ intends to analyze the structure of a collection of JSON documents and discover the common fields that they have
and as well their types.
This can help the user to decide whether the transformation of the data to a tabular format is feasible or not.

The tool is currently on its initial version and more features will be added with time.

## Installation and execution

Clone this repository.

A build artifact is included inside `build/libs`. To execute it (assuming that cwd is the root directory of the project):

    $ java -jar build/libs/flatj-0.0.1.jar

You can also build the executable yourself by using Gradle or through IntelliJ IDEA.

## Example

Let's assume a file `persons.json` that contains the following JSON data:

	[
        {
            "id" : 1,
            "name" : "John Doe",
            "age" : 25,
            "address" : "The Street #20"
        },
        {
            "id" : 2,
            "name" : "Ann Doe",
            "age" : "30y old",
            "profession" : "Physicist"
        },
        {
            "_id" : 3,
            "name" : "Nick Doe",
            "age" : 40,
            "address" : {
                "street" : "The Street",
                "number" : 40
            }
        },
        {
            "_id" : 4,
            "name" : "John Smit",
            "age" : 37,
            "address" : "A Street #50",
            "shifts" : [
                "08:00-16:00",
                "17:00-20:00"
            ]
        }
    ]

We can use `flatj` in the following way:

	$ java -jar build/libs/flatj-0.0.1.jar -f persons.json

The result will be the following:

    +-----------------------------------------------------+
    | keys           | types                | occurrences |
    |----------------|----------------------|-------------|
    | address        | String(2), Object(1) |           3 |
    | address.number | Number(1)            |           1 |
    | address.street | String(1)            |           1 |
    | age            | Number(3), String(1) |           4 |
    | id             | Number(4)            |           4 |
    | name           | String(4)            |           4 |
    | profession     | String(1)            |           1 |
    | shifts         | Array(1)             |           1 |
    +-----------------------------------------------------+

## Roadmap / future features

- Receive input from a MongoDB connection
- Analyze types of the contained items of a JSON array (the array has type of Array at the current version)
- Extend the functionality to a tool that transforms the tabular data to various formats (e.g. CSV, MySQL/Postgres tables, etc.)
